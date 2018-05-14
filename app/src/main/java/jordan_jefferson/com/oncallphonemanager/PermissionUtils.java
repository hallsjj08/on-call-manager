package jordan_jefferson.com.oncallphonemanager;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class PermissionUtils {

    private Context mContext;
    private Activity mActivity;
    private NotificationManager mNotificationManager;

    private String[] myPermissions = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS};

    public PermissionUtils(Context context, Activity activity){

        this.mContext = context;
        this.mActivity = activity;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public boolean checkPermissions(){

        for (int i = 0; i < myPermissions.length; i++) {
            if (ContextCompat.checkSelfPermission(mContext, myPermissions[i])
                    == PackageManager.PERMISSION_GRANTED) {
                Log.w(myPermissions[i], "Permission Granted");
            } else {
                Log.w(myPermissions[i], "Permission Denied");
                return false;
            }
        }
        return true;
    }

    public void requestPermissions(){
        ActivityCompat.requestPermissions(mActivity,
                myPermissions, 1);
        Log.w("PERMISSION_UTILS", "Requesting Permission");
    }

    public boolean isNotificationAccessGranted(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                mNotificationManager.isNotificationPolicyAccessGranted()){

            return true;

        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            //TODO: add checks for overriding DND settings here if users are operating below Marshmallow.

            return true;
        }

        return false;
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
