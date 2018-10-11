package jordan_jefferson.com.oncallphonemanager.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "onCallItems")
public class OnCallItem implements Serializable {

    @PrimaryKey (autoGenerate = true)
    private int _id;
    private String day;
    private boolean active;
    private boolean allDay;
    private String label;
    private String displayStartTime;
    private String displayEndTime;
    private int groupId;

    public OnCallItem(String day, boolean active, boolean allDay, String label,
                      String displayStartTime, String displayEndTime, int groupId) {

        this.day = day;
        this.active = active;
        this.allDay = allDay;
        this.label = label;
        this.displayStartTime = displayStartTime;
        this.displayEndTime = displayEndTime;
        this.groupId = groupId;

    }

    int get_id() {
        return _id;
    }

    void set_id(int _id) {
        this._id = _id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDisplayStartTime() {
        return displayStartTime;
    }

    public void setDisplayStartTime(String displayStartTime) {
        this.displayStartTime = displayStartTime;
    }

    public String getDisplayEndTime() {
        return displayEndTime;
    }

    public void setDisplayEndTime(String displayEndTime) {
        this.displayEndTime = displayEndTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
