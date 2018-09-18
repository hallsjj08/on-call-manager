package jordan_jefferson.com.oncallphonemanager;

import android.support.v4.app.Fragment;
import android.os.Bundle;

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
