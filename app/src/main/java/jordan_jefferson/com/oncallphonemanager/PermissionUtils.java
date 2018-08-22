package jordan_jefferson.com.oncallphonemanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    private Context mContext;
    private Activity mActivity;
    private NotificationManager mNotificationManager;

    private String[] myPermissions = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS};

    PermissionUtils(Context context, Activity activity){

        this.mContext = context;
        this.mActivity = activity;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public boolean permissionsGranted(){

        for (String myPermission : myPermissions) {
            if (ContextCompat.checkSelfPermission(mContext, myPermission)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                return false;
            }
        }
        return true;
    }

    public void requestPermissions(){
        ActivityCompat.requestPermissions(mActivity,
                myPermissions, 1);
    }

    @SuppressLint("Unused")
    public boolean isNotificationAccessGranted(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                mNotificationManager.isNotificationPolicyAccessGranted()){

            return true;

        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        return false;

    }

    public void requestNotificationAccess(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        }
    }

    @SuppressLint("Unused")
    public void setNotificationFilter(int notificationFilter){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mNotificationManager.setInterruptionFilter(notificationFilter);
        }
    }

    @SuppressLint("Unused")
    public int getNotificationFilter(){
        int currentInterruptionFilter = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mNotificationManager.getCurrentInterruptionFilter();
        }

        return currentInterruptionFilter;
    }

}
