package jordan_jefferson.com.oncallphonemanager.contacts

import android.os.Bundle
import jordan_jefferson.com.oncallphonemanager.BaseActivity
import jordan_jefferson.com.oncallphonemanager.R

class ContactInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)

        val contactInfoFragment = ContactInfoFragment.newInstance()
        contactInfoFragment.arguments = intent.extras

        supportFragmentManager.beginTransaction().disallowAddToBackStack()
                .add(R.id.contact_info_container, contactInfoFragment, null)
                .commit()

    }
}
