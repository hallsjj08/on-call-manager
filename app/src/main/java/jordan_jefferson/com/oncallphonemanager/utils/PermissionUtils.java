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
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import jordan_jefferson.com.oncallphonemanager.permissions.PermissionsNeededActivity;

public class PermissionUtils {

    private static final String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.FOREGROUND_SERVICE};

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

    public static void getPermissions(Fragment fragment){
        if(!permissionsGranted(fragment.getContext())){
           fragment.requestPermissions(PERMISSIONS, 1);
        }
    }

    public static boolean isNotificationAccessGranted(Context context){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                notificationManager.isNotificationPolicyAccessGranted()){

            return true;

        }else return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;

    }

    public static void requestNotificationAccess(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isNotificationAccessGranted(context)){

            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            navigateToSettigsPopup(context, intent, "DnD Access", "Do not Disturb Access is needed " +
                    "to modify audio settings even when Do not Disturb is turned on. " +
                    "Would you like to edit these permissions in Settings?", "Settings");

        }
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

    public static boolean isIgnoringBatteryOptimization(Context context){
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        String packageName = context.getPackageName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return powerManager.isIgnoringBatteryOptimizations(packageName);
        }

        return true;
    }

    public static void requestIgnoreBatteryOptomization(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isIgnoringBatteryOptimization(context)) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String packageName = context.getPackageName();

            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));

            context.startActivity(intent);
        }
    }

    public static void navigateToSettigsPopup(Context context, Intent intent, String title, 
                                              String message, String positiveButton){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(positiveButton, ((dialog, which) -> {
            if(intent != null){
                context.startActivity(intent);
            }else{
                requestIgnoreBatteryOptomization(context);
            }
            dialog.dismiss();
        }));

        builder.create().show();

    }

    public static void checkAllPermissions(View view){
        Context context = view.getContext();

        if(!permissionsGranted(context) || !isNotificationAccessGranted(context)
                || !isIgnoringBatteryOptimization(context)){
            showInsistantSnackBar(view);
        }
    }

    private static void showInsistantSnackBar(View view){
        BaseTransientBottomBar.Behavior behavior = new BaseTransientBottomBar.Behavior();
        behavior.setListener(null);

        Snackbar.make(view, "Warning! Permissions Needed!", Snackbar.LENGTH_INDEFINITE)
                .setBehavior(behavior)
                .setAction("More", v -> {
                    view.getContext().startActivity(new Intent(view.getContext(), PermissionsNeededActivity.class));
                })
                .show();
    }

}
