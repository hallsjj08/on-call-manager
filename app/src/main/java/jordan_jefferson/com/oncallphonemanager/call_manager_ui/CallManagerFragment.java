package jordan_jefferson.com.oncallphonemanager.call_manager_ui;


import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jordan_jefferson.com.oncallphonemanager.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallManagerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CALL_MANAGER_FRAGMENT";
    private Subscription subscription;
    private int groupId = 1;

    public CallManagerFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(){
        return new CallManagerFragment();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_manager, container, false);

        ImageButton addItemButton = view.findViewById(R.id.add_on_call_item);

        addItemButton.setOnClickListener(this);

        OnCallItemViewModel viewModel = ViewModelProviders.of(this).get(OnCallItemViewModel.class);

        viewModel.getMaxGroupId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if(integer != null){
                    groupId = integer + 1;
                }
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.days_manager_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(lm);

        final CallManagerListAdapter adapter = new CallManagerListAdapter(viewModel);
        recyclerView.setAdapter(adapter);

        viewModel.getAllOnCallItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OnCallGroupItem>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(List<OnCallGroupItem> groupedItems) {
                        Log.d(getClass().getSimpleName(), groupedItems.toString());
                        adapter.setGroupedItems(groupedItems);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_on_call_item:
                launchAddOnCallItemActivity();
        }
    }

    private void launchAddOnCallItemActivity(){
        Intent intent = new Intent(getActivity(), AddOnCallItemActivity.class);
        intent.putExtra("GroupId", groupId);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_up,
                R.anim.no_change).toBundle();
        startActivity(intent, bundle);
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.cancel();
    }
}
