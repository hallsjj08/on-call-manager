package jordan_jefferson.com.oncallphonemanager.call_manager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.util.Log;

import java.util.List;

import jordan_jefferson.com.oncallphonemanager.call_manager.RingtonePlayer;
import jordan_jefferson.com.oncallphonemanager.data.Contact;

/*
A Broadcast Receiver that listens for call state changes i.e. incoming, idle, and answered.
 */
public class ServiceReceiver extends BroadcastReceiver {

    private AudioManager am;
    private boolean isMatch = false;
    private int previousRingerMode;
    private int previousRingerVolume;
    private Context mContext;
    private List<Contact> mContacts;

    public ServiceReceiver(Context context) {
        mContext = context;
        this.am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public PhoneStateListener phoneStateListener = new PhoneStateListener() {
        /*
        Parameters:
            int state: 0 = idle, 1 = incoming, 2 = call answered
            String incomingNumber: displays in states 1 and 2.
         */
        @Override
        public synchronized void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

                    try {

                        if (state == 1) {

                            isMatch = phoneNumberAnalyzer(incomingNumber);

                            if (isMatch && am.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {

                                int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_RING);
                                previousRingerMode = am.getRingerMode();
                                previousRingerVolume = am.getStreamVolume(AudioManager.STREAM_RING);

                                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                am.setStreamVolume(AudioManager.STREAM_RING, maxVolume - 2, 0);

                                RingtonePlayer.getRingtonePlayer(mContext).play();
                            }
                        }else {

                            if(isMatch){
                                RingtonePlayer.getRingtonePlayer(mContext).stop();
                                am.setStreamVolume(AudioManager.STREAM_RING, previousRingerVolume,0);
                                am.setRingerMode(previousRingerMode);
                            }

                            isMatch = false;
                        }
                    }catch (SecurityException e) {
                            Log.e("ERROR", "Permissions are denied");
                            e.printStackTrace();
                        }

                        previousRingerVolume = 0;
        }
    };

    public PhoneStateListener getPhoneStateListener() {
        return phoneStateListener;
    }

    @SuppressLint("Unused")
    public void setPhoneStateListener(PhoneStateListener phoneStateListener) {
        this.phoneStateListener = phoneStateListener;
    }

    public void setContacts(List<Contact> contacts){
        mContacts = contacts;
    }

    private boolean phoneNumberAnalyzer(String incomingNumber){

        String phoneNumber = incomingNumber;
        int size = incomingNumber.length();

        switch (size){
            case 11:
                phoneNumber = incomingNumber.substring(1);
                break;
            case 12:
                phoneNumber = incomingNumber.substring(2);
                break;
        }

        if(mContacts != null){
            for(int i = 0; i < mContacts.size(); i++){
                if(phoneNumber.matches(mContacts.get(i).get_contactRegexNumber())){
                    return true;
                }
            }
        }

        return false;
    }

}
