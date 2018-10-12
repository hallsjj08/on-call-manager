package jordan_jefferson.com.oncallphonemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import jordan_jefferson.com.oncallphonemanager.callmanagerservices.CallManagerWorker;
import jordan_jefferson.com.oncallphonemanager.callmanager.CallManagerFragment;
import jordan_jefferson.com.oncallphonemanager.contacts.ContactListFragment;
import jordan_jefferson.com.oncallphonemanager.onboarding.OnBoardingActivity;

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final String TAG = getClass().getSimpleName();
    private final String CALL_FRAG_TAG = "CallFragTag";
    private final String CONTACT_FRAG_TAG = "ContactFragTag";
    private final String MORE_FRAG_TAG = "MoreFragTag";
    private final String CURRENT_SELECTION_KEY = "CurrentSelectionKey";

    private Fragment callManagerFragment;
    private Fragment contactListFramgent;
    private Fragment moreFragment;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(getApplication());
        // Normal app init code...

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our OnboardingFragment
        if (!mSharedPreferences.getBoolean(
                OnBoardingActivity.COMPLETED_ONBOARDING_PREF_NAME, false)) {

            Intent intent = new Intent(this, OnBoardingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            OneTimeWorkRequest callManagerWorker =
                    new OneTimeWorkRequest.Builder(CallManagerWorker.class).build();

            WorkManager.getInstance().cancelAllWork();
            WorkManager.getInstance().enqueue(callManagerWorker);
        }

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if(savedInstanceState != null){
            callManagerFragment = getSupportFragmentManager().findFragmentByTag(CALL_FRAG_TAG);
            contactListFramgent = getSupportFragmentManager().findFragmentByTag(CONTACT_FRAG_TAG);
            bottomNavigationView.setSelectedItemId(savedInstanceState.getInt(CURRENT_SELECTION_KEY));
        }else{
            bottomNavigationView.setSelectedItemId(R.id.navigation_call_manager);
        }

    }

    /**
     * Called when an item in the bottom navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not
     * be selected. Consider setting non-selectable items as disabled preemptively to
     * make them appear non-interactive.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.navigation_call_manager:
                if(callManagerFragment == null){
                    callManagerFragment = CallManagerFragment.newInstance();
                    addFragment(callManagerFragment, CALL_FRAG_TAG);
                }else {
                    swapFragments(callManagerFragment);
                }
                Log.d(TAG, "Call Manager Selected");
                break;
            case R.id.navigation_contacts:
                if(contactListFramgent == null){
                    contactListFramgent = ContactListFragment.newInstance();
                    addFragment(contactListFramgent, CONTACT_FRAG_TAG);
                }else {
                    swapFragments(contactListFramgent);
                }
                Log.d(TAG, "Contacts Selected");
                break;
            case R.id.navigation_about:
                Log.d(TAG, "More Selected");
                break;
        }

        return true;
    }

    private void swapFragments(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragmentContainer, fragment, null)
                .commit();
    }

    private void addFragment(Fragment fragment, String fragmentTag){
        getSupportFragmentManager().beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.fragmentContainer, fragment, fragmentTag)
                .commit();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if(bottomNavigationView.getSelectedItemId() != R.id.navigation_call_manager &&
                getSupportFragmentManager().getBackStackEntryCount() < 1){
            bottomNavigationView.setSelectedItemId(R.id.navigation_call_manager);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_SELECTION_KEY, bottomNavigationView.getSelectedItemId());
    }
}
