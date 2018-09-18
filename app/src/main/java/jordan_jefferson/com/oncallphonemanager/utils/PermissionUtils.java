package jordan_jefferson.com.oncallphonemanager.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
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

    public void alertNotificationAccessNeeded(Context context){
        AlertDialog.Builder alb = new AlertDialog.Builder(context);

        alb.setIcon(android.R.drawable.ic_dialog_alert);
        alb.setTitle("Notification Access");
        alb.setMessage("This feature requires Do no Disturb access to be granted. " +
                "Would you like to modify Do not Disturb settings for this app?");

        alb.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestNotificationAccess();
            }
        });

        alb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alb.create();
        alb.show();
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
