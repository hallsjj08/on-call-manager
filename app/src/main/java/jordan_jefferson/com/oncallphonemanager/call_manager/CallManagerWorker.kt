package jordan_jefferson.com.oncallphonemanager.call_manager

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import jordan_jefferson.com.oncallphonemanager.data.OnCallItem
import jordan_jefferson.com.oncallphonemanager.data.OnCallItemRepository
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.*
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CallManagerWorker(val context: Context, params: WorkerParameters) :
        Worker(context, params) {

    private val onCallItemRepository = OnCallItemRepository(context)
    private val onCallItems = onCallItemRepository.allActiveOnCallItems

    private val calendar = GregorianCalendar.getInstance()!!
    private val currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())!!

    private val TAG = "CALL MANAGER SERVICE"

    /**
     * Override this method to do your actual background processing.
     */
    override fun doWork(): Result {

        if(onCallItems == null || onCallItems.isEmpty()) return Result.SUCCESS

        JodaTimeAndroid.init(context)

        Log.d(TAG, "Items Size: ${onCallItems.size}")

        val isActive = checkActiveOnCallItems(onCallItems)

        val flag: Int
        if (isActive) {
            flag = PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        } else{
            flag = PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            prepareNearestActiveItem(onCallItems)
        }

        val componentName = ComponentName(context, IncomingCallReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(componentName, flag, PackageManager.DONT_KILL_APP)


        Log.d(TAG, "SUCCESS: " + DateTime.now().toString())
        return Result.SUCCESS
    }

    private fun checkActiveOnCallItems(onCallItems: List<OnCallItem>?): Boolean{

        if(onCallItems == null) return false

        Log.d(TAG, currentDay)

        val currentDate = calendar.time
        val currentTime = DateTime(currentDate)
        var startTime: DateTime
        var endTime: DateTime

        onCallItems.forEach{
            if(it.day == currentDay || it.day == null){

                startTime = dateFormatter(it.displayStartTime)
                endTime = dateFormatter(it.displayEndTime)

                if(currentTime.isAfter(endTime.minusMinutes(1)) &&
                        currentTime.isBefore(endTime.plusMinutes(1)) && it.day == null){
                    Log.d(TAG, "Deactivate")
                    it.isActive = false
                    onCallItemRepository.insertOnCallItemsAsync(arrayOf(it))
                    return@forEach
                }

                if(currentTime.isAfter(startTime.minusSeconds(10)) &&
                        (endTime.isBefore(startTime) || currentTime.isBefore(endTime))){

                    prepareFutureOnCallTime(currentTime, startTime, endTime, true)
                    return true

                }
            }
        }

        Log.d(TAG, "Item Active false")
        return false
    }

    private fun prepareFutureOnCallTime(currentTime: DateTime, startTime: DateTime,
                                endTime: DateTime, isActive: Boolean){

        if(isActive && endTime.isBefore(startTime)){
            Log.d(TAG, "End time is tomorrow $endTime")
            val delayMinutes = calculateDateDiffSeconds(currentTime, endTime.plusDays(1))
            prepareWorker(delayMinutes)
        }else if(isActive && currentTime.isBefore(endTime)){
            Log.d(TAG, "End time is on the same day")
            val delayMinutes = calculateDateDiffSeconds(currentTime, endTime)
            prepareWorker(delayMinutes)
        }
    }

    private fun prepareWorker(delaySeconds: Long){

        val callManagerWorker = OneTimeWorkRequest.Builder(CallManagerWorker::class.java)
                .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance().enqueue(callManagerWorker)
        Log.d(TAG, "Worker Prepared")
    }

    private fun calculateDateDiffSeconds(currentTime: DateTime, timeToExecute: DateTime): Long{

        Log.d(TAG, "Current $currentTime, End $timeToExecute")
        Log.d(TAG, "Diff in Seconds " + Seconds.secondsBetween(currentTime, timeToExecute).seconds)

        return Math.abs(Seconds.secondsBetween(currentTime, timeToExecute).seconds).toLong()
    }

    private fun getNearestDayOfWeek(onCallItem: OnCallItem): DateTime{
        val todayStartTime = dateFormatter(onCallItem.displayStartTime)
        
        var nextInterval = getDayOfWeek(onCallItem.day) - DateTime.now().dayOfWeek().get()
        if(nextInterval < 0 || (nextInterval == 0 && DateTime.now().isAfter(todayStartTime))){
            nextInterval += 7
        }
        return todayStartTime.plusDays(nextInterval)
    }

    private fun prepareNearestActiveItem(onCallItems: List<OnCallItem>){

        var tempDiff: Long
        var lowestDiff = Long.MAX_VALUE

        onCallItems.forEach{

            if(!it.isActive) return@forEach

            tempDiff = calculateDateDiffSeconds(DateTime.now(), getNearestDayOfWeek(it))
            if(tempDiff < lowestDiff) lowestDiff = tempDiff
        }

        prepareWorker(lowestDiff)
    }

    private fun getDayOfWeek(dayOfWeek: String?): Int{

        if(dayOfWeek == null) return DateTime.now().dayOfWeek

        try{
            return when(dayOfWeek){
                "Sunday" -> DateTimeConstants.SUNDAY
                "Monday" -> DateTimeConstants.MONDAY
                "Tuesday" -> DateTimeConstants.TUESDAY
                "Wednesday" -> DateTimeConstants.WEDNESDAY
                "Thursday" -> DateTimeConstants.THURSDAY
                "Friday" -> DateTimeConstants.FRIDAY
                "Saturday" -> DateTimeConstants.SATURDAY
                else -> throw IllegalStateException("Day of week doesn't match formatting. " +
                        "Returning todayStartTime's date day of week: " + DateTime.now().dayOfWeek())
            }
        }catch (e: IllegalStateException){
            e.printStackTrace()
            return DateTime.now().dayOfWeek
        }
    }
    
    private fun dateFormatter(displayTime: String): DateTime{
        val time = SimpleDateFormat("h:mm a", Locale.getDefault()).parse(displayTime)
        return DateTime(time).toLocalTime().toDateTimeToday()
    }
}
