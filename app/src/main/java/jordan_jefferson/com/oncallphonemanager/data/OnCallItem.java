package jordan_jefferson.com.oncallphonemanager.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

@Entity (tableName = "onCallItems")
public class OnCallItem implements Serializable {

    @PrimaryKey (autoGenerate = true)
    private int _id;
    private String day;
    private boolean active;
    private boolean allDay;
    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;
    private String label;
    private String displayStartTime;
    private String displayEndTime;
    private int groupId;

    public OnCallItem(String day, boolean active, boolean allDay, int startTimeHour, int startTimeMinute,
                      int endTimeHour, int endTimeMinute, String label, String displayStartTime, String displayEndTime, int groupId) {
        this.day = day;
        this.active = active;

        //TODO: Create class and TypeConverter for allDay, start, and endtimes.
        this.allDay = allDay;
        this.startTimeHour = startTimeHour;
        this.startTimeMinute = startTimeMinute;
        this.endTimeHour = endTimeHour;
        this.endTimeMinute = endTimeMinute;

        this.label = label;
        this.displayStartTime = displayStartTime;
        this.displayEndTime = displayEndTime;
        this.groupId = groupId;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
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

    public int getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(int startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public int getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(int startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(int endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public int getEndTimeMunute() {
        return endTimeMinute;
    }

    public void setEndTimeMunute(int endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
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

    public int getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(int endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
