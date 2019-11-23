package jordan_jefferson.com.oncallphonemanager;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "alarmSchedule")
public class AlarmSchedule implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long _id;
    private String name;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private boolean enabled;
    private boolean[] repeatDays;

    @Ignore
    private OnTimeUpdatedListener listener;

    AlarmSchedule(){
        Calendar calendar = Calendar.getInstance();
        startHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMinute = calendar.get(Calendar.MINUTE);
        endHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        if(endHour == 24) endHour = 0;
        endMinute = calendar.get(Calendar.MINUTE);
        repeatDays = new boolean[] {false, false, false, false, false, false, false};
    }

    protected AlarmSchedule(Parcel in) {
        _id = in.readLong();
        name = in.readString();
        startHour = in.readInt();
        startMinute = in.readInt();
        endHour = in.readInt();
        endMinute = in.readInt();
        enabled = in.readByte() != 0;
        repeatDays = in.createBooleanArray();
    }

    public static final Creator<AlarmSchedule> CREATOR = new Creator<AlarmSchedule>() {
        @Override
        public AlarmSchedule createFromParcel(Parcel in) {
            return new AlarmSchedule(in);
        }

        @Override
        public AlarmSchedule[] newArray(int size) {
            return new AlarmSchedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(name);
        dest.writeInt(startHour);
        dest.writeInt(startMinute);
        dest.writeInt(endHour);
        dest.writeInt(endMinute);
        dest.writeByte(!enabled ? (byte) 0 : (byte) 1);
        dest.writeBooleanArray(repeatDays);
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    @Ignore
    public void setStartTime(int hour, int minute) {
        startHour = hour;
        startMinute = minute;
        if(listener != null) listener.onStartTimeUpdated(getStartTimeString(false));

    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    @Ignore
    public void setEndTime(int hour, int minute) {
        endHour = hour;
        endMinute = minute;
        if(listener != null) listener.onEndTimeUpdated(getEndTimeString(false));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean[] getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(boolean[] repeatDays) {
        this.repeatDays = repeatDays;
    }

    @Ignore
    public String getStartTimeString(boolean is24HourFormat) {
        String input = startHour + ":" + endHour;

        if(is24HourFormat) return input;

        //Date/time pattern of input date
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
        //Date/time pattern of desired output date
        SimpleDateFormat outputformat = new SimpleDateFormat("h:mm aa", Locale.getDefault());
        Date date;
        String output = null;
        try{
            //Conversion of input String to date
            date= df.parse(input);
            //old date format to new date format
            output = outputformat.format(date);
        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return "Start: " + output;
    }

    @Ignore
    public String getEndTimeString(boolean is24HourFormat) {
        String input = endHour + ":" + endMinute;

        if(is24HourFormat) return input;

        //Date/time pattern of input date
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
        //Date/time pattern of desired output date
        SimpleDateFormat outputformat = new SimpleDateFormat("h:mm aa", Locale.getDefault());
        Date date;
        String output = null;
        try{
            //Conversion of input String to date
            date= df.parse(input);
            //old date format to new date format
            output = outputformat.format(date);
        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return "End: " + output;
    }

    @Ignore
    public void setOnTimeUpdateListener(OnTimeUpdatedListener listener) {
        this.listener = listener;
    }

    public class AlarmScheduleEnable {
        private long _id;
        private boolean enabled;

    }
}
