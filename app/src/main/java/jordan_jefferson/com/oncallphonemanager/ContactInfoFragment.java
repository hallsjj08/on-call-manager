package jordan_jefferson.com.oncallphonemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/*
ContactInfoFragment is a UI that lets the user either create a new contact for their contact list,
update an existing contact, or delete a contact.
 */
public class ContactInfoFragment extends Fragment {

    private int position;
    private Contact contact = null;
    private boolean numberFormatted;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_contact_info, container, false);

        Button bSubmit = view.findViewById(R.id.bSubmit);
        Button bCancel = view.findViewById(R.id.bCancel);
        ImageButton bDelete = view.findViewById(R.id.bDelete);
        ContactsList.getInstance(view.getContext());

        bDelete.setVisibility(View.INVISIBLE);

        final FragmentManager fm = getFragmentManager();
        final Fragment fragment = new ContactListFragment();

        /*
        Checks whether or not the user wants to edit an existing contact based on their selection
        from the contact list. The contact information gets passed to this fragment depending on
        which contact they selected.
         */
        Bundle bundle = this.getArguments();
        if(bundle != null){
            position = bundle.getInt("position");
            contact = (Contact) bundle.getSerializable("contact");
        }

        final TextInputLayout inputLayout = view.findViewById(R.id.textInputLayout3);
        final EditText etPhone = view.findViewById(R.id.edPhone);
        final EditText etContactName = view.findViewById(R.id.edContactName);
        final EditText etCompanyName = view.findViewById(R.id.edCompanyName);

        //Checks if a contact was passed from the previous screen and populates the text boxes accordingly.
        if(contact != null){

            etContactName.setText(contact.get_contactName());
            etCompanyName.setText(contact.get_companyName());
            etPhone.setText(contact.get_contactDisplayNumber());
            bSubmit.setText("Update");
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
                }else{
                    inputLayout.setErrorEnabled(true);
                    inputLayout.setError("Replace unknown digits with \"#\".");
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

                if(number.length() != 10){
                    numberFormatted = false;
                }else{
                    numberFormatted = true;
                }

                String regexNumber = number.replaceAll("#", "\\\\d");
                //regexNumber = "1" + regexNumber;
                Log.w("Phone Number", number);

                if(contact != null){
                    contact.set_contactName(contactName);
                    contact.set_companyName(companyName);
                    contact.set_contactDisplayNumber(number);
                    contact.set_contactRegexNumber(regexNumber);
                    ContactsList.getInstance(view.getContext()).updateContact(contact, position);
                }else if(contact == null && numberFormatted){
                    contact = new Contact(contactName, companyName, number, regexNumber);
                    ContactsList.getInstance(view.getContext()).addContact(contact);
                }

                if(numberFormatted){
                    CallManagerActivity.setFabVisibility(true);
                    fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                }else{
                    Toast.makeText(view.getContext(), "Please enter a valid phone number.", Toast.LENGTH_LONG).show();
                }


            }
        });

        //A Cancel button that returns to the previous fragment state.
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                CallManagerActivity.setFabVisibility(true);
                fm.popBackStack();
            }
        });

        //An image button that only shows if a user is updating a contact, giving them the option to delete.
        //TODO: Allow the user to swipe left on a contact to show delete button and remove this button.
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsList.getInstance(getContext()).removeContact(contact, position);
                FragmentManager fm = getFragmentManager();
                Fragment fragment = new ContactListFragment();

                CallManagerActivity.setFabVisibility(true);
                fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            }
        });
        return view;
    }
}
