package jordan_jefferson.com.oncallphonemanager.call_manager

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import jordan_jefferson.com.oncallphonemanager.data.OnCallItem
import jordan_jefferson.com.oncallphonemanager.data.OnCallItemRepository
import java.util.*

class CallManagerService(val context: Context, params: WorkerParameters) :
        Worker(context, params) {

    private val onCallItemRepository = OnCallItemRepository(context)
    private val onCallItems = onCallItemRepository.allActiveOnCallItems

    /**
     * Override this method to do your actual background processing.
     */
    override fun doWork(): Result {

        val isActive = checkActiveOnCallItems(onCallItems)

        val flag: Int
        flag = if (isActive) {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        } else{
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }

        val componentName = ComponentName(context, IncomingCallReceiver::class.java)

        context.packageManager.setComponentEnabledSetting(componentName, flag, PackageManager.DONT_KILL_APP)



        return Result.SUCCESS
    }

    val TAG = "CALL MANAGER SERVICE"

    fun checkActiveOnCallItems(onCallItems: List<OnCallItem>?): Boolean{

        if(onCallItems == null) return false

        val calendar = GregorianCalendar.getInstance()
        val currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        Log.d(TAG, currentDay)

        onCallItems.forEach{
            if(it.day == currentDay || it.day == null){
                if(currentHour >= it.startTimeHour && currentMinute >= it.startTimeMinute){
                    if(it.endTimeHour < it.startTimeHour){
                        Log.d(TAG, "Item Active ${it.label} is active " + true)
                        return true
                    }else if(currentHour <= it.endTimeHour && currentMinute <= it.endTimeMinute){
                        Log.d(TAG, "Item Active ${it.label} is active " + true)
                        return true
                    }
                }
            }
        }

        Log.d(TAG, "Item Active false")
        return false
    }

    fun prepareFutureOnCallTime(onCallItem: OnCallItem, isCurrentItemActive: Boolean){

    }
}