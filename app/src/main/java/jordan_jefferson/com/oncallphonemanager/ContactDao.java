package jordan_jefferson.com.oncallphonemanager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY _contactName ASC")
    LiveData<List<Contact>> getContacts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertContact(Contact contact);

    @Delete
    void delete(Contact contact);

}
