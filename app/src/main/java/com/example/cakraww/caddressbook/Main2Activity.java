package com.example.cakraww.caddressbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private void startAddContactActivity() {
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }

    public static class ContactFragment extends Fragment {
        private List<Contact> contacts;
        private ArrayAdapter<Contact> adapter;
        private DataSource dataSource;
        private CAddressBookContract.Category category;

        public ContactFragment() {
        }

        public static ContactFragment newInstance(CAddressBookContract.Category category) {
            ContactFragment fragment = new ContactFragment();

            Bundle args = new Bundle();
            args.putSerializable("category", category);
            fragment.setArguments(args);
            return fragment;
        }

        public void refreshContacts() {
            if (dataSource != null) {
                contacts = dataSource.getContacts(category);
                adapter.clear();
                adapter.addAll(contacts);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            refreshContacts();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle args = getArguments();
            if (args != null && dataSource == null) {
                dataSource = new DbDataSource(getContext());
                category= (CAddressBookContract.Category) args.getSerializable("category");
            }
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
//        dataSource = new ServerDataSource(this);

            if (dataSource != null) {
                contacts = dataSource.getContacts(category);

                ListView contactList = (ListView) rootView.findViewById(R.id.contact_list);

                adapter = new ArrayAdapter<Contact>(getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, contacts) {

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
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    Toast.makeText(view.getContext(), "Call permission is not enabled", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                startActivity(callIntent);
                            }).setNegativeButton("Cancel", (dialogInterface1, i2) -> dialogInterface1.dismiss());
                    builder.create().show();

                });
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<ContactFragment> contactFragmentList;
        private List<CAddressBookContract.Category> categories = Arrays.asList(null, CAddressBookContract.Category.FAMILY, CAddressBookContract.Category.WORK, CAddressBookContract.Category.FRIEND);

        public List<ContactFragment> getContactFragmentList() {
            return contactFragmentList;
        }
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            contactFragmentList = Arrays.asList(ContactFragment.newInstance(null),
                    ContactFragment.newInstance(CAddressBookContract.Category.FAMILY),
                    ContactFragment.newInstance(CAddressBookContract.Category.WORK),
                    ContactFragment.newInstance(CAddressBookContract.Category.FRIEND));
//            return contactFragmentList.get(position);
//            contactFragmentList = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return contactFragmentList.get(position);
//            return ContactFragment.newInstance(CAddressBookContract.Category.FAMILY);
        }

        @Override
        public int getCount() {
            return contactFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categories.get(position) != null ? categories.get(position).getDisplay() : "All";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_contact:
                startAddContactActivity();
                return true;
            case R.id.refresh_contacts:
                for (ContactFragment contactFragment: mSectionsPagerAdapter.getContactFragmentList()) {
                    contactFragment.refreshContacts();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
