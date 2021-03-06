package com.example.s215087038.wefixx.rsa;

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
import com.example.s215087038.wefixx.adapter.OpenRequestAdapter;
import com.example.s215087038.wefixx.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenFragment extends Fragment {
    private List<Request> requestList;
    private RecyclerView openRecyclerView;
    private OpenRequestAdapter mAdapter;
    String openRequestsUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/open_requests.php";


    public OpenFragment() {
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

        mAdapter = new OpenRequestAdapter(requestList);

        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        openRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        openRecyclerView.setItemAnimator(new DefaultItemAnimator());
        openRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        openRecyclerView.setAdapter(mAdapter);
        prepareRequestData();
        return myFragmentView;
    }

    public void prepareRequestData() {
        requestList.clear();
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

                        //adding the request to request list_open
                        requestList.add(new Request(
                                request.getString("fault_id"),
                                request.getString("request_date"),
                                request.getString("fault_type_id"),
                                request.getString("request_type"),
                                request.getString("description"),
                                request.getString("room"),
                                request.getString("photo")
                        ));

                    }

                    //creating adapter object and setting it to recyclerview
                    OpenRequestAdapter adapter = new OpenRequestAdapter(getActivity(), requestList, OpenFragment.this);
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