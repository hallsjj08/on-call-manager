package jordan_jefferson.com.oncallphonemanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/*
A Fragment that displays the a list of contacts. This class sets up the RecyclerView, the Adapter,
and the interface to handle onItemClickEvents
 */
public class ContactListFragment extends Fragment implements RecyclerViewItemClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ContactViewModel viewModel;
    private ContactListAdapter adapter;

    private static final String FRAGMENT_TAG = "Contact";

    public static ContactListFragment getInstance(){
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
        final View view = inflater.inflate(R.layout.contact_list_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new ContactListAdapter(this);
        mRecyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        viewModel.getContacts().observe(this, new Observer<List<Contact>>() {

            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                if(contacts != null){
                    adapter.setContacts(contacts);
                }
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = new ContactInfoFragment();

                assert getActivity() != null;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                        mFragment, FRAGMENT_TAG).addToBackStack(null).commit();

            }
        });

        return view;
    }

    /*
    This method takes in the view and position of the item clicked and passes the position to
    ContactInfoFragment where the user can update or delete the contact.
     */
    @Override
    public void recyclerViewItemClicked(View v, Contact contact){

        FragmentManager fm = getFragmentManager();
        Fragment fragment = ContactInfoFragment.getInstance(contact);


        assert fm != null;
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, fragment, FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

    }
}
