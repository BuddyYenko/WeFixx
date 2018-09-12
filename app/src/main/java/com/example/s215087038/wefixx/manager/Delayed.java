package com.example.s215087038.wefixx.manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.adapter.DelayedAdapter;
import com.example.s215087038.wefixx.adapter.HistoryAdapter;
import com.example.s215087038.wefixx.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Delayed extends AppCompatActivity {
    private List<Request> requestList;
    private RecyclerView recyclerView;
    private DelayedAdapter mAdapter;
    String historyUrl = "http://sict-iis.nmmu.ac.za/wefixx/manager/delayed.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        requestList = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recylcerView);
        mAdapter = new DelayedAdapter(requestList);

        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(Delayed.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(Delayed.this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        prepareRequestData();

    }


    private void prepareRequestData() {
        RequestQueue queue = Volley.newRequestQueue(Delayed.this);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, historyUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {
                        //getting request object from json array
                        JSONObject request = array.getJSONObject(i);

                        //adding the request to request list_open
                        requestList.add(new Request(
                                request.getString("request_date"),
                                request.getString("date_assigned"),
                                request.getString("request_type"),
                                request.getString("description"),
                                request.getString("room"),
                                request.getString("requester"),
                                request.getString("priority"),
                                request.getString("turnaround"),
                                request.getString("contact_number"),
                                request.getString("email"),
                                request.getString("provider_status"),
                                "http://sict-iis.nmmu.ac.za/wefixx/files/photos/" + request.getString("photo") + ".jpeg"

                        ));

                    }

                    //creating adapter object and setting it to recyclerview
                    DelayedAdapter adapter = new DelayedAdapter(Delayed.this, requestList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
        mAdapter.notifyDataSetChanged();
    }
}
