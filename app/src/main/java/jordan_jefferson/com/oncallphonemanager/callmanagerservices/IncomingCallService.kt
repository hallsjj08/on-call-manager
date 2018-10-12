package jordan_jefferson.com.oncallphonemanager.callmanagerservices

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.telephony.TelephonyManager
import android.util.Log
import jordan_jefferson.com.oncallphonemanager.data.ContactRepository
import jordan_jefferson.com.oncallphonemanager.utils.PermissionUtils
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

class IncomingCallService : Service() {

    val TAG = "Incoming Call Service"
    private lateinit var am: AudioManager
    private var ringtone: Ringtone? = null
    private var previousRingerMode: Int = 0
    private var previousRingerVolume: Int = 0

    private lateinit var contactRepository: ContactRepository
    private lateinit var regexNumbers: List<String>
    private lateinit var settingsContentObserver: SettingsContentObserver

    private var lastState = TelephonyManager.CALL_STATE_IDLE

    private var timer = Timer()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    override fun onCreate() {
        super.onCreate()

        am = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        contactRepository = ContactRepository(this)
        regexNumbers = contactRepository.regexNumbersBlocking

        timer.schedule(timerTask { stopSelf() },
                TimeUnit.MILLISECONDS.convert(3L, TimeUnit.MINUTES))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(TAG, "Start Command: ${Thread.currentThread().name}")

        if (intent != null && intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {

            val bundle = intent.extras!!.getBundle("PHONE STATE")

            val stateStr = bundle!!.getString(TelephonyManager.EXTRA_STATE)
            var number = ""
            var state = 0

            when (stateStr) {
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    state = TelephonyManager.CALL_STATE_IDLE
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> state = TelephonyManager.CALL_STATE_OFFHOOK
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    state = TelephonyManager.CALL_STATE_RINGING
                    number = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER, "")
                    Log.d(TAG, number)
                }
            }


            onCallStateChanged(state, number, regexNumbers, this)
        }

        return START_NOT_STICKY
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    override fun onDestroy() {
        Log.d(TAG, "Destroyed")
        disableRinger()
        timer.purge()
        super.onDestroy()
    }

    private fun onCallStateChanged(state: Int, incomingNumber: String, regexNumbers: List<String>, context: Context) {

        if (lastState == state) {
            return
        }

        resetTimer()

        try {

            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> if (!incomingNumber.isEmpty()) {
                    Log.d(TAG, "Ringing")
                    val isMatch = phoneNumberAnalyzer(incomingNumber, regexNumbers)
                    checkAndEnableRinger(isMatch, context)
                    lastState = TelephonyManager.CALL_STATE_RINGING
                }

                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    Log.d(TAG, "Off Hook")
                    disableRinger()
                    timer.cancel()
                    lastState = TelephonyManager.CALL_STATE_OFFHOOK
                }

                TelephonyManager.CALL_STATE_IDLE -> {
                    Log.d(TAG, "Idle")
                    disableRinger()
                    lastState = TelephonyManager.CALL_STATE_IDLE
                }
            }
        } catch (e: SecurityException) {
            Log.e("ERROR", "Permissions are denied")
            e.printStackTrace()
        }

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

    private fun checkAndEnableRinger(isMatch: Boolean, context: Context) {
        if (isMatch && am.ringerMode != AudioManager.RINGER_MODE_NORMAL) {

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

    private fun resetTimer(){
        timer.cancel()
        timer.purge()
        timer = Timer()
        timer.schedule(timerTask { stopSelf() }, TimeUnit.MILLISECONDS.convert(3L, TimeUnit.MINUTES))
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