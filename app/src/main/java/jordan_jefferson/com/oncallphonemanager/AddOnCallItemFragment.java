package jordan_jefferson.com.oncallphonemanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddOnCallItemFragment extends Fragment implements View.OnClickListener {


    public AddOnCallItemFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(){
        return new AddOnCallItemFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_on_call_item, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.days_manager_recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        RepeatDaysAdapter adapter = new RepeatDaysAdapter();
        recyclerView.setAdapter(adapter);

        Button bCancel = view.findViewById(R.id.cancel_add_item);
        Button bSave = view.findViewById(R.id.add_on_call_item);

        bCancel.setOnClickListener(this);
        bSave.setOnClickListener(this);

        return view;
    }

    public void cancel(){
        getActivity().finish();
    }

    public void save(){

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_add_item:
                cancel();
                break;
            case R.id.add_on_call_item:
                save();
                break;
        }
    }
}
