package jordan_jefferson.com.oncallphonemanager;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

@Database(entities = {Contact.class, AlarmSchedule.class}, version = 3, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class ContactDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();
    public abstract AlarmScheduleDao alarmScheduleDao();

    private static ContactDatabase INSTANCE;

    public static ContactDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ContactDatabase.class, "contacts").fallbackToDestructiveMigration().build();
        }

        return INSTANCE;
    }
}
