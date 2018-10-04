package com.example.s215087038.wefixx.rsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.NewRequest;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.SpinnerAdapter;
import com.example.s215087038.wefixx.adapter.AssignedRequestAdapter;
import com.example.s215087038.wefixx.adapter.ByProviderAdapter;
import com.example.s215087038.wefixx.adapter.LowAdapter;
import com.example.s215087038.wefixx.adapter.ProviderSpinnerAdapter;
import com.example.s215087038.wefixx.model.DataObject;
import com.example.s215087038.wefixx.model.ProviderDataObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageByProvider extends AppCompatActivity {
    public Spinner sp_provider;
    protected List<ProviderDataObject> spinnerData;
    String provider, sProvider;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/rsa/providers.php";
    String requestsUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/requests_by_provider.php";
    private List<com.example.s215087038.wefixx.model.Request> requestList;
    private RecyclerView requestRecyclerView;
    private ByProviderAdapter aAdapter;
    TextView tv_details, tv_provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_by_provider);
        requestList = new ArrayList<>();
        requestRecyclerView = (RecyclerView) findViewById(R.id.requestRecylcerView);
        tv_details = (TextView) findViewById(R.id.tv_details);
        tv_provider = (TextView) findViewById(R.id.tv_provider);


        aAdapter = new ByProviderAdapter(requestList);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(ManageByProvider.this));
        requestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        requestRecyclerView.addItemDecoration(new MyDividerItemDecoration(ManageByProvider.this, LinearLayoutManager.VERTICAL, 16));
        requestRecyclerView.setAdapter(aAdapter);
        requestJsonObject();
        prepareRequestData("1");

    }

    private void requestJsonObject() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spinnerData = Arrays.asList(mGson.fromJson(response, ProviderDataObject[].class));
                //display first question to the user
                if(null != spinnerData){
                    sp_provider = (Spinner) findViewById(R.id.sp_provider);
                    sp_provider.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long fault_id) {
                                    ProviderDataObject selected = (ProviderDataObject) parent.getItemAtPosition(position);
                                    //get selected fault type
                                    provider = selected.getID();
                                    tv_provider.setText(selected.getName());
                                    sProvider = selected.getName();
                                    prepareRequestData(provider);
                                }
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                    assert sp_provider != null;
                    ProviderSpinnerAdapter spinnerAdapter = new ProviderSpinnerAdapter(ManageByProvider.this, spinnerData);
                    sp_provider.setAdapter(spinnerAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }
    private void prepareRequestData(final String provider) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.POST, requestsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    requestList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        //getting request object from json array
                        JSONObject request = array.getJSONObject(i);

                        //adding the request to request list_open
                        requestList.add(new com.example.s215087038.wefixx.model.Request(
                                request.getString("fault_id"),
                                request.getString("request_date"),
                                request.getString("date_assigned"),
                                request.getString("expected_close"),
                                request.getString("days_overdue"),
                                request.getString("priority"),
                                request.getString("provider"),
                                request.getString("request_type"),
                                request.getString("description"),
                                request.getString("status"),
                                request.getString("photo"),
                                request.getString("room")

                                ));
                    }
                    //creating adapter object and setting it to recyclerview
                    ByProviderAdapter adapter = new ByProviderAdapter(ManageByProvider.this, requestList);
                    requestRecyclerView.setAdapter(adapter);
                    if(requestList == null || requestList.isEmpty()){
                        tv_details.setText("No requests for " + sProvider + " to manage");
                        tv_details.setVisibility(View.VISIBLE);
                    }
                    else{
                        tv_details.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("provider", provider);

                return params;
            }
        };
        queue.add(stringRequest1);

    }


}
