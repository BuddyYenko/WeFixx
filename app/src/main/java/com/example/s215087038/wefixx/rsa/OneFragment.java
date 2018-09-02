package com.example.s215087038.wefixx.rsa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.NewRequest;
import com.example.s215087038.wefixx.PriorityDataObject;
import com.example.s215087038.wefixx.ProviderDataObject;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.RecyclerTouchListener;
import com.example.s215087038.wefixx.SpinnerAdapter;
import com.example.s215087038.wefixx.models.Request;
import com.example.s215087038.wefixx.student.DataObject;
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

public class OneFragment extends Fragment {
    private List<Request> requestList;
    private RecyclerView openRecyclerView;
    private RequestAdapter mAdapter;
    String openRequestsUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/open_requests.php";
    String priorityUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/fault_provider.php";
    String providerUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/priority.php";
    protected List<ProviderDataObject> providerData;
    protected List<PriorityDataObject> priorityData;
    private RequestQueue queue;
    String provider, priority;

    Spinner sp_provider, sp_priority;
    Context mContext;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_one, container, false);

        requestList = new ArrayList<>();
        openRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.openRecylcerView);
        sp_provider = (Spinner) myFragmentView.findViewById(R.id.sp_provider);
        sp_priority = (Spinner) myFragmentView.findViewById(R.id.sp_priority);


        mAdapter = new RequestAdapter(requestList);

        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        openRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        openRecyclerView.setItemAnimator(new DefaultItemAnimator());
        openRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        openRecyclerView.setAdapter(mAdapter);

   //     priorityJsonObject();
     //   providerJsonObject();
        prepareRequestData();

//        openRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), openRecyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Request request = requestList.get(position);
//                Toast.makeText(getActivity(), request.getDescription() + " is selected!", Toast.LENGTH_SHORT).show();
////
////                Intent i = new Intent(mContext, AssignRequest.class);
////                i.putExtra("request_id", request.getID());
////                mContext.startActivity(i);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));
        return myFragmentView;
    }

    private void providerJsonObject() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, providerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                providerData = Arrays.asList(mGson.fromJson(response, ProviderDataObject[].class));
                //display first question to the user
                if (null != providerData) {
                    sp_provider.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long fault_id) {
                                    ProviderDataObject selected = (ProviderDataObject
                                            ) parent.getItemAtPosition(position);
                                    //get selected fault type
                                    provider = selected.getID();
                                }

                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                    assert sp_provider != null;
                    ProviderSpinnerAdapter spinnerAdapter = new ProviderSpinnerAdapter(getActivity(), providerData);
                    sp_provider.setAdapter(spinnerAdapter);
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

                params.put("fault_id", "1");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void priorityJsonObject() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, priorityUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                priorityData = Arrays.asList(mGson.fromJson(response, PriorityDataObject[].class));
                //display first question to the user
                if (null != priorityData) {
                    sp_priority.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long fault_id) {
                                    PriorityDataObject selected = (PriorityDataObject) parent.getItemAtPosition(position);
                                    //get selected fault type
                                    priority = selected.getID();
                                }

                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                    assert sp_priority != null;
                    PrioritySpinnerAdapter spinnerAdapter = new PrioritySpinnerAdapter(getActivity(), priorityData);
                    sp_priority.setAdapter(spinnerAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }


    private void prepareRequestData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, openRequestsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting request object from json array
                        JSONObject request = array.getJSONObject(i);

                        //adding the request to request open_list
                        requestList.add(new Request(
                                request.getString("fault_id"),
                                request.getString("request_date"),
                                request.getString("request_type"),
                                request.getString("description"),
                                request.getString("room"),
                                "http://sict-iis.nmmu.ac.za/wefixx/files/photos/" + request.getString("photo") + ".jpeg"
                        ));

                    }

                    //creating adapter object and setting it to recyclerview
                    RequestAdapter adapter = new RequestAdapter(getActivity(), requestList);
                    openRecyclerView.setAdapter(adapter);
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