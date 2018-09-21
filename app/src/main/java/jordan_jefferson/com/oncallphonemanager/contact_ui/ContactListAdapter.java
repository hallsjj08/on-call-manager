package jordan_jefferson.com.oncallphonemanager.contact_ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jordan_jefferson.com.oncallphonemanager.R;
import jordan_jefferson.com.oncallphonemanager.RecyclerViewItemClickListener;
import jordan_jefferson.com.oncallphonemanager.data.Contact;

/*
An adapter class that displays Contacts in the recyclerview.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private List<Contact> mContacts;
    private RecyclerViewItemClickListener mItemListener;

    //This Viewholder class implements OnClickListener and returns the view and position of the item clicked.
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

        if(!mContacts.get(position).get_contactName().isEmpty()){
            holder.tvInitial.setText(mContacts.get(position).get_contactName().charAt(0) + "");
            holder.tvContactName.setText(mContacts.get(position).get_contactName());
        }else if(!mContacts.get(position).get_companyName().isEmpty()){
            holder.tvInitial.setText(mContacts.get(position).get_companyName().charAt(0));
            holder.tvContactName.setText(mContacts.get(position).get_companyName());
        }else{
            holder.tvInitial.setText("#");
            holder.tvContactName.setText(mContacts.get(position).get_contactDisplayNumber());
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
