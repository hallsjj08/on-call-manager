package jordan_jefferson.com.oncallphonemanager;


import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallManagerFragment extends Fragment implements View.OnClickListener {


    public CallManagerFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(){
        return new CallManagerFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_manager, container, false);

        ImageButton addItemButton = view.findViewById(R.id.add_on_call_item);

        addItemButton.setOnClickListener(this);

        OnCallItemViewModel viewModel = ViewModelProviders.of(this).get(OnCallItemViewModel.class);

        viewModel.getAllOnCallItems().observe(this, new Observer<List<OnCallItem>>() {
            @Override
            public void onChanged(@Nullable List<OnCallItem> onCallItems) {
                if(onCallItems != null && !onCallItems.isEmpty()){
                    Log.d(getClass().getSimpleName(), onCallItems.size() + "");
                }
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
        Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_up,
                R.anim.no_change).toBundle();
        startActivity(intent, bundle);
    }
}
