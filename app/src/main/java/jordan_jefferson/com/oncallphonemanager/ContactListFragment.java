package jordan_jefferson.com.oncallphonemanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

/*
A Fragment that displays the a list of contacts. This class sets up the RecyclerView, the Adapter,
and the interface to handle onItemClickEvents
 */
public class ContactListFragment extends Fragment implements RecyclerViewItemClickListener, OnViewClosedListener{

    private ContactListAdapter adapter;
    private FloatingActionButton fab;
    private ContactViewModel viewModel;

    private static final String FRAGMENT_TAG = "CONTACT LIST";

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

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new ContactListAdapter(this);
        mRecyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation fabAnim = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
                fabAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fab.clearAnimation();
                        fab.hide();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                fab.startAnimation(fabAnim);
                int cx = (int) view.getX() + view.getWidth()/2;
                int cy = (int) view.getY() + view.getHeight()/2;
                addContactInfoFragment(cx, cy,null);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.getContacts().observe(this, new Observer<List<Contact>>() {

            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                if(contacts != null){
                    adapter.setContacts(contacts);
                }
            }
        });

    }

    /*
        This method takes in the view and position of the item clicked and passes the position to
        ContactInfoFragment where the user can update or delete the contact.
         */
    @Override
    public void recyclerViewItemClicked(View v, Contact contact){
        addContactInfoFragment((int) v.getX() + v.getWidth()/2, (int) v.getY() + v.getHeight()/2, contact);
    }

    /*
    A method that displays New/Edit Contact info.
    @param cx, the x position of the view selected.
    @param cy, the y position of the view selected.
    @param contact, the contact that was selected from the list of contacts. Will be null if fab is
    selected.
     */
    private void addContactInfoFragment(int cx, int cy, @Nullable Contact contact){

        Fragment fragment = ContactInfoFragment.getInstance(cx, cy, contact, this);

        if(getActivity() != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, fragment, FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void viewClosed() {
        if(fab.getVisibility() == View.GONE){
            Animation fabAnim = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            fabAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    fab.clearAnimation();
                    fab.show();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            fab.startAnimation(fabAnim);
        }
    }
}
