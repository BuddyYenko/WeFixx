package com.example.s215087038.wefixx.rsa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.models.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TwoFragment extends Fragment {
    private List<Request> assignedRequestList;
    private RecyclerView  assignedRecyclerView;
    private RequestAdapter aAdapter;
    String assignedRequestsUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/assigned_requests.php";

    public TwoFragment() {
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
        View myFragmentView = inflater.inflate(R.layout.fragment_two, container, false);
        assignedRequestList = new ArrayList<>();
        assignedRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.assignedRecylcerView);
        aAdapter = new RequestAdapter(assignedRequestList);

        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        assignedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        assignedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        assignedRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        assignedRecyclerView.setAdapter(aAdapter);
        prepareRequestData();
        return myFragmentView;
    }

    private void prepareRequestData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.GET, assignedRequestsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting request object from json array
                        JSONObject request = array.getJSONObject(i);

                        //adding the request to request list
                        assignedRequestList.add(new Request(
                                //request.getInt("id"),
                                request.getString("request_date"),
                                request.getString("request_type"),
                                request.getString("status"),
                                request.getString("description")
                        ));

                    }

                    //creating adapter object and setting it to recyclerview
                    RequestAdapter adapter = new RequestAdapter(getActivity(), assignedRequestList);
                    assignedRecyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest1);
    }
}
