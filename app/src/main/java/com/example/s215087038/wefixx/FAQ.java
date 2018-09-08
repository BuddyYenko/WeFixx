package com.example.s215087038.wefixx;

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
import com.example.s215087038.wefixx.adapter.FaqAdapter;
import com.example.s215087038.wefixx.adapter.HistoryAdapter;
import com.example.s215087038.wefixx.model.FaqDataObject;
import com.example.s215087038.wefixx.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FAQ extends AppCompatActivity {
    private List<FaqDataObject> faqList;
    private RecyclerView recyclerView;
    private FaqAdapter mAdapter;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/faq.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        faqList = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recylcerView);
        mAdapter = new FaqAdapter(faqList);

        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(FAQ.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(FAQ.this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        prepareRequestData();

    }

    private void prepareRequestData() {
        RequestQueue queue = Volley.newRequestQueue(FAQ.this);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
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
                        faqList.add(new FaqDataObject(
                                request.getString("question"),
                                request.getString("answer")


                        ));

                    }

                    //creating adapter object and setting it to recyclerview
                    FaqAdapter adapter = new FaqAdapter(FAQ.this, faqList);
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
