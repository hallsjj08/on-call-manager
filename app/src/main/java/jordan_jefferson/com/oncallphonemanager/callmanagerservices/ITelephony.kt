package jordan_jefferson.com.oncallphonemanager.callmanagerservices

interface ITelephony {
    fun incomingCall(phoneStateListener: CustomPhoneStateListener, incomingNumber: String?)
    fun destroyService(phoneStateListener: CustomPhoneStateListener)
}