package jordan_jefferson.com.oncallphonemanager;

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

/*
A Fragment that displays the a list of contacts. This class sets up the RecyclerView, the Adapter,
and the interface to handle onItemClickEvents
 */
public class ContactListFragment extends Fragment implements RecyclerViewItemClickListener{

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

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

        RecyclerView.Adapter adapter = new ContactListAdapter(ContactsList.getInstance(view.getContext()).getContactsList(),
                this);
        mRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = new ContactInfoFragment();

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
    public void recyclerViewItemClicked(View v, int position){

        Contact c = ContactsList.getInstance(v.getContext()).getContact(position);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = ContactInfoFragment.getInstance(position);

//        CallManagerActivity.setFabVisibility(false);

        assert fm != null;
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, fragment, FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

    }
}
