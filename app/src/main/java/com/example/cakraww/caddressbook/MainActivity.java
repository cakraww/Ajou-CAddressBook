package com.example.cakraww.caddressbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Contact> contacts;
    private ArrayAdapter<Contact> adapter;
    private DataSource dataSource;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_contact:
                startAddContactActivity();
                return true;
            case R.id.refresh_contacts:
                refreshContacts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshContacts() {
        contacts = dataSource.getContacts();
        adapter.clear();
        adapter.addAll(contacts);
        adapter.notifyDataSetChanged();
    }

    private void startAddContactActivity() {
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


//        dataSource = new DbDataSource(this);
        dataSource = new ServerDataSource(this);

        contacts = dataSource.getContacts();

        ListView contactList = (ListView) findViewById(R.id.contact_list);

        adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_2, android.R.id.text1, contacts) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
                TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
                tv1.setText(contacts.get(position).getName());
                tv2.setText(contacts.get(position).getCompany());
                return view;
            }
        };
        contactList.setAdapter(adapter);
        contactList.setOnItemClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            String phone = contacts.get(i).getPhone();
            builder.setCancelable(true).setTitle("Call: " + phone)
                    .setPositiveButton("Call", (dialogInterface, i1) -> {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + contacts.get(i)));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(view.getContext(), "Call permission is not enabled", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(callIntent);
                    }).setNegativeButton("Cancel", (dialogInterface1, i2) -> dialogInterface1.dismiss());
            builder.create().show();

//            Toast.makeText(view.getContext(), "" + phone, Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshContacts();
    }
}
