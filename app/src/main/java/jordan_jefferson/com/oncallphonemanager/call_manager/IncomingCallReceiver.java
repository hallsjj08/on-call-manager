package jordan_jefferson.com.oncallphonemanager.call_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
        if(intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
            incomingCallService.setAction(intent.getAction());
            incomingCallService.putExtra("PHONE STATE", intent.getExtras());
        }

        context.startService(incomingCallService);

        Log.d(TAG, Thread.currentThread().getName());
        Log.d(TAG, "Enabled");
    }
}
