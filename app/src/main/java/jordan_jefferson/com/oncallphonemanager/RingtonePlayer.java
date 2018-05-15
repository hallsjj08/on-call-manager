package jordan_jefferson.com.oncallphonemanager;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/*
A Singleton class that creates a single instance of a ringtone player. One instance of this
ringtone player ensures that multiple ringtones can't play at the same time.
 */
public class RingtonePlayer {

    private static RingtonePlayer sRingtonePlayer;
    private Ringtone mRingtone;

    private RingtonePlayer(Context context){

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mRingtone = RingtoneManager.getRingtone(context.getApplicationContext(), uri);

    }

    public static RingtonePlayer getRingtonePlayer(Context context){
        if (sRingtonePlayer == null){
            sRingtonePlayer = new RingtonePlayer(context);
        }

        return sRingtonePlayer;
    }

    public void stop(){
        if(mRingtone.isPlaying()){
            mRingtone.stop();
        }
    }

    public void play(){
        if(!mRingtone.isPlaying()){
            mRingtone.play();
        }
    }
}
