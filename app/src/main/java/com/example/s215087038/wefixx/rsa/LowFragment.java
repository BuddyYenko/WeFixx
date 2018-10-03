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
import com.example.s215087038.wefixx.adapter.AssignedRequestAdapter;
import com.example.s215087038.wefixx.adapter.LowAdapter;
import com.example.s215087038.wefixx.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LowFragment extends Fragment {
    private List<Request> lowList;
    private RecyclerView lowRecyclerView;
    private AssignedRequestAdapter aAdapter;
    String lowUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/low.php";
    public LowFragment() {
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
        View myFragmentView = inflater.inflate(R.layout.fragment_low, container, false);
        lowList = new ArrayList<>();
        lowRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.lowRecylcerView);

        aAdapter = new AssignedRequestAdapter(lowList);

        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lowRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lowRecyclerView.setItemAnimator(new DefaultItemAnimator());
        lowRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        lowRecyclerView.setAdapter(aAdapter);
        prepareRequestData();
        return myFragmentView;
    }

    private void prepareRequestData() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.GET, lowUrl, new Response.Listener<String>() {
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
                        lowList.add(new Request(
                                request.getString("fault_id"),
                                request.getString("request_date"),
                                request.getString("date_assigned"),
                                request.getString("expected_close"),
                                request.getString("days_overdue"),
                                request.getString("room"),
                                request.getString("request_type"),
                                request.getString("description"),
                                request.getString("provider"),
                                request.getString("status"),
                                request.getString("photo")
                                ));

                    }

                    //creating adapter object and setting it to recyclerview
                    LowAdapter adapter = new LowAdapter(getActivity(), lowList);
                    lowRecyclerView.setAdapter(adapter);
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
