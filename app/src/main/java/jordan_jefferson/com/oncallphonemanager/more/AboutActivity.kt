package jordan_jefferson.com.oncallphonemanager.more

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import jordan_jefferson.com.oncallphonemanager.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.activityFragmentContainer, AboutFragment.newInstance(), "About Fragment")
                .disallowAddToBackStack()
                .commit()
    }
}
