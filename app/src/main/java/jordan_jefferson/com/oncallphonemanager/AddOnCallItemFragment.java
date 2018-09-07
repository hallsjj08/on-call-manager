package jordan_jefferson.com.oncallphonemanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddOnCallItemFragment extends Fragment {


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

        TextView tvCancel = view.findViewById(R.id.cancel_add_item);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

}
