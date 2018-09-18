package jordan_jefferson.com.oncallphonemanager;

import java.util.List;

public class OnCallGroupItem {

    private int groupId;
    private String timeDescription;
    private String label;
    private List<OnCallItem> onCallItems;

    public OnCallGroupItem(int groupId){
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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

        label = onCallItems.get(0).getLabel() + ": ";

        timeDescription = onCallItems.get(0).getStartTimeHour() + ":" + onCallItems.get(0).getStartTimeMinute()
                + " - " + onCallItems.get(0).getEndTimeHour() + ":" + onCallItems.get(0).getEndTimeMinute();

        for(OnCallItem item : onCallItems){
            label = label + ", " + item.getDay();
        }
    }
}
