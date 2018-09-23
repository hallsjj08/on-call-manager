package jordan_jefferson.com.oncallphonemanager.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import io.reactivex.Flowable;

public class OnCallItemRepository {

    private OnCallItemDao onCallItemDao;
    private Flowable<List<OnCallItem>> allOnCallItems;
    private Flowable<List<OnCallItem>> allActiveOnCallItems;
    private LiveData<Integer> maxGroupId;

    public OnCallItemRepository(Application application) {
        ContactDatabase db = ContactDatabase.getDatabase(application);
        onCallItemDao = db.onCallItemDao();
        allOnCallItems = onCallItemDao.getOnCallItems();
        allActiveOnCallItems = onCallItemDao.getAllActiveItems();
    }

    public Flowable<List<OnCallItem>> getAllOnCallItems() {
        return allOnCallItems;
    }

    public Flowable<List<OnCallItem>> getAllActiveOnCallItems() {
        return allActiveOnCallItems;
    }

    public void insertOnCallItemsAsync(List<OnCallItem> onCallItems){
        Log.d("OnCallRepository", "Insert Data Called");
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

    public void deletGroupItemsAsync(int groupId){
        new DeleteGroupAsync(onCallItemDao).execute(groupId);
    }

    private static class DeleteGroupAsync extends AsyncTask<Integer, Void, Void>{

        private OnCallItemDao asyncDao;

        DeleteGroupAsync(OnCallItemDao dao){
            this.asyncDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {

            asyncDao.deleteGroupedItems(integers[0]);

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

    public void clearAllItmes(){
        new ClearAllItemsAsync(onCallItemDao).execute();
    }

    private static class ClearAllItemsAsync extends AsyncTask<Void, Void, Void>{

        OnCallItemDao asyncDao;

        ClearAllItemsAsync(OnCallItemDao dao){
            this.asyncDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            asyncDao.clearAll();

            return null;
        }
    }
}
