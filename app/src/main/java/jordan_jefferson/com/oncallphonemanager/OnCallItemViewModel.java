package jordan_jefferson.com.oncallphonemanager;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class OnCallItemViewModel extends AndroidViewModel {

    private OnCallItemRepository onCallItemRepository;
    private LiveData<List<OnCallItem>> allOnCallItems;
    private LiveData<List<OnCallItem>> allActiveOnCallItems;
    private LiveData<Integer> maxGroupId;

    public OnCallItemViewModel(@NonNull Application application) {
        super(application);

        onCallItemRepository = new OnCallItemRepository(application);
        allOnCallItems = onCallItemRepository.getAllOnCallItems();
        allActiveOnCallItems = onCallItemRepository.getAllActiveOnCallItems();
        maxGroupId = onCallItemRepository.getMaxGroupId();

    }

    public LiveData<List<OnCallItem>> getAllOnCallItems() {
        return allOnCallItems;
    }

    public LiveData<List<OnCallItem>> getAllActiveOnCallItems() {
        return allActiveOnCallItems;
    }

    public LiveData<Integer> getMaxGroupId() {
        return maxGroupId;
    }

    public void insertOnCallItems(List<OnCallItem> onCallItems){
        onCallItemRepository.insertOnCallItemsAsync(onCallItems);
    }

    public void deleteOnCallItems(List<OnCallItem> onCallItems){
        onCallItemRepository.deleteOnCallItemsAsync(onCallItems);
    }
}
