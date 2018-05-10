package jordan_jefferson.com.oncallphonemanager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

/*
A Singleton that determines whether or not the user has granted Do Not Disturb Access and can set
DnD priorities. The class needs to be edited to remove the Singleton Pattern. Possibly combine with
Permissions checks and make permissions class.
 */
public class NotificationSettings {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private static NotificationSettings sNotificationSettings;

    private NotificationSettings(Context context){

        this.mContext = context;

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public static NotificationSettings getNotificationSettings(Context context){
        if(sNotificationSettings == null){
            sNotificationSettings = new NotificationSettings(context);
        }

        return sNotificationSettings;
    }

    public boolean isAccessGranted(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !mNotificationManager.isNotificationPolicyAccessGranted()){

            return false;

        }

        return true;
    }

    public void requestNotificationAccess(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !mNotificationManager.isNotificationPolicyAccessGranted()){

            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            mContext.startActivity(intent);

        }
    }

    public void setNotificationFilter(int notificationFilter){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mNotificationManager.setInterruptionFilter(notificationFilter);
        }
    }

    public int getNotificationFilter(){
        int currentInterruptionFilter = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mNotificationManager.getCurrentInterruptionFilter();
        }

        return currentInterruptionFilter;
    }

}
