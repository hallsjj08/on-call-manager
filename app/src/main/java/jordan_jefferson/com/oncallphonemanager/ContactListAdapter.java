package jordan_jefferson.com.oncallphonemanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/*
An adapter class that displays Contacts in the recyclerview.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private ArrayList<Contact> mContacts;
    private Context mContext;
    private static RecyclerViewItemClickListener mItemListener;

    //This Viewholder class implements OnClickListener and returns the view and position of the item clicked.
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvContactName;
        public TextView tvCompanyName;
        public TextView tvDisplayNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvDisplayNumber = itemView.findViewById(R.id.tvDisplayNumber);

            itemView.setOnClickListener(this);

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            mItemListener.recyclerViewItemClicked(v, this.getAdapterPosition());

        }
    }

    ContactListAdapter(Context context, ArrayList<Contact> contacts, RecyclerViewItemClickListener itemListener){
        this.mContacts = contacts;
        this.mContext = context;
        this.mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LinearLayout contactTemplateRow = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_template_row, parent, false);

        ViewHolder view = new ViewHolder(contactTemplateRow);

        return view;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvContactName.setText(mContacts.get(position).get_contactName());
        holder.tvCompanyName.setText(mContacts.get(position).get_companyName());
        holder.tvDisplayNumber.setText(mContacts.get(position).get_contactDisplayNumber());


    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}
