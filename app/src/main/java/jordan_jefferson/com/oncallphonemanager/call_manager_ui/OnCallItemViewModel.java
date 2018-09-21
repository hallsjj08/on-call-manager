package jordan_jefferson.com.oncallphonemanager.call_manager_ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import jordan_jefferson.com.oncallphonemanager.data.OnCallItem;
import jordan_jefferson.com.oncallphonemanager.data.OnCallItemRepository;

public class OnCallItemViewModel extends AndroidViewModel {

    private static final String TAG = "RX_VIEW_MODEL";
    private OnCallItemRepository onCallItemRepository;
    private Flowable<List<OnCallItem>> allOnCallItems;
    private Flowable<List<OnCallItem>> allActiveOnCallItems;
    private LiveData<Integer> maxGroupId;

    public OnCallItemViewModel(@NonNull Application application) {
        super(application);

        onCallItemRepository = new OnCallItemRepository(application);
        allOnCallItems = onCallItemRepository.getAllOnCallItems();
        allActiveOnCallItems = onCallItemRepository.getAllActiveOnCallItems();
        maxGroupId = onCallItemRepository.getMaxGroupId();

    }

    public Flowable<List<OnCallGroupItem>> getAllOnCallItems() {
        return allOnCallItems.map(onCallItems -> {

            List<OnCallGroupItem> onCallGroupItems = new ArrayList<>();
            OnCallGroupItem groupItem;
            int tempId = -1;
            int groupItemIndex = -1;

            for(OnCallItem onCallItem : onCallItems){

                if(onCallItem.getGroupId() != tempId){
                    tempId = onCallItem.getGroupId();
                    groupItem = new OnCallGroupItem();
                    onCallGroupItems.add(groupItem);
                    groupItemIndex += 1;
                    onCallGroupItems.get(groupItemIndex).addOnCallItem(onCallItem);
                    onCallGroupItems.get(groupItemIndex).setPresenterData();
                }else{
                    onCallGroupItems.get(groupItemIndex).addOnCallItem(onCallItem);
                }
            }

            return onCallGroupItems;
        });
    }

    public Flowable<List<OnCallItem>> getAllActiveOnCallItems() {
        return allActiveOnCallItems;
    }

    public LiveData<Integer> getMaxGroupId() {
        return maxGroupId;
    }

    public void insertOnCallItems(List<OnCallItem> onCallItems){
        onCallItemRepository.insertOnCallItemsAsync(onCallItems);
    }

    public void deletOnCallGroupItems(int groupId){
        onCallItemRepository.deletGroupItemsAsync(groupId);
    }

    public void deleteOnCallItems(List<OnCallItem> onCallItems){
        onCallItemRepository.deleteOnCallItemsAsync(onCallItems);
    }

    public void clearAllItems(){onCallItemRepository.clearAllItmes();}
}
