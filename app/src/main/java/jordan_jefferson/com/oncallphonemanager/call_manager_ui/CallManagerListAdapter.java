package jordan_jefferson.com.oncallphonemanager.call_manager_ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jordan_jefferson.com.oncallphonemanager.R;

public class CallManagerListAdapter extends RecyclerView.Adapter<CallManagerListAdapter.ViewHolder> {

    private List<OnCallGroupItem> groupItems;
    private OnCallItemViewModel viewModel;
    private boolean clickable = false;

    public CallManagerListAdapter(OnCallItemViewModel viewModel){
        this.viewModel = viewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageButton deleteOnCallGroup;
        public TextView tvDay;
        public TextView tvDescription;
        public SwitchCompat dayActiveSwitch;
        public ImageView editArrow;

        public ViewHolder(View itemView) {
            super(itemView);

            deleteOnCallGroup = itemView.findViewById(R.id.deleteItem);
            tvDay = itemView.findViewById(R.id.tvDays);
            tvDescription = itemView.findViewById(R.id.description);
            dayActiveSwitch = itemView.findViewById(R.id.day_active_switch);
            editArrow = itemView.findViewById(R.id.edit_arrow);

        }

        @Override
        public void onClick(View v) {
            if(clickable){
                //TODO: add item click events to edit on call items.
            }
        }
    }

    @NonNull
    @Override
    public CallManagerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View onCallTemplateRow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.on_call_item_template_row, parent, false);

        return new ViewHolder(onCallTemplateRow);
    }

    @Override
    public void onBindViewHolder(@NonNull CallManagerListAdapter.ViewHolder holder, int position) {
        holder.tvDay.setText(groupItems.get(position).getTimeDescription());
        holder.tvDescription.setText(groupItems.get(position).getLabel());
        deleteOnCallGroupItems(holder.deleteOnCallGroup, position);
    }

    @Override
    public int getItemCount() {
        if(groupItems != null) return groupItems.size();
        return 0;
    }

    public void setGroupedItems(List<OnCallGroupItem> groupedItems){
        this.groupItems = groupedItems;
        notifyDataSetChanged();
    }

    private void deleteOnCallGroupItems(ImageButton deleteButton, final int position){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deletOnCallGroupItems(groupItems.get(position).getGroupId());
            }
        });
    }

    public void setClicable(boolean clickable){
        this.clickable = clickable;
    }

    //TODO: add animations to indicate that item is clickable or not
    private void setAnimationEditItemTransition(){

    }
}
