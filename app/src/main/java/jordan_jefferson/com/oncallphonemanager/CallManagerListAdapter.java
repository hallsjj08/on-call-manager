package jordan_jefferson.com.oncallphonemanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CallManagerListAdapter extends RecyclerView.Adapter<CallManagerListAdapter.ViewHolder> {



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvDay;
        public SwitchCompat dayActiveSwitch;
        public ImageView editArrow;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDay = itemView.findViewById(R.id.tvDays);
            dayActiveSwitch = itemView.findViewById(R.id.day_active_switch);
            editArrow = itemView.findViewById(R.id.edit_arrow);

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public CallManagerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CallManagerListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
