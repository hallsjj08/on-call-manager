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
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PermissionUtils mPermissionUtils;
    private ReceiverFactory receiverFactory;
    public FragmentManager mFragmentManager;
    private SwitchCompat callManagerSwitch;

    public final String DEBUG_TAG = "MY_ACTIVITY_INFO";
    public static String FRAGMENT_TAG = "MY_FRAGMENT_TAG";
    public final String RECEIVER_STATE = "IS_RECEIVER_ENABLED";

    public boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
            isReceiverRegistered = savedInstanceState.getBoolean(RECEIVER_STATE);
            if(isReceiverRegistered){
                receiverFactory.registerReceivers();
            }
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mFragment, FRAGMENT_TAG).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(RECEIVER_STATE, receiverFactory.isReceiverRegistered());

        super.onSaveInstanceState(outState);
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(receiverFactory.isReceiverRegistered() && mPermissionUtils.permissionsGranted() && mPermissionUtils.isNotificationAccessGranted()){
            callManagerSwitch.setChecked(true);
        }else{
            callManagerSwitch.setChecked(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        callManagerSwitch = menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchForActionBar);

        callManagerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mPermissionUtils.isNotificationAccessGranted() && mPermissionUtils.permissionsGranted()
                        && !receiverFactory.isReceiverRegistered()){
                    receiverFactory.registerReceivers();
                    Snackbar snack = Snackbar.make(getCurrentFocus(), "Call Manager On", Snackbar.LENGTH_SHORT);
                    snack.getView().findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snack.show();
                }else if(!mPermissionUtils.permissionsGranted() || !mPermissionUtils.isNotificationAccessGranted()){

                    if(!mPermissionUtils.permissionsGranted()){
                        mPermissionUtils.requestPermissions();
                    }

                    if(!mPermissionUtils.isNotificationAccessGranted()){
                        mPermissionUtils.alertNotificationAccessNeeded(MainActivity.this);
                    }
                }else{
                    if(receiverFactory.isReceiverRegistered()){

                        receiverFactory.unregisterReceivers();
                        Snackbar snack = Snackbar.make(getCurrentFocus(), "Call Manager Off", Snackbar.LENGTH_SHORT);
                        snack.getView().findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        snack.show();

                    }
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_about2:
                Intent intent = new Intent(MainActivity.this, OnBoardingActivity.class);
                MainActivity.this.startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(DEBUG_TAG, "Resumed");

        if(!mPermissionUtils.isNotificationAccessGranted() || !mPermissionUtils.permissionsGranted()){
            if(receiverFactory.isReceiverRegistered()){
                receiverFactory.unregisterReceivers();
            }
            invalidateOptionsMenu();
        }
    }
}
