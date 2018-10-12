package jordan_jefferson.com.oncallphonemanager.callmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import jordan_jefferson.com.oncallphonemanager.R;

public class RepeatDaysAdapter extends RecyclerView.Adapter<RepeatDaysAdapter.ViewHolder>{

    public interface DayCheckListener{
        void onCheckedChangedListener(String day, boolean isChecked);
    }

    private String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private List<String> repeatedDays;
    private DayCheckListener dayCheckCallback;

    RepeatDaysAdapter(DayCheckListener dayCheckListener){
        this.dayCheckCallback = dayCheckListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox cbDay;

        ViewHolder(View itemView) {
            super(itemView);
            cbDay = itemView.findViewById(R.id.dayCheckBox);
        }
    }

    @NonNull
    @Override
    public RepeatDaysAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View repeatDayTemplateRow = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_row_repeat_days, parent, false);

        return new ViewHolder(repeatDayTemplateRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final RepeatDaysAdapter.ViewHolder holder, int position) {
        holder.cbDay.setText(days[position]);

        holder.cbDay.setOnCheckedChangeListener(null);

        if(repeatedDays != null && !repeatedDays.isEmpty()){
            checkRepeatedDays(holder.cbDay);
        }

        holder.cbDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(getClass().getSimpleName(), holder.cbDay.getText().toString() +
                        ": Checked = " + isChecked);

                dayCheckCallback.onCheckedChangedListener(holder.cbDay.getText().toString(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    public void setRepeatedDays(List<String> repeatedDays){
        this.repeatedDays = repeatedDays;
        notifyDataSetChanged();
    }

    private void checkRepeatedDays(CompoundButton dayCheckBox){
        for(String day : repeatedDays){
            if(dayCheckBox.getText().toString().equals(day)){
                dayCheckBox.setChecked(true);
                break;
            }
        }
    }
}
