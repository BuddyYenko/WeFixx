//package com.example.s215087038.wefixx.rsa;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.example.s215087038.wefixx.MyDividerItemDecoration;
//import com.example.s215087038.wefixx.NewRequest;
//import com.example.s215087038.wefixx.R;
//
//import com.example.s215087038.wefixx.SpinnerAdapter;
//import com.example.s215087038.wefixx.models.Request;
//import com.example.s215087038.wefixx.student.PriorityDataObject;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Text;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class ManageRequest extends AppCompatActivity {
//    private List<Request> requestList;
//    private List<Request> assignedRequestList;
//    private RecyclerView openRecyclerView, assignedRecyclerView;
//    private OpenRequestAdapter mAdapter, aAdapter;
//    String openRequestsUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/open_requests.php";
//    String assignedRequestsUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/assigned_requests.php";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_request);
//        ///Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//       // setSupportActionBar(toolbar);
//
//        requestList = new ArrayList<>();
//        assignedRequestList = new ArrayList<>();
//        openRecyclerView = (RecyclerView) findViewById(R.id.openRecylcerView);
//        assignedRecyclerView = (RecyclerView) findViewById(R.id.assignedRecylcerView);
//
//
//        mAdapter = new OpenRequestAdapter(requestList);
//        aAdapter = new OpenRequestAdapter(assignedRequestList);
//
//        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        openRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        openRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        openRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
//        openRecyclerView.setAdapter(mAdapter);
//
//        assignedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        assignedRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        assignedRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
//        assignedRecyclerView.setAdapter(aAdapter);
//        prepareRequestData();
//    }
//
//    private void prepareRequestData() {
////        Movie movie = new Movie("Mad Max: Fury Road", "Action & Adventure", "2015");
////        movieList.add(movie);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, openRequestsUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                             try {
//                            //converting the string to json array object
//                            JSONArray array = new JSONArray(response);
//
//                            //traversing through all the object
//                            for (int i = 0; i < array.length(); i++) {
//
//                                //getting request object from json array
//                                JSONObject request = array.getJSONObject(i);
//
//                                //adding the request to request open_list
//                                requestList.add(new Request(
//                                        //request.getInt("id"),
//                                        request.getString("request_date"),
//                                        request.getString("request_type"),
//                                        request.getString("description"),
//                                        request.getString("room")
//                                ));
//
//                            }
//
//                            //creating adapter object and setting it to recyclerview
//                            OpenRequestAdapter adapter = new OpenRequestAdapter(ManageRequest.this, requestList);
//                            openRecyclerView.setAdapter(adapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        queue.add(stringRequest);
//        mAdapter.notifyDataSetChanged();
//
//        StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.GET, assignedRequestsUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    //converting the string to json array object
//                    JSONArray array = new JSONArray(response);
//
//                    //traversing through all the object
//                    for (int i = 0; i < array.length(); i++) {
//
//                        //getting request object from json array
//                        JSONObject request = array.getJSONObject(i);
//
//                        //adding the request to request open_list
//                        assignedRequestList.add(new Request(
//                                //request.getInt("id"),
//                                request.getString("request_date"),
//                                request.getString("request_type"),
//                                request.getString("description"),
//                                request.getString("room")
//                        ));
//
//                    }
//
//                    //creating adapter object and setting it to recyclerview
//                    OpenRequestAdapter adapter = new OpenRequestAdapter(ManageRequest.this, assignedRequestList);
//                    assignedRecyclerView.setAdapter(adapter);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        queue.add(stringRequest1);
//    }
//}