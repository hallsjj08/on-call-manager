package jordan_jefferson.com.oncallphonemanager;

import android.animation.Animator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/*
ContactInfoFragment is a UI that lets the user either create a new contact for their contact list,
update an existing contact, or delete a contact.
 */
public class ContactInfoFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {

    private Contact contact;
    private View view;
    private int cx;
    private int cy;
    private boolean numberFormatted = false;
    private static final String CONTACT_KEY = "position";
    private static final String VIEW_CENTER_X = "centerX";
    private static final String VIEW_CENTER_Y = "centerY";
    private static final String TAG = "CONTACT INFO";

    private ContactViewModel viewModel;
    private static OnViewClosedListener onViewClosedListener;

    /*
    Empty public constructor for Fragment
     */
    public ContactInfoFragment(){ }

    /*
    A method to create a new instance of ContactInfoInfoFragment
    @param cx, x position of view that launched newInstance to use in circleReveal/Hide
    @param cy, y position of view that launched newInstance to use in circleReveal/Hide
    @param contact, the contact selected from the users contact list
    @param listener, the listener that is listening for the fragment to be removed.
     */
    public static Fragment newInstance(int cx, int cy, @Nullable Contact contact, OnViewClosedListener listener){
        ContactInfoFragment contactInfoFragment = new ContactInfoFragment();
        onViewClosedListener = listener;

        Bundle b = new Bundle();
        b.putInt(VIEW_CENTER_X, cx);
        b.putInt(VIEW_CENTER_Y, cy);
        b.putSerializable(CONTACT_KEY, contact);
        contactInfoFragment.setArguments(b);

        return contactInfoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        if(getArguments() != null){
            this.cx = getArguments().getInt(VIEW_CENTER_X);
            this.cy = getArguments().getInt(VIEW_CENTER_Y);
            this.contact = (Contact) getArguments().getSerializable(CONTACT_KEY);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_contact_info, container, false);

        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(this);

        Button bSubmit = view.findViewById(R.id.bSubmit);
        ImageButton bCancel = view.findViewById(R.id.bCancel);
        Button bDelete = view.findViewById(R.id.bDelete);


        TextView title = view.findViewById(R.id.coantact_card_title);
        final TextInputLayout inputLayout = view.findViewById(R.id.textInputLayout3);
        final EditText etPhone = view.findViewById(R.id.edPhone);
        final EditText etContactName = view.findViewById(R.id.edContactName);
        final EditText etCompanyName = view.findViewById(R.id.edCompanyName);

        if(contact != null){
            title.setText(R.string.edit_contact);
            etContactName.setText(contact.get_contactName());
            etCompanyName.setText(contact.get_companyName());
            etPhone.setText(contact.get_contactDisplayNumber());
            bSubmit.setText(R.string.update);
            bDelete.setVisibility(View.VISIBLE);
            numberFormatted = true;
        }else{
            etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                /**
                 * Called when the focus state of a view has changed.
                 *
                 * @param v        The view whose state has changed.
                 * @param hasFocus The new focus state of v.
                 */
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    etPhone.setHint("Ex. (123) 456-####");
                    inputLayout.setError("Replace unknown digits with \"#\".");
                }
            });
        }

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(etPhone.getText().toString().length() == 10){
                    inputLayout.setErrorEnabled(false);
                    numberFormatted = true;
                }else{
                    inputLayout.setErrorEnabled(true);
                    inputLayout.setError("Replace unknown digits with \"#\".");
                    numberFormatted = false;
                }
            }
        });

        //A button that updates an existing contact or adds a new one to the list.
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contactName = etContactName.getText().toString();
                String companyName = etCompanyName.getText().toString();
                String number = etPhone.getText().toString();
                number = number.trim();
                String regexNumber = number.replaceAll("#", "\\\\d");

                if(contact != null){
                    contact.set_contactName(contactName);
                    contact.set_companyName(companyName);
                    contact.set_contactDisplayNumber(number);
                    contact.set_contactRegexNumber(regexNumber);
                }else {
                    contact = new Contact(contactName, companyName, number, regexNumber);
                }

                if(numberFormatted){
                    circleHide(view);
                    viewModel.insert(contact);
                }else{
                    Snackbar.make(v, "Please enter a valid phone number.", Snackbar.LENGTH_LONG)
                    .show();
                }

                hideKeyboard(v);
            }
        });

        //A Cancel button that returns to the previous fragment state.
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                circleHide(view);
            }
        });

        //A button that only shows if a user is updating a contact, giving them the option to delete.
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.delete(contact);
                hideKeyboard(v);
                circleHide(view);
            }
        });
        return view;
    }

    //Removes this instance of the fragment from the stack
    private void removeFragment(){
        if(getActivity() != null){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    //Hides the keyboard if shown.
    private void hideKeyboard(View view){
        if (view != null && getContext() != null) {
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null){
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * Callback method to be invoked when the global layout state or the visibility of views
     * within the view tree changes
     */
    @Override
    public void onGlobalLayout() {
        circleReveal(view);
        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    /*
    Provides a circular reveal animation of the provided view
     */
    private void circleReveal(View view){
        int startRadius = 0;
        int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, endRadius);
        anim.setDuration(250);
        anim.start();
    }

    /*
    Provides a circular hide animation of the provided view
     */
    private void circleHide(final View view){
        int endRadius = 0;
        int startRadius = (int) Math.hypot(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, endRadius);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
                onViewClosedListener.viewClosed();
                removeFragment();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        anim.setDuration(250);
        anim.start();
    }

}
