package jordan_jefferson.com.oncallphonemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/*
A Factory class that creates instances of the Broadcast Receivers and provides methods to register
and unregister them from the app.
 */
public class ReceiverFactory {

    private ServiceReceiver myCallReceiver;
    private TelephonyManager telephony;
    private IntentFilter intentFilter;
    private Context mContext;

    public boolean isReceiverRegistered() {
        return isReceiverRegistered;
    }

    private boolean isReceiverRegistered = false;

    ReceiverFactory(Context context){
        this.mContext = context;
        this.myCallReceiver = new ServiceReceiver(mContext);
        this.telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        this.intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
    }

    public void registerReceivers(){
        mContext.registerReceiver(myCallReceiver, intentFilter);

        telephony.listen(myCallReceiver.getPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        isReceiverRegistered = true;
    }

    public void unregisterReceivers(){

        telephony.listen(myCallReceiver.getPhoneStateListener(), PhoneStateListener.LISTEN_NONE);
        mContext.unregisterReceiver(myCallReceiver);

        isReceiverRegistered = false;
    }

    @SuppressLint("Unused")
    public ServiceReceiver getMyCallReceiver() {
        return myCallReceiver;
    }

    @SuppressLint("Unused")
    public void setMyCallReceiver(ServiceReceiver myCallReceiver) {
        this.myCallReceiver = myCallReceiver;
    }

    @SuppressLint("Unused")
    public TelephonyManager getTelephony() {
        return telephony;
    }

    @SuppressLint("Unused")
    public void setTelephony(TelephonyManager telephony) {
        this.telephony = telephony;
    }

    @SuppressLint("Unused")
    public Context getContext() {
        return mContext;
    }

    @SuppressLint("Unused")
    public void setContext(Context context) {
        mContext = context;
    }
}
