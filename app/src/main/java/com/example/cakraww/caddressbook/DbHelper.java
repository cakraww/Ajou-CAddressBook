package com.example.cakraww.caddressbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.cakraww.caddressbook.CAddressBookContract.COL_COMPANY;
import static com.example.cakraww.caddressbook.CAddressBookContract.COL_NAME;
import static com.example.cakraww.caddressbook.CAddressBookContract.COL_PHONE;
import static com.example.cakraww.caddressbook.CAddressBookContract.TABLE_NAME;

/**
 * Created by cakraww on 7/15/16.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "caddressbook.db";
    private static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE %s(%s VARCHAR(100) PRIMARY KEY, %s VARCHAR(30) NOT NULL, %s VARCHAR(100))", TABLE_NAME, COL_NAME, COL_PHONE, COL_COMPANY);
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE contacts";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
//                Arrays.asList(
//                new Contact("Kimchi", "08080808", "Chikim"),
//                new Contact("Uyu", "39389494", "Chikim"),
//                new Contact("Jwesongheyo", "998473772", "Chikim"),
//                new Contact("Gwenchanayo", "91932394", "Chikim"),
//                new Contact("Binchi", "93939392939", "Chikim")
//        );
        try (SQLiteDatabase db = getReadableDatabase();
             Cursor c = db.query(TABLE_NAME, new String[]{COL_NAME, COL_PHONE, COL_COMPANY}, null, null, null, null, COL_NAME)) {
            if (c.moveToFirst()) {
                do {
                    String name = c.getString(c.getColumnIndex(COL_NAME));
                    String phone = c.getString(c.getColumnIndex(COL_PHONE));
                    String company = c.getString(c.getColumnIndex(COL_COMPANY));

                    contacts.add(new Contact(name, phone, company));
                } while (c.moveToNext());
            }
        }

        return contacts;
    }

    public long insertContact(String name, String phone, String company) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);
        values.put(COL_COMPANY, company);

        long id = -1;
        try (SQLiteDatabase db = getWritableDatabase()) {
            id = db.insert(TABLE_NAME, null, values);
        }
        return id;
    }
}
