package jordan_jefferson.com.oncallphonemanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AlarmScheduleViewModel extends AndroidViewModel {

    private AlarmScheduleRepository alarmScheduleRepository;
    private LiveData<List<AlarmSchedule>> alarmSchedules;

    public AlarmScheduleViewModel(@NonNull Application application) {
        super(application);
        alarmScheduleRepository = new AlarmScheduleRepository(application);
        alarmSchedules = alarmScheduleRepository.getAllAlarmSchedules();
    }

    public LiveData<List<AlarmSchedule>> getAlarmSchedules() {
        return alarmSchedules;
    }

    public void insertAlarmSchedule(AlarmSchedule alarmSchedule) {
        alarmScheduleRepository.insert(alarmSchedule);
    }

    public void deleteAlarmSchedule(AlarmSchedule alarmSchedule) {
        alarmScheduleRepository.delete(alarmSchedule);
    }

}
