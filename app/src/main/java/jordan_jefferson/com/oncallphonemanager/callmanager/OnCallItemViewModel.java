package jordan_jefferson.com.oncallphonemanager.callmanager;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import jordan_jefferson.com.oncallphonemanager.data.OnCallItem;
import jordan_jefferson.com.oncallphonemanager.data.OnCallItemRepository;

public class OnCallItemViewModel extends AndroidViewModel {

    private static final String TAG = "RX_VIEW_MODEL";
    private OnCallItemRepository onCallItemRepository;
    private Flowable<List<OnCallItem>> allOnCallItems;

    public OnCallItemViewModel(@NonNull Application application) {
        super(application);
        
        onCallItemRepository = new OnCallItemRepository(application.getApplicationContext());
        allOnCallItems = onCallItemRepository.getAllOnCallItems();

    }

    public Flowable<List<OnCallGroupItem>> getAllOnCallItems() {
        return allOnCallItems.map(onCallItems -> {

            LinkedHashMap<Integer, List<OnCallItem>> newonCallGroupItems = new LinkedHashMap<>();
            List<OnCallItem> subList = new ArrayList<>();
            List<OnCallGroupItem> onCallGroupItems = new ArrayList<>();
            OnCallGroupItem groupItem = new OnCallGroupItem();

            int tempId = -1;
            int index = 1;

            if(!onCallItems.isEmpty()){
                tempId = onCallItems.get(0).getGroupId();
                onCallGroupItems.add(groupItem);
            }

            for(OnCallItem onCallItem : onCallItems){
                if(onCallItem.getGroupId() == tempId){
                    subList.add(onCallItem);
                }else {
                    newonCallGroupItems.put(index, subList);
                    tempId = onCallItem.getGroupId();
                    subList = new ArrayList<>();
                    subList.add(onCallItem);
                    index += 1;
                    onCallGroupItems.add(new OnCallGroupItem());
                }

                if(onCallItem.equals(onCallItems.get(onCallItems.size() - 1))){
                    newonCallGroupItems.put(index, subList);
                }
            }

            index = 0;

            for(Map.Entry<Integer, List<OnCallItem>> entry : newonCallGroupItems.entrySet()){
                onCallGroupItems.get(index).setOnCallItems(entry.getValue());
                index += 1;
            }

            return onCallGroupItems;
        });
    }

    public void insertOnCallItems(List<OnCallItem> onCallItems){
        onCallItemRepository.insertOnCallItemsAsync(onCallItems.toArray(new OnCallItem[0]));
    }

    public void deletOnCallGroupItems(int groupId){
        onCallItemRepository.deletGroupItemsAsync(groupId);
    }

    public void deleteOnCallItems(List<OnCallItem> onCallItems){
        onCallItemRepository.deleteOnCallItemsAsync(onCallItems);
    }

    public void clearAllItems(){onCallItemRepository.clearAllItmes();}
}
