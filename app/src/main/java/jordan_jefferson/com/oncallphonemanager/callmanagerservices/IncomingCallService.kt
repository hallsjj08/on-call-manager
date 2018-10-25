package jordan_jefferson.com.oncallphonemanager.callmanagerservices

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.graphics.Color
import android.graphics.drawable.Icon
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import jordan_jefferson.com.oncallphonemanager.R
import jordan_jefferson.com.oncallphonemanager.data.ContactRepository
import jordan_jefferson.com.oncallphonemanager.utils.PermissionUtils

class IncomingCallService : Service(), ITelephony {

    private val TAG = "Incoming Call Service"
    private val notificationChanelId = "call_service"
    private lateinit var am: AudioManager
    private var ringtone: Ringtone? = null
    private var previousRingerMode: Int = 0
    private var previousRingerVolume: Int = 0

    lateinit var telephonyManager: TelephonyManager

    private lateinit var contactRepository: ContactRepository
    private lateinit var regexNumbers: List<String>
    private lateinit var settingsContentObserver: SettingsContentObserver

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    override fun onCreate() {
        super.onCreate()

        startForegroundService()

        Log.d(TAG, "Created")

        telephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(CustomPhoneStateListener(this), PhoneStateListener.LISTEN_CALL_STATE)


        am = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        contactRepository = ContactRepository(this)
        regexNumbers = contactRepository.regexNumbersBlocking
    }

    override fun incomingCall(phoneStateListener: CustomPhoneStateListener, incomingNumber: String?) {
        Log.d(TAG, "Incoming Call Called")
        if(incomingNumber == null || incomingNumber.isEmpty()) return
        val isMatch = phoneNumberAnalyzer(incomingNumber, regexNumbers)
        if(isMatch) {
            checkAndEnableRinger(applicationContext)
        }else{
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
            stopSelf()
        }
    }

    private fun startForegroundService(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "On Call Phone Manager"
            val descriptionText = "Processing incoming call."
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(notificationChanelId, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, notificationChanelId)
        val notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("On Call")
                .setContentText("You have an incoming call")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setAutoCancel(true)
                .build()

        startForeground(1337, notification)

    }

    override fun destroyService(phoneStateListener: CustomPhoneStateListener) {
        Log.d(TAG, "Destroy Service Called")
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        disableRinger()
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Start Command: ${Thread.currentThread().name}")
        return START_STICKY
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    override fun onDestroy() {
        Log.d(TAG, "Destroyed")
        super.onDestroy()
        stopForeground(true)
    }

    private fun phoneNumberAnalyzer(incomingNumber: String, regexNumbers: List<String>?): Boolean {

        var phoneNumber = incomingNumber

        when (incomingNumber.length) {
            11 -> phoneNumber = incomingNumber.substring(1)
            12 -> phoneNumber = incomingNumber.substring(2)
        }

        if (regexNumbers != null && !regexNumbers.isEmpty()) {
            for (i in regexNumbers.indices) {
                if (phoneNumber.matches(regexNumbers[i].toRegex())) {
                    Log.d(TAG, "Matched Number")
                    return true
                }
            }
        }

        return false
    }

    private fun checkAndEnableRinger(context: Context) {
        if (am.ringerMode != AudioManager.RINGER_MODE_NORMAL) {

            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)!!
            if(ringtone == null) ringtone = RingtoneManager.getRingtone(this, uri)!!

            val maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_RING)
            previousRingerMode = am.ringerMode
            previousRingerVolume = am.getStreamVolume(AudioManager.STREAM_RING)

            am.ringerMode = AudioManager.RINGER_MODE_NORMAL
            am.setStreamVolume(AudioManager.STREAM_RING, maxVolume - 2, AudioManager.FLAG_PLAY_SOUND)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PermissionUtils.setNotificationFilter(context, NotificationManager.INTERRUPTION_FILTER_ALL)
            }

            if (!ringtone!!.isPlaying) {
                ringtone?.play()
                settingsContentObserver = SettingsContentObserver(am, this, Handler())
                this.contentResolver.registerContentObserver(
                        android.provider.Settings.System.CONTENT_URI,
                        true, settingsContentObserver)
            }

            Log.d(TAG, "Enabling Ringer")
        }
    }

    private fun disableRinger(){
        if (ringtone != null && ringtone!!.isPlaying) {
            ringtone?.stop()
            am.setStreamVolume(AudioManager.STREAM_RING, previousRingerVolume, 0)
            am.ringerMode = previousRingerMode
            this.contentResolver.unregisterContentObserver(settingsContentObserver)
        }
    }

    private class SettingsContentObserver(val am: AudioManager, val incomingCallService: IncomingCallService,
                                          handler: Handler) : ContentObserver(handler) {

        var startVolume = am.getStreamVolume(AudioManager.STREAM_RING)

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)

            val changedVolume = am.getStreamVolume(AudioManager.STREAM_RING)
            val delta = startVolume - changedVolume

            if(delta > 0){
                incomingCallService.disableRinger()
            }else{
                startVolume = changedVolume
            }
        }
    }
}