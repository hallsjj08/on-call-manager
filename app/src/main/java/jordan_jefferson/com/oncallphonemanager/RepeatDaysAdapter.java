package jordan_jefferson.com.oncallphonemanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

public class RepeatDaysAdapter extends RecyclerView.Adapter<RepeatDaysAdapter.ViewHolder>{

    String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",};

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox cbDay;

        public ViewHolder(View itemView) {
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

        holder.cbDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(getClass().getSimpleName(), holder.cbDay.getText().toString() +
                        ": Checked = " + isChecked);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return days.length;
    }
}
