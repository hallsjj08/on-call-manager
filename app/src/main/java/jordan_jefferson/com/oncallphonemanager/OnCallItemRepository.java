package jordan_jefferson.com.oncallphonemanager;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class OnCallItemRepository {

    private OnCallItemDao onCallItemDao;
    private LiveData<List<OnCallItem>> allOnCallItems;
    private LiveData<List<OnCallItem>> allActiveOnCallItems;
    private LiveData<Integer> maxGroupId;

    public OnCallItemRepository(Application application) {
        ContactDatabase db = ContactDatabase.getDatabase(application);
        onCallItemDao = db.onCallItemDao();
        allOnCallItems = onCallItemDao.getOnCallItems();
        allActiveOnCallItems = onCallItemDao.getAllActiveItems();
        maxGroupId = onCallItemDao.getMaxGroupId();
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

    public void insertOnCallItemsAsync(List<OnCallItem> onCallItems){
        new InsertItemsAsync(onCallItemDao).execute(onCallItems.toArray(new OnCallItem[onCallItems.size()]));
    }

    private static class InsertItemsAsync extends AsyncTask<OnCallItem, Void, Void>{

        private OnCallItemDao asyncDao;

        InsertItemsAsync(OnCallItemDao dao){
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(OnCallItem... onCallItems) {

            asyncDao.insertOnCallItems(onCallItems);

            return null;
        }
    }

    public void deleteOnCallItemsAsync(List<OnCallItem> onCallItems){
        new DeleteItemsAsync(onCallItemDao).execute(onCallItems.toArray(new OnCallItem[onCallItems.size()]));
    }

    private static class DeleteItemsAsync extends AsyncTask<OnCallItem, Void, Void>{

        private OnCallItemDao asyncDao;

        DeleteItemsAsync(OnCallItemDao dao){
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(OnCallItem... onCallItems) {

            asyncDao.deleteOnCallItems(onCallItems);

            return null;
        }
    }
}
