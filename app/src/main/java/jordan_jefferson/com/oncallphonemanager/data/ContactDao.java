package jordan_jefferson.com.oncallphonemanager.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY _contactName ASC")
    LiveData<List<Contact>> getContacts();

    @Query("SELECT * FROM contacts ORDER BY _contactName ASC")
    List<Contact> getContactsBlocking();

    @Query("SELECT _contactRegexNumber FROM contacts")
    List<String> getRegexNumbersBlocking();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertContact(Contact contact);

    @Delete
    void delete(Contact contact);

}
