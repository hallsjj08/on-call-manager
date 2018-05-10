package jordan_jefferson.com.oncallphonemanager;

import android.content.Context;

import java.util.ArrayList;

/*
A Singleton class that retrieves a list of contacts from the database and places them in an arraylist.
This arraylist is passed to the adapter to be displayed to the user. It also handles the addition,
update, and deletion of contacts from the arraylist and the database. This class also provides the
phoneNumberAnalyzer method which compares an incoming call's phone number to the phone numbers in the
contact list.
 */
public class ContactsList {

    private static ContactsList sContactsList;
    private Context mAppContext;
    private ArrayList<Contact> mContacts;
    private DBManager myDB;

    private ContactsList(Context appContext){

        this.mAppContext = appContext;

        this.myDB = new DBManager(mAppContext);
        this.mContacts = myDB.retrieveContacts();

    }

    public static ContactsList getInstance(Context c){
        if(sContactsList == null){
            sContactsList = new ContactsList(c.getApplicationContext());
        }

        return sContactsList;
    }

    public void addContact(Contact c){
        mContacts.add(c);
        myDB.addContact(c);
    }

    public void updateContact(Contact c, int position){
        mContacts.set(position, c);
        myDB.updateContact(c);
    }

    public void removeContact(Contact c, int position){
        myDB.deleteContact(c);
        mContacts.remove(position);
    }

    public Contact getContact(int position){
        return mContacts.get(position);
    }

    public ArrayList<Contact> getContactsList() {
        return mContacts;
    }

    public boolean phoneNumberAnalyzer(String incomingNumber){

        for(int i = 0; i < mContacts.size(); i++){
            if(incomingNumber.matches(mContacts.get(i).get_contactRegexNumber())){
                return true;
            }
        }

        return false;
    }
}
