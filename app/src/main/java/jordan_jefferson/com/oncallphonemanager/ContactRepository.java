package jordan_jefferson.com.oncallphonemanager;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ContactRepository {

    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application){
        ContactDatabase db = ContactDatabase.getDatabase(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {return allContacts;}

    public void insert(Contact contact) {new insertAsyncTask(contactDao).execute(contact);}

    private static class insertAsyncTask extends AsyncTask<Contact, Void, Void>{
        
        private ContactDao asyncTaskDao;
        
        insertAsyncTask(ContactDao dao){
            asyncTaskDao = dao;
        }
        
        @Override
        protected Void doInBackground(Contact... contacts) {
            
            asyncTaskDao.insertContact(contacts[0]);
            return null;
            
        }
    }

    public void delete(Contact contact) {new deleteAsyncTask(contactDao).execute(contact);}

    private static class deleteAsyncTask extends AsyncTask<Contact, Void, Void>{

        private ContactDao asyncTaskDao;

        deleteAsyncTask(ContactDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {

            asyncTaskDao.delete(contacts[0]);
            return null;
        }
    }

}
