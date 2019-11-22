package jordan_jefferson.com.oncallphonemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AlarmScheduleListFragment extends Fragment implements RecyclerViewItemClickListener<AlarmSchedule>, Observer<List<AlarmSchedule>> {

    private AlarmSchedulerListAdapter adapter;
    private AlarmScheduleViewModel viewModel;
    private TextView tvNoContacts;

    public static final String FRAGMENT_TAG = "ContactListFragment";
    public static final String EXTRA_ALARM_SCHEDULE = "EXTRA_ALARM_SCHEDULE";

    public static AlarmScheduleListFragment newInstance(){
        AlarmScheduleListFragment fragment = new AlarmScheduleListFragment();

        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        adapter = new AlarmSchedulerListAdapter(this);
        mRecyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(AlarmScheduleViewModel.class);
        tvNoContacts = view.findViewById(R.id.no_contacts);

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchContactInfoActivity(null);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getAlarmSchedules().observe(this, this);
    }

    /*
    This method takes in the view and position of the item clicked and passes the position to
    ContactInfoActivity where the user can update or delete the contact.
    */
    @Override
    public void recyclerViewItemClicked(View v, AlarmSchedule alarmSchedule){
        launchContactInfoActivity(alarmSchedule);
    }

    private void launchContactInfoActivity(@Nullable AlarmSchedule alarmSchedule) {
        Intent intent = new Intent();
        intent.setClass(getContext(), WhitelistAlarmInfoActivity.class);
        if(alarmSchedule != null) intent.putExtra(EXTRA_ALARM_SCHEDULE, alarmSchedule);
        getContext().startActivity(intent);
    }

    @Override
    public void onChanged(List<AlarmSchedule> alarmSchedules) {
        if (alarmSchedules != null) {
            if (alarmSchedules.isEmpty()) {
                tvNoContacts.setVisibility(View.VISIBLE);
            } else if (tvNoContacts != null) {
                tvNoContacts.setVisibility(View.GONE);
            }
            adapter.setAlarms(alarmSchedules);
        }
    }
}
