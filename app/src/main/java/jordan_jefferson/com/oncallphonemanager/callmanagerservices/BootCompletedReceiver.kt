package jordan_jefferson.com.oncallphonemanager.callmanagerservices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import jordan_jefferson.com.oncallphonemanager.MainActivity

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Toast.makeText(context, "Call Manager Boot Completed", Toast.LENGTH_LONG).show()

        if(Intent.ACTION_BOOT_COMPLETED == intent?.action){
            Log.d("Receiver", "Boot Completed")
            MainActivity.queueCallManagerWorker(0)
        }
    }
}