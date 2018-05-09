package jordan_jefferson.com.oncallphonemanager;

import android.content.Context;

import java.util.ArrayList;

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
