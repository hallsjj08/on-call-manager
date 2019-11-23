package jordan_jefferson.com.oncallphonemanager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlarmScheduleDao {

    @Query("SELECT * FROM alarmSchedule ORDER BY name ASC")
    LiveData<List<AlarmSchedule>> getAlarmSchedues();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAlarmSchedule(AlarmSchedule schedule);

    @Delete
    void delete(AlarmSchedule schedule);

}
