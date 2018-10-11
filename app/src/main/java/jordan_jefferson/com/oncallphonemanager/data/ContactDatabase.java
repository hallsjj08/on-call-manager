package jordan_jefferson.com.oncallphonemanager.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Contact.class, OnCallItem.class}, version = 4, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();
    public abstract OnCallItemDao onCallItemDao();

    private static ContactDatabase INSTANCE;

    static ContactDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ContactDatabase.class, "contacts")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
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

    private final static Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE IF NOT EXISTS 'tempItems' (" +
                    "'_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "'day' TEXT, " +
                    "'active' INTEGER NOT NULL, " +
                    "'allDay' INTEGER NOT NULL, " +
                    "'displayStartTime' TEXT, " +
                    "'displayEndTime' TEXT, " +
                    "'label' TEXT, " +
                    "'groupId' INTEGER NOT NULL)");

            database.execSQL("INSERT INTO 'tempItems' SELECT _id, day, active, allDay, displayStartTime, " +
                    "displayEndTime, label, groupId FROM 'onCallItems'");
            database.execSQL("DROP TABLE 'onCallItems'");
            database.execSQL("ALTER TABLE 'tempItems' RENAME TO 'onCallItems'");
        }
    };
}
