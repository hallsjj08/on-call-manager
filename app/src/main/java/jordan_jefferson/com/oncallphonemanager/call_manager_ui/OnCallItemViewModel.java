package jordan_jefferson.com.oncallphonemanager.call_manager_ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return allOnCallItems.map(new Function<List<OnCallItem>, List<OnCallGroupItem>>() {

            @Override
            public List<OnCallGroupItem> apply(List<OnCallItem> onCallItems) throws Exception {
                Map<Integer, List<OnCallItem>> groupedItems = new HashMap<>();

                for(OnCallItem onCallItem : onCallItems){
                    if(groupedItems.containsKey(onCallItem.getGroupId())){
                        groupedItems.get(onCallItem.getGroupId()).add(onCallItem);
                    }else {
                        List<OnCallItem> groupOnCallItems = new ArrayList<>();
                        groupOnCallItems.add(onCallItem);
                        groupedItems.put(onCallItem.getGroupId(), groupOnCallItems);
                    }
                }

                List<OnCallGroupItem> onCallGroupItems = new ArrayList<>();
                for (Map.Entry<Integer, List<OnCallItem>> pair : groupedItems.entrySet()) {
                    OnCallGroupItem item = new OnCallGroupItem(pair.getKey());
                    item.setOnCallItems(pair.getValue());
                    onCallGroupItems.add(item);
                }

                Log.d(TAG, groupedItems.toString());

                return onCallGroupItems;
            }
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
}
