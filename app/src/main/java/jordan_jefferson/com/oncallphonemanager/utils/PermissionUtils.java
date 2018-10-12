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
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    private static final String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,};

    public static boolean permissionsGranted(Context context){

        for (String myPermission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, myPermission)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(Activity activity){
        ActivityCompat.requestPermissions(activity,
                PERMISSIONS, 1);
    }

    public static boolean isNotificationAccessGranted(Context context){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                notificationManager.isNotificationPolicyAccessGranted()){

            return true;

        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        return false;

    }

    public static void requestNotificationAccess(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }

    public static void alertNotificationAccessNeeded(Context context){
        AlertDialog.Builder alb = new AlertDialog.Builder(context);

        alb.setIcon(android.R.drawable.ic_dialog_alert);
        alb.setTitle("Notification Access");
        alb.setMessage("This feature requires Do no Disturb access to be granted. " +
                "Would you like to modify Do not Disturb settings for this app?");

        alb.setPositiveButton("Settings", (dialog, which) -> {
//                requestNotificationAccess();
        });

        alb.setNegativeButton("Cancel", (dialog, which) -> {

        });

        alb.create();
        alb.show();
    }

    public static void setNotificationFilter(Context context, int notificationFilter){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            notificationManager.setInterruptionFilter(notificationFilter);
        }
    }

    public static int getNotificationFilter(Context context){
        int currentInterruptionFilter = 0;


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            notificationManager.getCurrentInterruptionFilter();
        }

        return currentInterruptionFilter;
    }

    private static boolean isIgnoringBatteryOptimization(Context context){
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        String packageName = context.getPackageName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return powerManager.isIgnoringBatteryOptimizations(packageName);
        }

        return true;
    }

    public static void checkOrRequestWhiteListing(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String packageName = context.getPackageName();

            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));

            context.startActivity(intent);
        }
    }

}
