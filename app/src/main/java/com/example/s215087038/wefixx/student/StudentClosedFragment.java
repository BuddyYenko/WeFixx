package com.example.s215087038.wefixx.student;

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
import com.example.s215087038.wefixx.adapter.StudentAssignedAdapter;
import com.example.s215087038.wefixx.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentClosedFragment extends Fragment {
    private List<Request> requestList;
    private RecyclerView recyclerView;
    private StudentAssignedAdapter mAdapter;
    String openRequestsUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/open_requests.php";


    public StudentClosedFragment() {
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
        View myFragmentView = inflater.inflate(R.layout.fragment_student_assigned, container, false);

        requestList = new ArrayList<>();
        recyclerView = (RecyclerView) myFragmentView.findViewById(R.id.openRecylcerView);

        mAdapter = new StudentAssignedAdapter(requestList);

        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        prepareRequestData();
        return myFragmentView;
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

                        //adding the request to request list_open
                        requestList.add(new Request(
                                request.getString("fault_id"),
                                request.getString("request_date"),
                                request.getString("date_assigned"),
                                request.getString("date_closed"),
                                request.getString("request_type"),
                                request.getString("description"),
                                request.getString("priority"),
                                request.getString("provider"),
                                request.getString("comment"),
                                request.getInt("rating"),

                                "http://sict-iis.nmmu.ac.za/wefixx/files/photos/" + request.getString("photo") + ".jpeg"
                        ));

                    }

                    //creating adapter object and setting it to recyclerview
                    OpenRequestAdapter adapter = new OpenRequestAdapter(getActivity(), requestList);
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