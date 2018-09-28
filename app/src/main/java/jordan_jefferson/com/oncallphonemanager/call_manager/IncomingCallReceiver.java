package jordan_jefferson.com.oncallphonemanager.call_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

import jordan_jefferson.com.oncallphonemanager.data.Contact;
import jordan_jefferson.com.oncallphonemanager.data.ContactRepository;

/*
A Broadcast Receiver that listens for call state changes i.e. incoming, idle, and answered.
 */
public class IncomingCallReceiver extends BroadcastReceiver {

    private static final String TAG = "Incoming Call Receiver";
    private AudioManager am;
    private Ringtone ringtone;
    private int previousRingerMode;
    private int previousRingerVolume;

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {

            ContactRepository contactRepository = new ContactRepository(context);
            List<Contact> contacts = contactRepository.getContactsBlocking();
            this.am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), uri);

            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = "";
            int state = 0;

            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
                number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER, "");
                Log.d(TAG, number);
            }


            onCallStateChanged(state, number, contacts);
        }

        Log.d(TAG, "Enabled");
    }

    private void onCallStateChanged(int state, String incomingNumber, List<Contact> contacts) {

        if (lastState == state) {
            return;
        }

        try {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if(!incomingNumber.isEmpty()){
                        boolean isMatch = phoneNumberAnalyzer(incomingNumber, contacts);
                        checkAndEnableRinger(isMatch);
                        lastState = TelephonyManager.CALL_STATE_RINGING;
                        Log.d(TAG, "Ringing");
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (ringtone.isPlaying()) ringtone.stop();
                    am.setStreamVolume(AudioManager.STREAM_RING, previousRingerVolume, 0);
                    am.setRingerMode(previousRingerMode);
                    lastState = TelephonyManager.CALL_STATE_OFFHOOK;
                    Log.d(TAG, "Off Hooke");
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    if(ringtone.isPlaying()) ringtone.stop();
                    am.setStreamVolume(AudioManager.STREAM_RING, previousRingerVolume, 0);
                    am.setRingerMode(previousRingerMode);
                    lastState = TelephonyManager.CALL_STATE_IDLE;
                    Log.d(TAG, "Idle");
                    break;
            }
        } catch (
                SecurityException e) {
            Log.e("ERROR", "Permissions are denied");
            e.printStackTrace();
        }
    }

    private boolean phoneNumberAnalyzer(String incomingNumber, List<Contact> contacts) {

        String phoneNumber = incomingNumber;
        int size = incomingNumber.length();

        switch (size) {
            case 11:
                phoneNumber = incomingNumber.substring(1);
                break;
            case 12:
                phoneNumber = incomingNumber.substring(2);
                break;
        }

        if (contacts != null) {
            for (int i = 0; i < contacts.size(); i++) {
                if (phoneNumber.matches(contacts.get(i).get_contactRegexNumber())) {
                    Log.d(TAG, "Matched Number");
                    return true;
                }
            }
        }

        return false;
    }

    private void checkAndEnableRinger(boolean isMatch) {
        if (isMatch && am.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {

            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_RING);
            previousRingerMode = am.getRingerMode();
            previousRingerVolume = am.getStreamVolume(AudioManager.STREAM_RING);

            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            am.setStreamVolume(AudioManager.STREAM_RING, maxVolume - 2, 0);

            if (!ringtone.isPlaying()) ringtone.play();

            Log.d(TAG, "Enabling Ringer");
        }
    }
}
