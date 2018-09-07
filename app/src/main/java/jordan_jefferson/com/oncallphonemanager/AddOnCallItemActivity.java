package jordan_jefferson.com.oncallphonemanager;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;

public class AddOnCallItemActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_on_call_item);

        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.add_call_item_container, AddOnCallItemFragment.newInstance(), null)
                .commit();

    }
}
