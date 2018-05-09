package jordan_jefferson.com.oncallphonemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactListFragment extends Fragment implements RecyclerViewItemClickListener{

    RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.contact_list_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ContactListAdapter(view.getContext(),
                ContactsList.getInstance(view.getContext()).getContactsList(),
                this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void recyclerViewItemClicked(View v, int position){

        Contact c = ContactsList.getInstance(v.getContext()).getContact(position);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = new ContactInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("contact", c);

        fragment.setArguments(bundle);

        CallManagerActivity.setFabVisibility(false);

        fm.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();

    }
}
