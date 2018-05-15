package jordan_jefferson.com.oncallphonemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/*
CallManagerActivity is the launcher activity and the only activity in the app.
 */

public class CallManagerActivity extends AppCompatActivity {

    private static FloatingActionButton fab;
    private SharedPreferences mSharedPreferences;
    private Fragment mFragment;
    private PermissionUtils mPermissionUtils;
    private ReceiverFactory receiverFactory;

    public final String PERMISSIONS_REQUESTED_AFTER_ONBOARDING = "Onboarding Permissions";
    public final String DEBUG_TAG = "MY_ACTIVITY_INFO";
    public final String FRAGMENT_TAG = "MY_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_manager);

        //Sets up the actionbar/toolbar for the app.
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Creates all objects that involve Broadcast Receivers.
        receiverFactory = new ReceiverFactory(getApplicationContext());
        mPermissionUtils = new PermissionUtils(getApplicationContext(), this);

        mSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our OnboardingFragment
        if (!mSharedPreferences.getBoolean(
                NewUserOnBoardingActivity.COMPLETED_ONBOARDING_PREF_NAME, false)) {
            startActivity(new Intent(this, NewUserOnBoardingActivity.class));
        }

        //Handles rotation changes
        if(savedInstanceState == null){
            mFragment = new ContactListFragment();
            mFragment.setArguments(getIntent().getExtras());
        }else{
            mFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mFragment, FRAGMENT_TAG).commit();

        Log.w(DEBUG_TAG, "Created");

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment = new ContactInfoFragment();

                setFabVisibility(false);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mFragment, FRAGMENT_TAG).addToBackStack("listFragment").commit();

            }
        });

    }

    /*
    This static class sets the visibility of the floating action button. Made static so that this
    instance can be accessed from other classes.
     */
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

    /*
    An Android method that is called when the static method .requestPermissions(Activity activity,
    String[] permissions, int requestCode) is called.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    receiverFactory.registerReceivers();
                    invalidateOptionsMenu();
                    return;
                } else {
                    return;
                }
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(receiverFactory.isReceiverRegistered()){
            menu.findItem(R.id.bEnableReceiverObserver).setIcon(R.drawable.ic_phone_in_talk_secondary_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //TODO: Create a fragment that handles menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call_manager, menu);
        return true;
    }

    /*
    Handles which settings item was clicked and performs actions accordingly.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.bEnableReceiverObserver:
                receiverEnabler(item);
                return true;

            //Allows the user to manage Do Not Disturb access.
                case R.id.DnD_Permission:
                    mPermissionUtils.requestNotificationAccess();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    //Method to handle enabling/disabling the receivers when the menu item is clicked.
    public void receiverEnabler(MenuItem item){

        if(mPermissionUtils.permissionsGranted() &&
                !ContactsList.getInstance(getApplicationContext()).getContactsList().isEmpty()
                && !receiverFactory.isReceiverRegistered()){
            receiverFactory.registerReceivers();
            item.setIcon(R.drawable.ic_phone_in_talk_secondary_24dp);
        }else if(ContactsList.getInstance(getApplicationContext()).getContactsList().isEmpty()){
            Toast.makeText(CallManagerActivity.this, "Please add a contact to get started.",
                    Toast.LENGTH_LONG).show();
        }else if(!mPermissionUtils.permissionsGranted()){
            mPermissionUtils.requestPermissions();
        }else{
            if(receiverFactory.isReceiverRegistered()){
                receiverFactory.unregisterReceivers();
                item.setIcon(R.drawable.ic_phone_missed_red_24dp);
            }
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
        
        if(mSharedPreferences.getBoolean(
                NewUserOnBoardingActivity.COMPLETED_ONBOARDING_PREF_NAME, false) &&
                !mSharedPreferences.getBoolean(PERMISSIONS_REQUESTED_AFTER_ONBOARDING, false)){
            mPermissionUtils.requestPermissions();
            mPermissionUtils.requestNotificationAccess();

            SharedPreferences.Editor sharedPreferencesEditor =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            sharedPreferencesEditor.putBoolean(
                    PERMISSIONS_REQUESTED_AFTER_ONBOARDING, true);
            sharedPreferencesEditor.apply();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(DEBUG_TAG, "Paused");
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

        if(receiverFactory.isReceiverRegistered()){
            receiverFactory.unregisterReceivers();
        }
    }
}
