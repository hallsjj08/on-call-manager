package jordan_jefferson.com.oncallphonemanager.call_manager_ui;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jordan_jefferson.com.oncallphonemanager.data.OnCallItem;

public class OnCallGroupItem implements Serializable {

    private int groupId;
    private boolean isActive;
    private String timeDescription;
    private String label;
    private List<OnCallItem> onCallItems;

    OnCallGroupItem(){
        onCallItems = new ArrayList<>();
    }

    public void setPresenterData(){
        if(!onCallItems.isEmpty()){
            OnCallItem item = onCallItems.get(0);
            Log.d("Item Id", item.get_id() + "");
            Log.d("GroupItemDataSet: " + item.getGroupId(), "Is Item Active: " + item.isActive());
            groupId = item.getGroupId();
            isActive = item.isActive();
            timeDescription = item.getStartTimeHour() + " - " + item.getEndTimeHour();
            label = item.getLabel();
        }
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

    public String getTimeDescription() {
        return timeDescription;
    }

    public void setTimeDescription(String timeDescription) {
        this.timeDescription = timeDescription;
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

    public void setOnCallItems(List<OnCallItem> onCallItems) {
        this.onCallItems = onCallItems;
    }

    public void addOnCallItem(OnCallItem onCallItem){
        onCallItems.add(onCallItem);
    }
}
