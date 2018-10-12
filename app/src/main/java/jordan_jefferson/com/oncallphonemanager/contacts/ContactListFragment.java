package jordan_jefferson.com.oncallphonemanager.contacts;

import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import jordan_jefferson.com.oncallphonemanager.R;
import jordan_jefferson.com.oncallphonemanager.RecyclerViewItemClickListener;
import jordan_jefferson.com.oncallphonemanager.data.Contact;

/*
A Fragment that displays the a list of contacts. This class sets up the RecyclerView, the Adapter,
and the interface to handle onItemClickEvents
 */
public class ContactListFragment extends Fragment implements RecyclerViewItemClickListener {

    private ContactListAdapter adapter;
    private ContactViewModel viewModel;

    private static final String FRAGMENT_TAG = "CONTACT LIST";

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

        View view1 = inflater.inflate(R.layout.fragmen_contact_listt, container, false);

        RecyclerView mRecyclerView = view1.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view1.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new ContactListAdapter(this);
        mRecyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        ImageButton addContactButton = view1.findViewById(R.id.add_contact);
        addContactButton.setOnClickListener(view -> addContactInfo(null));
        return view1;
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.getContacts().observe(this, contacts -> {
            if(contacts != null){
                adapter.setContacts(contacts);
            }
        });

    }

    /*
    This method takes in the view and position of the item clicked and passes the position to
    ContactInfoFragment where the user can update or delete the contact.
    */
    @Override
    public void recyclerViewItemClicked(View v, Object object){
        if(object instanceof Contact){
            addContactInfo((Contact) object);
        }
    }

    /*
    A method that displays New/Edit Contact info.
    @param contact, the contact that was selected from the list of contacts. Will be null if addContactButton is
    selected.
     */
    private void addContactInfo(@Nullable Contact contact){
        Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
        intent.putExtra("CONTACT", contact);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_up,
                R.anim.no_change).toBundle();
        startActivity(intent, bundle);
    }
}
