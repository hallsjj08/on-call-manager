package jordan_jefferson.com.oncallphonemanager;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AlarmScheduleRepository {

    private AlarmScheduleDao alarmScheduleDao;
    private LiveData<List<AlarmSchedule>> allAlarmSchedules;

    public AlarmScheduleRepository(Application application){
        ContactDatabase db = ContactDatabase.getDatabase(application);
        alarmScheduleDao = db.alarmScheduleDao();
        allAlarmSchedules = alarmScheduleDao.getAlarmSchedues();
    }

    public LiveData<List<AlarmSchedule>> getAllAlarmSchedules() {return allAlarmSchedules;}

    public void insert(AlarmSchedule alarmSchedule) {new insertAsyncTask(alarmScheduleDao).execute(alarmSchedule);}

    private static class insertAsyncTask extends AsyncTask<AlarmSchedule, Void, Void> {

        private AlarmScheduleDao asyncTaskDao;

        insertAsyncTask(AlarmScheduleDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(AlarmSchedule... alarmSchedules) {

            asyncTaskDao.insertContact(alarmSchedules[0]);
            return null;

        }
    }

    public void delete(AlarmSchedule alarmSchedule) {new deleteAsyncTask(alarmScheduleDao).execute(alarmSchedule);}

    private static class deleteAsyncTask extends AsyncTask<AlarmSchedule, Void, Void>{

        private AlarmScheduleDao asyncTaskDao;

        deleteAsyncTask(AlarmScheduleDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(AlarmSchedule... alarmSchedules) {

            asyncTaskDao.delete(alarmSchedules[0]);
            return null;
        }
    }

}
