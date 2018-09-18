package jordan_jefferson.com.oncallphonemanager.call_manager_ui;


import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;

import jordan_jefferson.com.oncallphonemanager.data.OnCallItem;
import jordan_jefferson.com.oncallphonemanager.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddOnCallItemFragment extends Fragment implements View.OnClickListener,
        RepeatDaysAdapter.DayCheckListener, EditTextDialog.EditTextListener {

    private LinkedHashSet<String> repeatDays = new LinkedHashSet<>();
    private final String allDayStartTime = "12:00 AM";
    private final String allDayEndTime = "11:59 PM";

    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int groupId;

    private String label;

    private Button bLabel;

    public AddOnCallItemFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(){
        return new AddOnCallItemFragment();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupId = getArguments().getInt("GroupId");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_on_call_item, container, false);

        initDaysRecyclerView(view);

        initButtons(view);

        return view;
    }

    private void initDaysRecyclerView(@NonNull View view){
        RecyclerView recyclerView = view.findViewById(R.id.days_manager_recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        RepeatDaysAdapter adapter = new RepeatDaysAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void initButtons(@NonNull View view){
        Button bCancel = view.findViewById(R.id.cancel_add_item);
        Button bSave = view.findViewById(R.id.add_on_call_item);
        bLabel = view.findViewById(R.id.buttonLabel);
        SwitchCompat allDaySwitch = view.findViewById(R.id.allDaySwitch);
        Button startTime = view.findViewById(R.id.buttonStartTime);
        Button endTime = view.findViewById(R.id.buttonEndTime);

        bCancel.setOnClickListener(this);
        bSave.setOnClickListener(this);
        bLabel.setOnClickListener(this);
        allDaySwitchListener(allDaySwitch, startTime, endTime);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_add_item:
                cancel();
                break;
            case R.id.add_on_call_item:
                save();
                break;
            case R.id.buttonLabel:
                showEditLabelDialog(v);
                break;
            case R.id.buttonStartTime:
                showTimePicker("Start Time", v);
                break;
            case R.id.buttonEndTime:
                showTimePicker("End Time", v);
                break;
        }
    }

    private void cancel(){
        if(getActivity() != null) getActivity().finish();
    }

    private void save(){

        OnCallItemViewModel viewModel = ViewModelProviders.of(this).get(OnCallItemViewModel.class);
        List<OnCallItem> onCallItems = new ArrayList<>();

        if(!repeatDays.isEmpty()){
            for (String repeatDay : repeatDays) {
                onCallItems.add(new OnCallItem(repeatDay, true, true, startHour, startMinute,
                        endHour, endMinute, label, groupId));
            }
        }else{
            onCallItems.add(new OnCallItem(null, true, true, startHour, startMinute,
                    endHour, endMinute, label, groupId));
        }

        viewModel.insertOnCallItems(onCallItems);

        getActivity().finish();

    }

    private void showEditLabelDialog(View v){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        DialogFragment dialogFragment = EditTextDialog.newInstance("Set Label");
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(ft, "dialog");
    }

    private void showTimePicker(@NonNull String title, @NonNull View v){

        final Button timeButton = (Button) v;

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                switch (timeButton.getId()){
                    case R.id.buttonStartTime:
                        startHour = hourOfDay;
                        startMinute = minute;
                        break;
                    case R.id.buttonEndTime:
                        endHour = hourOfDay;
                        endMinute = minute;
                        break;

                }

                String displayTime;
                String displayMinute;

                if(minute < 10) {
                    displayMinute = "0" + minute;
                }else{
                    displayMinute = minute + "";
                }

                if(hourOfDay > 12){
                    displayTime = (hourOfDay - 12) + ":" + displayMinute + " PM";
                }else{
                    displayTime = hourOfDay + ":" + displayMinute + " AM";
                }

                timeButton.setText(displayTime);
            }
        }, currentHour, currentMinute, false);
        timePicker.setTitle(title);
        timePicker.show();
    }

    private void allDaySwitchListener(SwitchCompat allDaySwitch, final Button startTime, final Button endTime){
        allDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startTime.setText(allDayStartTime);
                    startTime.setEnabled(false);
                    endTime.setText(allDayEndTime);
                    endTime.setEnabled(false);
                    startHour = 0;
                    startMinute = 0;
                    endHour = 23;
                    endMinute = 59;
                }else{
                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onCheckedChangedListener(String day, boolean isChecked) {
        if(isChecked){
            repeatDays.add(day);
        }else{
            repeatDays.remove(day);
        }

        Log.d(getClass().getSimpleName(), repeatDays.toString());
    }

    @Override
    public void onEditTextSet(String label) {
        this.label = label;
        bLabel.setText(label);
    }
}
