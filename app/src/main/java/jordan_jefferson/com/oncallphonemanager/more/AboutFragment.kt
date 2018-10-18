package jordan_jefferson.com.oncallphonemanager.more


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import jordan_jefferson.com.oncallphonemanager.R
import jordan_jefferson.com.oncallphonemanager.onboarding.OnBoardingActivity


class AboutFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        init(view)

        return view
    }

    private fun init(view: View){
        val backButton = view.findViewById<ImageButton>(R.id.navigateUpButton)
        val tvVersion = view.findViewById<TextView>(R.id.version)
        val aboutDeveloper = view.findViewById<Button>(R.id.aboutDeveloper)
        val productTour = view.findViewById<Button>(R.id.productTour)

        backButton.setOnClickListener(this)
        tvVersion.text = getProductVersion()
        aboutDeveloper.setOnClickListener(this)
        productTour.setOnClickListener(this)
    }

    private fun getProductVersion(): String{
        return try {
            val pInfo = context?.packageManager?.getPackageInfo(context?.packageName, 0)
            "Version: " + pInfo?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "Product Version Not Found."
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.navigateUpButton -> activity?.finish()
            R.id.aboutDeveloper -> launchAboutDeveloper()
            R.id.productTour -> launchProductTour()
        }
    }

    private fun launchAboutDeveloper(){
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://jordan-jefferson/[58374ab2]"))
        val packageManager = context!!.packageManager
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list.isEmpty()) {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=jordan-jefferson-58374ab2"))
        }
        startActivity(intent)
    }

    private fun launchProductTour(){
        activity?.startActivity(Intent(context, OnBoardingActivity::class.java))
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment{
            return AboutFragment()
        }
    }
}
