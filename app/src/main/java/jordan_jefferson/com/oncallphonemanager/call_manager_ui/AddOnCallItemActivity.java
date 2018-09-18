package jordan_jefferson.com.oncallphonemanager.call_manager_ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import jordan_jefferson.com.oncallphonemanager.BaseActivity;
import jordan_jefferson.com.oncallphonemanager.R;

public class AddOnCallItemActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_on_call_item);

        Fragment newOnCallItemFragment = AddOnCallItemFragment.newInstance();
        newOnCallItemFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.add_call_item_container, newOnCallItemFragment, null)
                .commit();

    }
}
