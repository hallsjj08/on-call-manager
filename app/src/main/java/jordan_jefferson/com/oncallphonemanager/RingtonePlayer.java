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
    private Context mContext;
    private Ringtone mRingtone;
    private Uri mUri;

    private RingtonePlayer(Context context){

        this.mContext = context;

        mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mRingtone = RingtoneManager.getRingtone(mContext.getApplicationContext(), mUri);

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
