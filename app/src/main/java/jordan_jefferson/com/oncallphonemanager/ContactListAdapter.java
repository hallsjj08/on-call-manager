package jordan_jefferson.com.oncallphonemanager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private List<Contact> mContacts;
    private RecyclerViewItemClickListener mItemListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvInitial;
        public TextView tvContactName;

        ViewHolder(View itemView) {
            super(itemView);

            tvInitial = itemView.findViewById(R.id.circle_text);
            tvContactName = itemView.findViewById(R.id.tvContactName);

            itemView.setOnClickListener(this);

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            mItemListener.recyclerViewItemClicked(v, mContacts.get(this.getAdapterPosition()));

        }
    }

    ContactListAdapter(RecyclerViewItemClickListener itemListener){
        mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View contactTemplateRow = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_template_row, parent, false);

        return new ViewHolder(contactTemplateRow);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Contact contact = mContacts.get(position);

        if(!TextUtils.isEmpty(contact.get_contactName())){
            holder.tvInitial.setText(contact.get_contactName().substring(0,1));
            holder.tvContactName.setText(mContacts.get(position).get_contactName());
        }else if(!TextUtils.isEmpty(contact.get_companyName())){
            holder.tvInitial.setText(contact.get_companyName().substring(0,1));
            holder.tvContactName.setText(mContacts.get(position).get_companyName());
        }else if(!TextUtils.isEmpty(contact.get_contactDisplayNumber())){
            holder.tvInitial.setText("#");
            holder.tvContactName.setText(contact.get_contactDisplayNumber());
        }
    }

    public void setContacts(List<Contact> contacts){
        mContacts = contacts;
        notifyDataSetChanged();
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if(mContacts != null){
            return mContacts.size();
        }
        return 0;
    }
}
