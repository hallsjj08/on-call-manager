package jordan_jefferson.com.oncallphonemanager.callmanagerservices

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

class CustomPhoneStateListener(private val telephonyInterface: ITelephony): PhoneStateListener(){

    private var NOT_RINGING = -200
    private var lastState = NOT_RINGING

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        super.onCallStateChanged(state, phoneNumber)

        if(lastState == state) return

        when(state){
            TelephonyManager.CALL_STATE_RINGING -> {
                lastState = TelephonyManager.CALL_STATE_RINGING
                telephonyInterface.incomingCall(this, phoneNumber)
            }
            else -> {
                lastState = NOT_RINGING
                telephonyInterface.destroyService(this)
            }
        }

    }
}