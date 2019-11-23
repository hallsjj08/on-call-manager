package jordan_jefferson.com.oncallphonemanager;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class WhitelistAlarmInfoActivity extends AppCompatActivity implements View.OnClickListener,
        OnTimeUpdatedListener, TimePickerDialog.OnTimeSetListener, TextWatcher {

    AlarmScheduleViewModel viewModel;
    AlarmSchedule schedule;
    EditText etName;
    Button bStartTime;
    Button bEndTime;
    Button[] days;
    boolean[] daysSelected;
    boolean infoUpdated = false;
    int selectedTimeId = 0;

    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist_alarm_info);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra(AlarmScheduleListFragment.EXTRA_ALARM_SCHEDULE)) {
            schedule = getIntent().getParcelableExtra(AlarmScheduleListFragment.EXTRA_ALARM_SCHEDULE);
        }

        if(schedule == null) {
            infoUpdated = true;
            schedule = new AlarmSchedule();
        }

        etName = findViewById(R.id.schedule_name);
        if(schedule.getName() != null) etName.setText(schedule.getName());
        etName.addTextChangedListener(this);
        bStartTime = findViewById(R.id.start_time);
        bStartTime.setOnClickListener(this);
        bEndTime = findViewById(R.id.end_time);
        bEndTime.setOnClickListener(this);
        timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Dialog,
                this, schedule.getStartHour(), schedule.getStartMinute(), false);

        Button bDone = findViewById(R.id.button_alarm_schedule_done);
        viewModel = ViewModelProviders.of(this).get(AlarmScheduleViewModel.class);
        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < daysSelected.length; i++) {
                    daysSelected[i] = days[i].isSelected();
                }

                schedule.setEnabled(infoUpdated);
                schedule.setName(etName.getText().toString());
                viewModel.insertAlarmSchedule(schedule);
                finish();
            }
        });

        schedule.setOnTimeUpdateListener(this);

        bStartTime.setText(schedule.getStartTimeString(DateFormat.is24HourFormat(this)));
        bEndTime.setText(schedule.getEndTimeString(DateFormat.is24HourFormat(this)));

        days = new Button[] {findViewById(R.id.sunday),
                findViewById(R.id.monday),
                findViewById(R.id.tuesday),
                findViewById(R.id.wednesday),
                findViewById(R.id.thursday),
                findViewById(R.id.friday),
                findViewById(R.id.saturday)};

        daysSelected = schedule.getRepeatDays();
        for(int i = 0; i < daysSelected.length; i++) {
            days[i].setOnClickListener(this);
            days[i].setSelected(daysSelected[i]);
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        infoUpdated = true;

        switch (id) {
            case R.id.start_time:
                selectedTimeId = id;
                timePickerDialog.updateTime(schedule.getStartHour(), schedule.getStartMinute());
                timePickerDialog.show();
                break;
            case R.id.end_time:
                selectedTimeId = id;
                timePickerDialog.updateTime(schedule.getEndHour(), schedule.getEndMinute());
                timePickerDialog.show();
                break;
            default:
                v.setSelected(!v.isSelected());
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_info_menu, menu);
        return getIntent().hasExtra(AlarmScheduleListFragment.EXTRA_ALARM_SCHEDULE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.remove_contact:
                viewModel.deleteAlarmSchedule(schedule);
                hideKeyboard();
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onStartTimeUpdated(String startTime) {
        bStartTime.setText(startTime);
    }

    @Override
    public void onEndTimeUpdated(String endTime) {
        bEndTime.setText(endTime);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        switch (selectedTimeId) {
            case R.id.start_time:
                schedule.setStartTime(hourOfDay, minute);
                break;
            case R.id.end_time:
                schedule.setEndTime(hourOfDay, minute);
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        infoUpdated = true;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
