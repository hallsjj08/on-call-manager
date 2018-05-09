package jordan_jefferson.com.oncallphonemanager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class CallManagerActivity extends AppCompatActivity {

    private static FloatingActionButton fab;
    private boolean isPermissionGranted;
    private AlertDialog.Builder alb;
    private AlertDialog al;
    private Fragment mFragment;

    public final String DEBUG_TAG = "MY_ACTIVITY_INFO";
    public final String FRAGMENT_TAG = "MY_FRAGMENT_TAG";
    private final String[] myPermissions = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_manager);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null){
            mFragment = new ContactListFragment();
            mFragment.setArguments(getIntent().getExtras());
        }else{
            mFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mFragment, FRAGMENT_TAG).commit();

        Log.w(DEBUG_TAG, "Created");

        final ReceiverFactory receiverFactory = new ReceiverFactory(getApplicationContext());
        fab = findViewById(R.id.floatingActionButton);
        alb = new AlertDialog.Builder(this);

        final Switch bEnabled = findViewById(R.id.bEnableReceiverObserver);
        bEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                if(isOn){
                    for (int i = 0; i < myPermissions.length; i++) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), myPermissions[i])
                                == PackageManager.PERMISSION_GRANTED) {
                            Log.w(DEBUG_TAG, "Permission Granted");
                            isPermissionGranted = true;
                        } else {
                            isPermissionGranted = false;
                            break;
                        }
                    }

                        if(isPermissionGranted) {
                            if(ContactsList.getInstance(getApplicationContext()).getContactsList().isEmpty()){
                                Toast.makeText(CallManagerActivity.this, "Please add a contact to enable this feature.",
                                        Toast.LENGTH_LONG).show();
                                bEnabled.setChecked(false);
                            } else{
                                receiverFactory.registerReceivers();
                            }
                        }else {
                                Log.w(DEBUG_TAG, "Permission Denied");

                                al = alb.create();
                                al.show();
                            }
                }else{
                    if(receiverFactory.isReceiverRegistered()){
                        receiverFactory.unregisterReceivers();
                    }
                }
            }
        });

        alb.setMessage("The following feature needs \"Read Phone State\" permission enabled. " +
                "Would you like to edit permissions?");
        alb.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog the dialog that received the click
             * @param which  the button that was clicked (ex.
             *               {@link DialogInterface#BUTTON_POSITIVE}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    Log.w(DEBUG_TAG, "Permission Denied");

                    ActivityCompat.requestPermissions(CallManagerActivity.this,
                            myPermissions, 1);
                    Log.w(DEBUG_TAG, "Requesting Permission");
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bEnabled.setChecked(false);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment = new ContactInfoFragment();

                setFabVisibility(false);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mFragment, FRAGMENT_TAG).addToBackStack("listFragment").commit();

            }
        });

    }

    public static void setFabVisibility(boolean visible){
        if(visible){
            fab.setVisibility(FloatingActionButton.VISIBLE);
        }else{
            fab.setVisibility(FloatingActionButton.INVISIBLE);
        }
    }

    /**
     * Take care of popping the mFragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        fab.setVisibility(fab.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    this.finishAndRemoveTask();
                }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
                case R.id.phone_read_permissions:
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    return true;
                case R.id.DnD_Permission:
                    NotificationSettings.getNotificationSettings(getApplicationContext());
                    NotificationSettings.getNotificationSettings(getApplicationContext()).requestNotificationAccess();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {

        super.onStart();

        Log.w(DEBUG_TAG, "Started");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.w(DEBUG_TAG, "Resumed");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(DEBUG_TAG, "Paused");

        //ContactListFragment.contactListUpdated();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(DEBUG_TAG, "Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.w(DEBUG_TAG, "Destroyed");

//        try{
////            receiverFactory.unregisterReceivers();
////        }catch(Exception e){
////            e.printStackTrace();
////        }


        //finishAffinity();
    }
}
