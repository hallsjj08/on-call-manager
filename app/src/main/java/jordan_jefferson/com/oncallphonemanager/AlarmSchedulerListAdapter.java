package jordan_jefferson.com.oncallphonemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlarmSchedulerListAdapter extends RecyclerView.Adapter<AlarmSchedulerListAdapter.ViewHolder> {

    private List<AlarmSchedule> alarmSchedules;
    private RecyclerViewItemClickListener mItemListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            CompoundButton.OnCheckedChangeListener {
        public TextView tvName;
        public TextView tvStartTime;
        public TextView tvEndTime;
        public Switch enabled;
        public TextView[] days;

        ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvScheduleName);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            enabled = itemView.findViewById(R.id.switch_alarm_enabled);
            days = new TextView[] { itemView.findViewById(R.id.tvSunday),
                    itemView.findViewById(R.id.tvMonday),
                    itemView.findViewById(R.id.tvTuesday),
                    itemView.findViewById(R.id.tvWednesday),
                    itemView.findViewById(R.id.tvThursday),
                    itemView.findViewById(R.id.tvFriday),
                    itemView.findViewById(R.id.tvSaturday) };

            enabled.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            mItemListener.onRecyclerViewItemClicked(v, alarmSchedules.get(this.getAdapterPosition()));

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AlarmSchedule schedule = alarmSchedules.get(this.getAdapterPosition());
            schedule.setEnabled(isChecked);
            mItemListener.onRecyclerViewItemIsEnabledChange(buttonView, schedule);
        }
    }

    AlarmSchedulerListAdapter(RecyclerViewItemClickListener itemListener) {
        mItemListener = itemListener;
    }

    @NonNull
    @Override
    public AlarmSchedulerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View contactTemplateRow = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_schedule_template_row, parent, false);

        return new AlarmSchedulerListAdapter.ViewHolder(contactTemplateRow);
    }


    @Override
    public void onBindViewHolder(@NonNull AlarmSchedulerListAdapter.ViewHolder holder, int position) {

        AlarmSchedule alarmSchedule = alarmSchedules.get(position);

        holder.tvName.setText(alarmSchedule.getName());
        holder.tvStartTime.setText(alarmSchedule.getStartTimeString(false));
        holder.tvEndTime.setText(alarmSchedule.getEndTimeString(false));
        holder.enabled.setChecked(alarmSchedule.isEnabled());

        boolean[] selectedDays = alarmSchedule.getRepeatDays();
        for(int i = 0; i < selectedDays.length; i++) {
            holder.days[i].setSelected(selectedDays[i]);
        }


    }

    public void setAlarms(List<AlarmSchedule> alarmSchedules) {
        this.alarmSchedules = alarmSchedules;
        notifyDataSetChanged();
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (alarmSchedules != null) {
            return alarmSchedules.size();
        }
        return 0;
    }

}
