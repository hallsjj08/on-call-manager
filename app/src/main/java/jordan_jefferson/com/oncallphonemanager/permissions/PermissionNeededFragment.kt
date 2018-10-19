package jordan_jefferson.com.oncallphonemanager.permissions


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import jordan_jefferson.com.oncallphonemanager.R
import jordan_jefferson.com.oncallphonemanager.utils.PermissionUtils


class PermissionNeededFragment : Fragment(), View.OnClickListener {

    private lateinit var callLogStateButton: Button
    private lateinit var dndAccessButton: Button
    private lateinit var whiteListingButton: Button

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return PermissionNeededFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_permission_needed, container, false)

        init(view)

        return view
    }

    private fun init(view: View){
        val navigateUpButton = view.findViewById<ImageButton>(R.id.navigateUpButton)
        callLogStateButton = view.findViewById(R.id.callLogStateButton)
        dndAccessButton = view.findViewById(R.id.dndButton)
        whiteListingButton = view.findViewById(R.id.whiteListingButton)

        navigateUpButton.setOnClickListener(this)
        callLogStateButton.setOnClickListener(this)
        dndAccessButton.setOnClickListener(this)
        whiteListingButton.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        checkIfPermissionIsNeeded(PermissionUtils.permissionsGranted(context), callLogStateButton)
        checkIfPermissionIsNeeded(PermissionUtils.isNotificationAccessGranted(context), dndAccessButton)
        checkIfPermissionIsNeeded(PermissionUtils.isIgnoringBatteryOptimization(context), whiteListingButton)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.navigateUpButton -> activity?.finish()
            R.id.callLogStateButton -> PermissionUtils.getPermissions(this)
            R.id.dndButton -> PermissionUtils.requestNotificationAccess(context)
            R.id.whiteListingButton ->{
                if(!PermissionUtils.isIgnoringBatteryOptimization(context)) {
                    PermissionUtils.navigateToSettigsPopup(context, null,
                            "Ignore Battery Optimization", "This app needs to ignore battery optimization " +
                            "to ensure on call times are turned on an off at the designated times. Would you like to " +
                            "edit this permission?", "Edit")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkIfPermissionIsNeeded(isNeeded: Boolean, button: Button){
        if(isNeeded){
            button.foreground = ColorDrawable(resources.getColor(R.color.goodCheckForeground, null))
        }else{
            button.foreground = ColorDrawable(resources.getColor(R.color.highImportanceForeground, null))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1){
            for (i in permissions.indices){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED
                        && !shouldShowRequestPermissionRationale(permissions[i])){

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context?.packageName, null)
                    intent.data = uri

                    PermissionUtils.navigateToSettigsPopup(context, intent, "Permissions Needed",
                            "These permissions are needed to compare incoming phone numbers to your contacts list." +
                                    " Would you like to edit these permissions in Settings?", "Settings")
                    return
                }
            }
        }
    }
}
