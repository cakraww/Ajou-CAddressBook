package com.example.cakraww.caddressbook;

import java.util.List;

/**
 * Created by cakraww on 7/18/16.
 */
interface DataSource {
    long insertContact(String name, String phone, String company, CAddressBookContract.Category category);

    List<Contact> getContacts(CAddressBookContract.Category category);
}
