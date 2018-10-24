package com.example.s215087038.wefixx.manager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.s215087038.wefixx.model.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.adapter.HistoryAdapter;
import com.example.s215087038.wefixx.adapter.ProviderSpinnerAdapter;
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
import java.util.UUID;

public class ProviderHistory extends AppCompatActivity {
    public Spinner sp_provider;
    protected List<ProviderDataObject> spinnerData;
    String provider, sProvider;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/rsa/providers.php";
    String provider_history = "http://sict-iis.nmmu.ac.za/wefixx/rsa/provider_history.php";
    private List<Request> requestList;
    private RecyclerView requestRecyclerView;
    private HistoryAdapter aAdapter;
    TextView tv_details, tv_provider;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_by_provider);

        requestList = new ArrayList<>();
        requestRecyclerView = (RecyclerView) findViewById(R.id.requestRecylcerView);
        tv_details = (TextView) findViewById(R.id.tv_details);
        tv_provider = (TextView) findViewById(R.id.tv_provider);

        aAdapter = new HistoryAdapter(requestList);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(ProviderHistory.this));
        requestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        requestRecyclerView.addItemDecoration(new MyDividerItemDecoration(ProviderHistory.this, LinearLayoutManager.VERTICAL, 16));
        requestRecyclerView.setAdapter(aAdapter);
        requestJsonObject();
        prepareRequestData("1");

        builder = new AlertDialog.Builder(ProviderHistory.this);

    }

    public void DisplayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prepareRequestData(provider);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void requestJsonObject() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
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
                    ProviderSpinnerAdapter spinnerAdapter = new ProviderSpinnerAdapter(ProviderHistory.this, spinnerData);
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
        StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.POST, provider_history, new Response.Listener<String>() {
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
                        requestList.add(new Request(
                                request.getString("request_date"),
                                request.getString("date_assigned"),
                                request.getString("date_closed"),
                                request.getString("request_type"),
                                request.getString("description"),
                                request.getString("room"),
                                request.getString("comment"),
                                request.getLong("rating"),
                                request.getString("provider"),
                                request.getString("priority"),
                                request.getString("requester"),
                                request.getString("photo")
                        ));

                    }
                    //creating adapter object and setting it to recyclerview
                    HistoryAdapter adapter = new HistoryAdapter(ProviderHistory.this, requestList);
                    requestRecyclerView.setAdapter(adapter);
                    if(requestList == null || requestList.isEmpty()){
                        tv_details.setText("No  delayed requests for " + sProvider + " to manage");
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
