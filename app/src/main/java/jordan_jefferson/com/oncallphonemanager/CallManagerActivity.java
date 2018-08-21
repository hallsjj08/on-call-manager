package jordan_jefferson.com.oncallphonemanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

/*
CallManagerActivity is the launcher activity and the only activity in the app.
 */

public class CallManagerActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    private PermissionUtils mPermissionUtils;
    private ReceiverFactory receiverFactory;
    public FragmentManager mFragmentManager;

    public final String PERMISSIONS_REQUESTED_AFTER_ONBOARDING = "Onboarding Permissions";
    public final String DEBUG_TAG = "MY_ACTIVITY_INFO";
    public static String FRAGMENT_TAG = "MY_FRAGMENT_TAG";
    public final String RECEIVER_STATE = "IS_RECEIVER_ENABLED";

    public boolean isReceiverRegistered;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our OnboardingFragment
        if (!mSharedPreferences.getBoolean(
                OnBoardingActivity.COMPLETED_ONBOARDING_PREF_NAME, false)) {

            Intent intent = new Intent(this, OnBoardingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        //Creates all objects that involve Broadcast Receivers.
        receiverFactory = new ReceiverFactory(getApplicationContext());
        mPermissionUtils = new PermissionUtils(getApplicationContext(), this);
        mFragmentManager = getSupportFragmentManager();

        ContactViewModel viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        viewModel.getContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                if(contacts != null){
                    receiverFactory.setContacts(contacts);
                }
            }
        });

        //Handles rotation changes
        Fragment mFragment;
        if(savedInstanceState == null){
            mFragment = new ContactListFragment();
            mFragment.setArguments(getIntent().getExtras());
        }else{
            mFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mFragment, FRAGMENT_TAG).commit();

        if(savedInstanceState != null){
            isReceiverRegistered = savedInstanceState.getBoolean(RECEIVER_STATE);
            if(isReceiverRegistered){
                receiverFactory.registerReceivers();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(RECEIVER_STATE, receiverFactory.isReceiverRegistered());

        super.onSaveInstanceState(outState);
    }

    /**
     * Take care of popping the mFragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    receiverFactory.registerReceivers();
                    invalidateOptionsMenu();
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

        if(mPermissionUtils.permissionsGranted() && !receiverFactory.isReceiverRegistered()){

            receiverFactory.registerReceivers();
            item.setIcon(R.drawable.ic_phone_in_talk_secondary_24dp);

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mSharedPreferences.getBoolean(
                OnBoardingActivity.COMPLETED_ONBOARDING_PREF_NAME, false) &&
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(receiverFactory.isReceiverRegistered()){
            receiverFactory.unregisterReceivers();
        }
    }
}
