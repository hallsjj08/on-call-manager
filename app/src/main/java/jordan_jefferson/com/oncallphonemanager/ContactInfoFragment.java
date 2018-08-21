package jordan_jefferson.com.oncallphonemanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

/*
ContactInfoFragment is a UI that lets the user either create a new contact for their contact list,
update an existing contact, or delete a contact.
 */
public class ContactInfoFragment extends Fragment {

    private Contact contact;
    private boolean numberFormatted;
    private static final String CONTACT_KEY = "position";
    private FragmentManager fm;
    private Fragment fragment;

    private ContactViewModel viewModel;

    public static Fragment getInstance(Contact contact){
        ContactInfoFragment contactInfoFragment = new ContactInfoFragment();

        Bundle b = new Bundle();
        b.putSerializable(CONTACT_KEY, contact);
        contactInfoFragment.setArguments(b);

        return contactInfoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fm = getFragmentManager();
        viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        if(getArguments() != null){
            contact = (Contact) getArguments().getSerializable(CONTACT_KEY);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_contact_info, container, false);



        Button bSubmit = view.findViewById(R.id.bSubmit);
        Button bCancel = view.findViewById(R.id.bCancel);
        ImageButton bDelete = view.findViewById(R.id.bDelete);

        bDelete.setVisibility(View.INVISIBLE);

        fragment = ContactListFragment.getInstance();


        final TextInputLayout inputLayout = view.findViewById(R.id.textInputLayout3);
        final EditText etPhone = view.findViewById(R.id.edPhone);
        final EditText etContactName = view.findViewById(R.id.edContactName);
        final EditText etCompanyName = view.findViewById(R.id.edCompanyName);

        if(contact != null){
            etContactName.setText(contact.get_contactName());
            etCompanyName.setText(contact.get_companyName());
            etPhone.setText(contact.get_contactDisplayNumber());
            bSubmit.setText(R.string.update);
            bDelete.setVisibility(View.VISIBLE);
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

                String regexNumber = number.replaceAll("#", "\\\\d");

                if(contact != null){
                    contact.set_contactName(contactName);
                    contact.set_companyName(companyName);
                    contact.set_contactDisplayNumber(number);
                    contact.set_contactRegexNumber(regexNumber);
                    viewModel.insert(contact);
                }else if(contact == null && numberFormatted){
                    contact = new Contact(contactName, companyName, number, regexNumber);
                    viewModel.insert(contact);
                }

                if(numberFormatted){
                    assert fm != null;
                    fm.beginTransaction().replace(R.id.fragmentContainer, fragment, CallManagerActivity.FRAGMENT_TAG).commit();
                }else{
                    Toast.makeText(view.getContext(), "Please enter a valid phone number.", Toast.LENGTH_LONG).show();
                }


            }
        });

        //A Cancel button that returns to the previous fragment state.
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert fm != null;
                fm.popBackStack();
            }
        });

        //An image button that only shows if a user is updating a contact, giving them the option to delete.
        //TODO: Allow the user to swipe left on a contact to show delete button and remove this button.
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.delete(contact);

//                CallManagerActivity.setFabVisibility(true);
                assert fm != null;
                fm.beginTransaction().replace(R.id.fragmentContainer, fragment, CallManagerActivity.FRAGMENT_TAG).commit();
            }
        });
        return view;
    }
}
