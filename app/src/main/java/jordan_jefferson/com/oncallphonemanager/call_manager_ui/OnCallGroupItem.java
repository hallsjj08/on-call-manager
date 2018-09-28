package jordan_jefferson.com.oncallphonemanager.call_manager_ui;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jordan_jefferson.com.oncallphonemanager.data.OnCallItem;

public class OnCallGroupItem implements Serializable {

    private int groupId;
    private boolean isActive;
    private boolean allDay;
    private String startTime;
    private String endTime;
    private String label;
    private List<OnCallItem> onCallItems;
    private List<String> repeatedDays;

    OnCallGroupItem(){
        groupId = -1;
        isActive = false;
        allDay = false;
        startTime = "Starts: ";
        endTime = "Ends: ";
        label = "";
        onCallItems = new ArrayList<>(7);
        repeatedDays = new ArrayList<>();
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean isActive() {
        Log.d("Group Id " + groupId, isActive + "");
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;

        for(OnCallItem item : onCallItems){
            item.setActive(active);
        }
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<OnCallItem> getOnCallItems() {
        return onCallItems;
    }

    private void initData(){
        if(!onCallItems.isEmpty()){
            OnCallItem item = onCallItems.get(0);
            groupId = item.getGroupId();
            isActive = item.isActive();
            allDay = item.isAllDay();
            startTime = startTime + item.getDisplayStartTime();
            endTime = endTime + item.getDisplayEndTime();

            if(item.getLabel() != null){
                label = item.getLabel();
            }

            for(OnCallItem labelItem : onCallItems){
                if(labelItem.getDay() != null){
                    label = label + " " + labelItem.getDay();
                    repeatedDays.add(labelItem.getDay());
                }
            }
        }
    }

    public void setOnCallItems(List<OnCallItem> onCallItems) {
        this.onCallItems = onCallItems;
        initData();
    }

    public List<String> getRepeatedDays(){
        return repeatedDays;
    }
}
