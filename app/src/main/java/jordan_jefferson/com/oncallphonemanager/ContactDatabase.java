package jordan_jefferson.com.oncallphonemanager;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();

    private static ContactDatabase INSTANCE;

    public static ContactDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ContactDatabase.class, "contacts").build();
        }

        return INSTANCE;
    }
}
