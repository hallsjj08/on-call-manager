package jordan_jefferson.com.oncallphonemanager.contacts

import android.os.Bundle
import jordan_jefferson.com.oncallphonemanager.BaseActivity
import jordan_jefferson.com.oncallphonemanager.R

class ContactInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        val contactInfoFragment = ContactInfoFragment.newInstance()
        contactInfoFragment.arguments = intent.extras

        supportFragmentManager.beginTransaction().disallowAddToBackStack()
                .add(R.id.activityFragmentContainer, contactInfoFragment, null)
                .commit()

    }
}
