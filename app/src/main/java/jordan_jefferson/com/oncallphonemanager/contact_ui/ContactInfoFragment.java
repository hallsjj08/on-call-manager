package jordan_jefferson.com.oncallphonemanager.contact_ui;

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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import jordan_jefferson.com.oncallphonemanager.R;
import jordan_jefferson.com.oncallphonemanager.data.Contact;

/*
ContactInfoFragment is a UI that lets the user either create a new contact for their contact list,
update an existing contact, or delete a contact.
 */
public class ContactInfoFragment extends Fragment implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    private Contact contact;
    private boolean numberFormatted = false;
    private static final String TAG = "CONTACT INFO";

    private ContactViewModel viewModel;

    private TextInputLayout inputLayout;
    private EditText etPhone;
    private EditText etContactName;
    private EditText etCompanyName;


    /*
    Empty public constructor for Fragment
     */
    public ContactInfoFragment(){ }

    /*
    A method to create a new instance of ContactInfoInfoFragment
    @param contact, the contact selected from the users contact list
     */
    public static Fragment newInstance(){
        return new ContactInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        if(getArguments() != null){
            this.contact = (Contact) getArguments().getSerializable("CONTACT");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);

        init(view);

        return view;
    }

    private void init(View view){
        Button bSubmit = view.findViewById(R.id.bSubmit);
        ImageButton bCancel = view.findViewById(R.id.bCancel);
        Button bDelete = view.findViewById(R.id.bDelete);

        TextView title = view.findViewById(R.id.coantact_card_title);
        inputLayout = view.findViewById(R.id.textInputLayout3);
        etPhone = view.findViewById(R.id.edPhone);
        etContactName = view.findViewById(R.id.edContactName);
        etCompanyName = view.findViewById(R.id.edCompanyName);

        bSubmit.setOnClickListener(this);
        bCancel.setOnClickListener(this);
        bDelete.setOnClickListener(this);
        etPhone.addTextChangedListener(this);

        if(contact != null){
            title.setText(R.string.edit_contact);
            etContactName.setText(contact.get_contactName());
            etCompanyName.setText(contact.get_companyName());
            etPhone.setText(contact.get_contactDisplayNumber());
            bSubmit.setText(R.string.update);
            bDelete.setVisibility(View.VISIBLE);
            numberFormatted = true;
        }else{
            etPhone.setOnFocusChangeListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bSubmit:
                addUpdateContact(v);
                break;
            case R.id.bCancel:
                finishActivity();
                break;
            case R.id.bDelete:
                viewModel.delete(contact);
                finishActivity();
                break;
        }
    }

    private void finishActivity(){
        hideKeyboard(getView());
        getActivity().finish();
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

    private void addUpdateContact(View v){
        String contactName = etContactName.getText().toString();
        String companyName = etCompanyName.getText().toString();
        String number = etPhone.getText().toString().trim();
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
            viewModel.insert(contact);
            finishActivity();
        }else{
            Snackbar.make(v, "Please enter a valid phone number.", Snackbar.LENGTH_LONG)
                    .show();
        }

        hideKeyboard(v);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(etPhone.getText().toString().trim().length() == 10){
            inputLayout.setErrorEnabled(false);
            numberFormatted = true;
        }else{
            inputLayout.setErrorEnabled(true);
            inputLayout.setError("Replace unknown digits with \"#\".");
            numberFormatted = false;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        etPhone.setHint("Ex. (123) 456-####");
        inputLayout.setError("Replace unknown digits with \"#\".");
    }
}
