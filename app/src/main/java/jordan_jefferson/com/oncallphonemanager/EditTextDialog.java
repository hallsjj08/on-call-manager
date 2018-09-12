package jordan_jefferson.com.oncallphonemanager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class EditTextDialog extends DialogFragment implements View.OnClickListener {

    public interface EditTextListener{
        void onEditTextSet(String label);
    }

    private static final String TITLE_KEY = "title";
    private EditTextListener listener;
    private EditText editTextLabel;

    public static DialogFragment newInstance(@NonNull String title){
        EditTextDialog editTextDialog = new EditTextDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEY, title);
        editTextDialog.setArguments(bundle);
        return editTextDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (EditTextListener) getTargetFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_text_view, container, false);

        init(view);

        return view;
    }

    private void init(View view) {

        Button bSubmitLabel = view.findViewById(R.id.bSubmitLabel);
        Button bCancelLabel = view.findViewById(R.id.bCancelLabel);
        editTextLabel = view.findViewById(R.id.editTextLabel);

        bSubmitLabel.setOnClickListener(this);
        bCancelLabel.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TITLE_KEY, getArguments().getString(TITLE_KEY));

        getDialog().setTitle(getArguments().getString(TITLE_KEY));
        getDialog().setCancelable(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bSubmitLabel:
                listener.onEditTextSet(editTextLabel.getText().toString());
                dismiss();
                break;
            case R.id.bCancelLabel:
                dismiss();
                break;
        }
    }
}
