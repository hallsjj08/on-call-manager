package jordan_jefferson.com.oncallphonemanager.contacts;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import jordan_jefferson.com.oncallphonemanager.data.Contact;
import jordan_jefferson.com.oncallphonemanager.data.ContactRepository;

public class ContactViewModel extends AndroidViewModel {
    
    private ContactRepository contactRepository;
    private LiveData<List<Contact>> contacts;
    
    public ContactViewModel(@NonNull Application application) {
        super(application);
        
        contactRepository = new ContactRepository(application.getApplicationContext());
        contacts = contactRepository.getAllContacts();
        
    }

    public LiveData<List<Contact>> getContacts(){
        return contacts;
    }

    public void insert(Contact contact){
        contactRepository.insert(contact);
    }

    public void delete(Contact contact) { contactRepository.delete(contact); }
    
}
