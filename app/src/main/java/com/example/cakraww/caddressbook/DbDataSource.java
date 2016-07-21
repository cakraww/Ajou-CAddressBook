package com.example.cakraww.caddressbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.cakraww.caddressbook.CAddressBookContract.COL_CATEGORY;
import static com.example.cakraww.caddressbook.CAddressBookContract.COL_COMPANY;
import static com.example.cakraww.caddressbook.CAddressBookContract.COL_NAME;
import static com.example.cakraww.caddressbook.CAddressBookContract.COL_PHONE;
import static com.example.cakraww.caddressbook.CAddressBookContract.Category.FAMILY;
import static com.example.cakraww.caddressbook.CAddressBookContract.Category.FRIEND;
import static com.example.cakraww.caddressbook.CAddressBookContract.Category.WORK;
import static com.example.cakraww.caddressbook.CAddressBookContract.TABLE_NAME;

/**
 * Created by cakraww on 7/15/16.
 */
public class DbDataSource extends SQLiteOpenHelper implements DataSource {
    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "caddressbook.db";
    private static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE %s(%s VARCHAR(100) PRIMARY KEY, %s VARCHAR(30) NOT NULL, %s VARCHAR(100), %s VARCHAR(50) NOT NULL)", TABLE_NAME, COL_NAME, COL_PHONE, COL_COMPANY, COL_CATEGORY);
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE contacts";


    public DbDataSource(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        insertContact(db, "Kimchi", "08080808", "Chikim", FAMILY);
        insertContact(db, "Uyu", "39389494", "Chikim", FAMILY);
        insertContact(db, "Jwesongheyo", "998473772", "Chikim", FAMILY);
        insertContact(db, "Gwenchanayo", "91932394", "Chikim", WORK);
        insertContact(db, "Binchi", "93939392939", "Chikim", WORK);
        insertContact(db, "Coffee", "93939392939", "Chikim", WORK);
        insertContact(db, "Gyeonggi", "93939392939", "Chikim", FRIEND);
        insertContact(db, "Jeogeo", "93939392939", "Chikim", FRIEND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public List<Contact> getContacts(CAddressBookContract.Category category) {
        String selection = null;
        String selectionArgs[] = null;
        if (category != null) {
            selection = COL_CATEGORY + "=?";
            selectionArgs = new String[]{category.getDisplay()};
        }

        List<Contact> contacts = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase();
             Cursor c = db.query(TABLE_NAME, new String[]{COL_NAME, COL_PHONE, COL_COMPANY}, selection, selectionArgs, null, null, COL_NAME)) {
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

    private long insertContact(SQLiteDatabase db, String name, String phone, String company, CAddressBookContract.Category category) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);
        values.put(COL_COMPANY, company);
        values.put(COL_CATEGORY, category.getDisplay());

        long id = db.insert(TABLE_NAME, null, values);
        return id;
    }

    @Override
    public long insertContact(String name, String phone, String company, CAddressBookContract.Category category) {
        long id = -1;
        try (SQLiteDatabase db = getWritableDatabase()) {
            id = insertContact(db, name, phone, company, category);
        }
        return id;
    }
}
