package jordan_jefferson.com.oncallphonemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/*
Currently unused class. This class will be used to handle all fragment transactions.
 */
public class SingleFragmentActivity extends FragmentActivity {

    /**
     * Perform initialization of all fragments and loaders.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_call_manager);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){
            fragment = new ContactListFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }else{
            //fragment = createFragment();
            fm.beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, fragment);
        }
    }
}
