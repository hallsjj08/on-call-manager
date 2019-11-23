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

/*
A Fragment that displays the a list of contacts. This class sets up the RecyclerView, the Adapter,
and the interface to handle onItemClickEvents
 */
public class ContactListFragment extends Fragment implements RecyclerViewItemClickListener<Contact>, Observer<List<Contact>> {

    private ContactListAdapter adapter;
    private ContactViewModel viewModel;
    private TextView tvNoContacts;

    public static final String FRAGMENT_TAG = "ContactListFragment";
    public static final String EXTRA_CONTACT = "extra_contact";

    public static ContactListFragment newInstance(){
        ContactListFragment contactListFragment = new ContactListFragment();

        Bundle bundle = new Bundle();
        contactListFragment.setArguments(bundle);

        return contactListFragment;
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

        adapter = new ContactListAdapter(this);
        mRecyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
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
        viewModel.getContacts().observe(this, this);
    }

    /*
    This method takes in the view and position of the item clicked and passes the position to
    ContactInfoActivity where the user can update or delete the contact.
    */
    @Override
    public void onRecyclerViewItemClicked(View v, Contact contact){
        launchContactInfoActivity(contact);
    }

    @Override
    public void onRecyclerViewItemIsEnabledChange(View v, Contact data) {
        //Ignored as there are no views to enable a contact for anything.
    }

    private void launchContactInfoActivity(@Nullable Contact contact) {
        Intent intent = new Intent();
        intent.setClass(getContext(), ContactInfoActivity.class);
        if(contact != null) intent.putExtra(EXTRA_CONTACT, contact);
        getContext().startActivity(intent);
    }

    @Override
    public void onChanged(List<Contact> contacts) {
        if (contacts != null) {
            if (contacts.isEmpty()) {
                tvNoContacts.setVisibility(View.VISIBLE);
            } else if (tvNoContacts != null) {
                tvNoContacts.setVisibility(View.GONE);
            }
            adapter.setContacts(contacts);
        }
    }
}
