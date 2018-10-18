package jordan_jefferson.com.oncallphonemanager.more


import android.content.ActivityNotFoundException
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import jordan_jefferson.com.oncallphonemanager.R
import android.content.Intent
import android.net.Uri
import android.os.Build
import java.net.URI


class MoreFragment : Fragment(), View.OnClickListener {

    private val TAG = "MORE FRAGMENT"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_more, container, false)

        init(view)

        return view
    }

    private fun init(view: View){
        val about = view.findViewById<Button>(R.id.about)
        val share = view.findViewById<Button>(R.id.share)
        val rate = view.findViewById<Button>(R.id.rate)
        val feedback = view.findViewById<Button>(R.id.feedback)
//        val settings = view.findViewById<Button>(R.id.settings)

        about.setOnClickListener(this)
        share.setOnClickListener(this)
        rate.setOnClickListener(this)
        feedback.setOnClickListener(this)
//        settings.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.about -> launchAboutActivity()
            R.id.share -> shareApp()
            R.id.rate -> rateApp()
            R.id.feedback -> sendFeedback()
//            R.id.settings -> launchAppSettings()
        }
    }

    private fun launchAboutActivity(){
        activity?.startActivity(Intent(activity, AboutActivity::class.java))
        Log.d(TAG, "About Launched")
    }

    private fun shareApp(){

        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, "On Call Phone Manager")
        var sAux = "\nLet me recommend you this application\n\n"
        sAux += "https://play.google.com/store/apps/details?id=${context?.packageName} \n\n"
        i.putExtra(Intent.EXTRA_TEXT, sAux)
        startActivity(Intent.createChooser(i, "choose one"))

        Log.d(TAG, "Share Launched")
    }

    private fun rateApp(){
        val appPackageName = context?.packageName // getPackageName() from Context or Activity object
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    private fun sendFeedback(){

        val emailIntent = Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", "jjeffe10@gmail.com", null))
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("jjeffe10@gmail.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "On Call Phone Manager Feedback")
        emailIntent.putExtra(Intent.EXTRA_TEXT, getDeviceInfo() + "\n\nFeedback: ")
        startActivity(Intent.createChooser(emailIntent, "Send Feedback:"))

        Log.d(TAG, "Feedback Launched")
    }

    private fun getDeviceInfo(): String {

        return "Device: " + Build.BRAND + " " + Build.PRODUCT + "\n" +
                "Android Version: " + Build.VERSION.SDK_INT + "\n" +
                "OS Version: " + System.getProperty("os.version")
    }

//    private fun launchAppSettings(){
//        Log.d(TAG, "Settings Launched")
//    }

}
