package jordan_jefferson.com.oncallphonemanager;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallManagerFragment extends Fragment {


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

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .addToBackStack(null)
//                        .add(R.id.fragmentContainer, AddOnCallItemFragment.newInstance())
//                        .commit();

                Intent intent = new Intent(getActivity(), AddOnCallItemActivity.class);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_up, R.anim.no_change).toBundle();
                startActivity(intent, bundle);
            }
        });

        return view;
    }

}
