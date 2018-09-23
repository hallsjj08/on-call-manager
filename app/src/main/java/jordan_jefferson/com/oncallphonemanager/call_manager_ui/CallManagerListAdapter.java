package jordan_jefferson.com.oncallphonemanager.call_manager_ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jordan_jefferson.com.oncallphonemanager.R;
import jordan_jefferson.com.oncallphonemanager.RecyclerViewItemClickListener;
import jordan_jefferson.com.oncallphonemanager.utils.RecyclerViewAnimationUtils;

public class CallManagerListAdapter extends RecyclerView.Adapter<CallManagerListAdapter.ViewHolder>{

    private List<OnCallGroupItem> groupItems;
    private OnCallItemViewModel viewModel;
    private boolean clickable;
    private RecyclerViewItemClickListener itemClickListener;

    private RecyclerViewAnimationUtils animationUtils = new RecyclerViewAnimationUtils();

    CallManagerListAdapter(OnCallItemViewModel viewModel, RecyclerViewItemClickListener itemClickListener){
        this.viewModel = viewModel;
        this.itemClickListener = itemClickListener;
        clickable = false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageButton deleteOnCallGroup;
        TextView tvStartTime;
        TextView tvEndTime;
        TextView tvDescription;
        SwitchCompat dayActiveSwitch;
        ImageView editArrow;

        ViewHolder(View itemView) {
            super(itemView);

            deleteOnCallGroup = itemView.findViewById(R.id.deleteItem);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvDescription = itemView.findViewById(R.id.description);
            dayActiveSwitch = itemView.findViewById(R.id.day_active_switch);
            editArrow = itemView.findViewById(R.id.edit_arrow);

            animationUtils.addInvisibleToVisibleViews(deleteOnCallGroup, editArrow);
            animationUtils.addVisibleViews(tvStartTime, tvEndTime, tvDescription);
            animationUtils.addVisibleToInvisibleViews(dayActiveSwitch);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickable){
                itemClickListener.recyclerViewItemClicked(v, groupItems.get(this.getAdapterPosition()));
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
        holder.tvStartTime.setText(groupItems.get(position).getStartTime());
        holder.tvEndTime.setText(groupItems.get(position).getEndTime());
        holder.tvDescription.setText(groupItems.get(position).getLabel());

        holder.dayActiveSwitch.setOnCheckedChangeListener(null);

        holder.dayActiveSwitch.setChecked(groupItems.get(position).isActive());

        holder.dayActiveSwitch.setOnCheckedChangeListener((view, isChecked) -> {
            groupItems.get(position).setActive(isChecked);
            viewModel.insertOnCallItems(groupItems.get(position).getOnCallItems());
        });

        deleteOnCallGroupItems(holder.deleteOnCallGroup, position);
        checkClickable(clickable, holder);
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
        deleteButton.setOnClickListener(v -> {
            viewModel.deletOnCallGroupItems(groupItems.get(position).getGroupId());
            notifyItemRemoved(position);
            //this line below gives you the animation and also updates the
            //list items after the deleted item
            notifyItemRangeChanged(position, getItemCount());

        });
    }

    public boolean isClickable(){return clickable;}

    public void setClickable(boolean clickable){
        this.clickable = clickable;

        if(clickable){
            animationUtils.startAnimation();
        }else {
            animationUtils.reverseAnimation();
        }
    }

    private void checkClickable(boolean clickable, ViewHolder holder){
        if(clickable){
            holder.tvStartTime.setTranslationX(42);
            holder.tvEndTime.setTranslationX(42);
            holder.tvDescription.setTranslationX(42);
            holder.deleteOnCallGroup.setTranslationX(16);
            holder.editArrow.setTranslationX(16);
            holder.deleteOnCallGroup.setVisibility(View.VISIBLE);
            holder.editArrow.setVisibility(View.VISIBLE);
            holder.dayActiveSwitch.setVisibility(View.GONE);
        }
    }
}
