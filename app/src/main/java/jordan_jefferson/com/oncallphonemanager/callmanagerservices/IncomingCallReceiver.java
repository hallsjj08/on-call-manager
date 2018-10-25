package jordan_jefferson.com.oncallphonemanager.callmanagerservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

/*
A Broadcast Receiver that listens for call state changes i.e. incoming, idle, and answered.
 */
public class IncomingCallReceiver extends BroadcastReceiver {

    private static final String TAG = "Incoming Call Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent incomingCallService = new Intent(context, IncomingCallService.class);
        if(intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(incomingCallService);
                    Log.d(TAG, "Foreground Start");
                }else{
                    context.startService(incomingCallService);
                    Log.d(TAG, "Service Start");
                }
            }
        }

        Log.d(TAG, Thread.currentThread().getName());
        Log.d(TAG, "Enabled");
    }
}
