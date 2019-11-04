package jordan_jefferson.com.oncallphonemanager;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    
    private ContactRepository contactRepository;
    private LiveData<List<Contact>> contacts;
    
    public ContactViewModel(@NonNull Application application) {
        super(application);
        
        contactRepository = new ContactRepository(application);
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
