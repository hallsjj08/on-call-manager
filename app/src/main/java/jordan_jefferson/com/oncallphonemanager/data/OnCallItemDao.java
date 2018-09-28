package jordan_jefferson.com.oncallphonemanager.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface OnCallItemDao {

    @Query("SELECT * FROM onCallItems ORDER BY groupId DESC, case day " +
            "when null then 0 " +
            "when 'Sunday' then 1 " +
            "when 'Monday' then 2 " +
            "when 'Tuesday' then 3 " +
            "when 'Wednesday' then 4 " +
            "when 'Thursday' then 5 " +
            "when 'Friday' then 6 " +
            "when 'Saturday' then 7 " +
            "end")
    Flowable<List<OnCallItem>> getOnCallItems();

    @Query("SELECT * FROM onCallItems WHERE active = 1")
    List<OnCallItem> getAllActiveItems();

    @Query("DELETE FROM onCallItems WHERE groupId = :groupId")
    void deleteGroupedItems(int groupId);

    @Query("DELETE FROM onCallItems")
    void clearAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnCallItems(OnCallItem... onCallItems);

    @Delete
    void deleteOnCallItems(OnCallItem... onCallItems);

}
