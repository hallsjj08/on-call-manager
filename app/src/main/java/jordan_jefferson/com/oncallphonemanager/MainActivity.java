package jordan_jefferson.com.oncallphonemanager;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public FragmentManager mFragmentManager;
    public Fragment currentFragment;
    public Fragment contactListFragment;
    public Fragment alarmScheduleFragment;
    public static String FRAGMENT_TAG = "MY_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        // Check if we need to display our OnboardingFragment
//        if (!mSharedPreferences.getBoolean(
//                OnBoardingActivity.COMPLETED_ONBOARDING_PREF_NAME, false)) {
//
//            Intent intent = new Intent(this, OnBoardingActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        }

        mFragmentManager = getSupportFragmentManager();

        //Handles rotation changes
        Fragment mFragment;
        if(savedInstanceState == null){
            contactListFragment = new ContactListFragment();
            alarmScheduleFragment = new AlarmScheduleListFragment();
            currentFragment = contactListFragment;
        }else{
            currentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        }

        mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment, FRAGMENT_TAG).commit();

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
//        outState.putBoolean(RECEIVER_STATE, receiverFactory.isReceiverRegistered());

        super.onSaveInstanceState(outState);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(item.getTitle());
        switch (id){
            case R.id.contacts:
                mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, contactListFragment, FRAGMENT_TAG).commit();
            case R.id.whitelist_scheduler:
                mFragmentManager.beginTransaction().replace(R.id.fragmentContainer, alarmScheduleFragment, FRAGMENT_TAG).commit();
                break;
            case R.id.nav_about2:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
