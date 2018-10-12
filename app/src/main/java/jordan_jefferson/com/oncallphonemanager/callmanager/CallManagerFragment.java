package jordan_jefferson.com.oncallphonemanager.callmanager;


import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
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
    private CallManagerListAdapter adapter;
    private OnCallItemViewModel viewModel;

    private Button editItemButton;
    private Button doneEditingButton;

    public CallManagerFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(){
        return new CallManagerFragment();
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_manager, container, false);

        ImageButton addItemButton = view.findViewById(R.id.add_on_call_item);
        editItemButton = view.findViewById(R.id.edit);
        doneEditingButton = view.findViewById(R.id.done);

        addItemButton.setOnClickListener(this);
        editItemButton.setOnClickListener(this);
        doneEditingButton.setOnClickListener(this);

        viewModel = ViewModelProviders.of(this).get(OnCallItemViewModel.class);

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

                        if(!groupedItems.isEmpty()) {
                            groupId = groupedItems.get(0).getGroupId() + 1;
                            if(!adapter.isClickable() && editItemButton.getVisibility() != View.VISIBLE){
                                viewFadeInOut(null, editItemButton);
                            }
                        }else{
                            groupId = 1;
                            if(editItemButton.getVisibility() == View.VISIBLE){
                                viewFadeInOut(editItemButton, null);
                            }else {
                                viewFadeInOut(doneEditingButton, null);
                                adapter.setClickable(false);
                            }
                        }
                        adapter.setGroupedItems(groupedItems);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        Log.e(TAG, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        RecyclerView recyclerView = view.findViewById(R.id.days_manager_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(lm);

        adapter = new CallManagerListAdapter(viewModel,
                (v, onCallGroupItem) -> launchAddOnCallItemActivity((OnCallGroupItem) onCallGroupItem));

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_on_call_item:
                launchAddOnCallItemActivity(null);
                break;
            case  R.id.edit:
                adapter.setClickable(true);
                viewFadeInOut(editItemButton, doneEditingButton);
                break;
            case R.id.done:
                adapter.setClickable(false);
                viewFadeInOut(doneEditingButton, editItemButton);
                break;
        }
    }

    private void launchAddOnCallItemActivity(@Nullable OnCallGroupItem onCallGroupItem){
        Intent intent = new Intent(getActivity(), AddOnCallItemActivity.class);
        intent.putExtra("GroupId", groupId);
        intent.putExtra("GroupItem", onCallGroupItem);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_up,
                R.anim.no_change).toBundle();
        startActivity(intent, bundle);
    }

    private void viewFadeInOut(final View viewToFadeOut, final View viewToFadeIn) {

        if(viewToFadeOut != null){
            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setDuration(250);
            viewToFadeOut.setVisibility(View.GONE);
            viewToFadeOut.startAnimation(fadeOut);
        }

        if(viewToFadeIn != null){
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setDuration(250);
            fadeIn.setStartOffset(250);
            viewToFadeIn.setVisibility(View.VISIBLE);
            viewToFadeIn.startAnimation(fadeIn);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(subscription != null){
            subscription.cancel();
        }
    }
}
