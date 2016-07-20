package com.example.cakraww.caddressbook;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by cakraww on 7/18/16.
 */
public class ServerDataSource implements DataSource {
    private final Context context;
    private static final String API_URL = "http://webauthoring.ajou.ac.kr/~vcl/contacts.php";
    private static SyncHttpClient client = new SyncHttpClient();

    public ServerDataSource(Context context) {
        this.context = context;
    }

    @Override
    public long insertContact(String name, String phone, String company) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("phone", phone);
        params.put("company", company);
        client.post(API_URL, params, new JsonHttpResponseHandler() {});
        return 0;
    }

    @Override
    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        client.get(API_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject contactObj = (JSONObject) response.get(i);
                        String name = contactObj.getString("name");
                        String phone = contactObj.getString("phone");

                        Contact contact = new Contact(name, phone, "");
                        contacts.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FAIL", "stat:" + statusCode + "," + errorResponse);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        return contacts;
    }
}
