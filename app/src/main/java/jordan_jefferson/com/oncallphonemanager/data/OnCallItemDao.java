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

    @Query("SELECT * FROM onCallItems ORDER BY groupId DESC")
    Flowable<List<OnCallItem>> getOnCallItems();

    @Query("SELECT * FROM onCallItems WHERE active = 1")
    Flowable<List<OnCallItem>> getAllActiveItems();

    @Query("SELECT MAX(groupId) FROM onCallItems")
    LiveData<Integer> getMaxGroupId();

    @Query("DELETE FROM onCallItems WHERE groupId = :groupId")
    void deleteGroupedItems(int groupId);

    @Query("DELETE FROM onCallItems")
    void clearAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnCallItems(OnCallItem... onCallItems);

    @Delete
    void deleteOnCallItems(OnCallItem... onCallItems);

}
