package com.example.cakraww.caddressbook;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.cakraww.caddressbook.CAddressBookContract.COL_COMPANY;
import static com.example.cakraww.caddressbook.CAddressBookContract.COL_NAME;
import static com.example.cakraww.caddressbook.CAddressBookContract.COL_PHONE;
import static com.example.cakraww.caddressbook.CAddressBookContract.TABLE_NAME;

public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        DbHelper dbHelper = new DbHelper(this);

        EditText nameField = (EditText) findViewById(R.id.name_field);
        EditText phoneField = (EditText) findViewById(R.id.phone_field);
        EditText companyField = (EditText) findViewById(R.id.company_field);

        Button addButton = (Button) findViewById(R.id.add_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        addButton.setOnClickListener(v -> {
            String name = nameField.getText().toString();
            String phone = phoneField.getText().toString();
            String company = companyField.getText().toString();
            dbHelper.insertContact(name, phone, company);

            Toast.makeText(v.getContext(), "Contact added", Toast.LENGTH_SHORT).show();
            finish();
        });

        cancelButton.setOnClickListener((View v) -> {
            finish();
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
