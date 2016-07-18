package com.example.cakraww.caddressbook;

/**
 * Created by cakraww on 7/15/16.
 */
public class Contact {
    private String name;
    private String phone;
    private String company;

    public Contact(String name, String phone, String company) {
        this.name = name;
        this.phone = phone;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCompany() {
        return company;
    }
}
