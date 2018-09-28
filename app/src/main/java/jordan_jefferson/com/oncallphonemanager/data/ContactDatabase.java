package jordan_jefferson.com.oncallphonemanager.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Contact.class, OnCallItem.class}, version = 3, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();
    public abstract OnCallItemDao onCallItemDao();

    private static ContactDatabase INSTANCE;

    public static ContactDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ContactDatabase.class, "contacts")
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }

    private final static Migration MIGRATION_1_2 = new Migration(1, 2) {
                @Override
                public void migrate(@NonNull final SupportSQLiteDatabase database) {
                    database.execSQL("CREATE TABLE IF NOT EXISTS 'onCallItems' (" +
                            "'_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "'day' TEXT, " +
                            "'active' INTEGER NOT NULL, " +
                            "'allDay' INTEGER NOT NULL, " +
                            "'startTimeHour' INTEGER NOT NULL, " +
                            "'startTimeMinute' INTEGER NOT NULL, " +
                            "'endTimeHour' INTEGER NOT NULL, " +
                            "'endTimeMinute' INTEGER NOT NULL, " +
                            "'label' TEXT, " +
                            "'groupId' INTEGER NOT NULL)");
                }
            };

    private final static Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'onCallItems' ADD COLUMN 'displayStartTime' TEXT DEFAULT NULL");
            database.execSQL("ALTER TABLE 'onCallItems' ADD COLUMN 'displayEndTime' TEXT DEFAULT NULL");
        }
    };
}
