package jordan_jefferson.com.oncallphonemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class ContactInfoActivity extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener {

    private boolean numberFormatted = false;
    private ContactViewModel viewModel;
    private Contact contact;

    TextInputLayout inputLayout;
    EditText etPhone;
    EditText etContactName;
    EditText etCompanyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        ActionBar actionBar = getSupportActionBar();
        if(getIntent().hasExtra(ContactListFragment.EXTRA_CONTACT)) {
            contact = getIntent().getParcelableExtra(ContactListFragment.EXTRA_CONTACT);
        }

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            String title = contact != null ? "Edit Contact" : "New Contact";
            actionBar.setTitle(title);
        }

        viewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        Button bSubmit = findViewById(R.id.bSubmit);

        inputLayout = findViewById(R.id.textInputLayout3);
        etPhone = findViewById(R.id.edPhone);
        etContactName = findViewById(R.id.edContactName);
        etCompanyName = findViewById(R.id.edCompanyName);

        if (contact != null) {
            etContactName.setText(contact.get_contactName());
            etCompanyName.setText(contact.get_companyName());
            etPhone.setText(contact.get_contactDisplayNumber());
            numberFormatted = true;
        } else {
            etPhone.setOnFocusChangeListener(this);
        }

        etPhone.addTextChangedListener(this);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_info_menu, menu);
        return getIntent().hasExtra(ContactListFragment.EXTRA_CONTACT);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.remove_contact:
                viewModel.delete(contact);
                hideKeyboard();
                finish();
                break;
        }

        return true;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void updateContact() {
        String contactName = etContactName.getText().toString();
        String companyName = etCompanyName.getText().toString();
        String number = etPhone.getText().toString();
        number = number.trim();
        String regexNumber = number.replaceAll("#", "\\\\d");

        if (contact != null) {
            contact.set_contactName(contactName);
            contact.set_companyName(companyName);
            contact.set_contactDisplayNumber(number);
            contact.set_contactRegexNumber(regexNumber);
        } else {
            contact = new Contact(contactName, companyName, number, regexNumber);
        }

        if (numberFormatted) {
            viewModel.insert(contact);
        } else {
            Snackbar.make(getCurrentFocus(), "Please enter a valid phone number.", Snackbar.LENGTH_LONG)
                    .show();
        }

        hideKeyboard();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etPhone.getText().toString().length() == 10) {
            inputLayout.setErrorEnabled(false);
            numberFormatted = true;
        } else {
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
