package com.example.s215087038.wefixx.student;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.adapter.OpenRequestAdapter;
import com.example.s215087038.wefixx.adapter.StudentOpenAdapter;
import com.example.s215087038.wefixx.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentOpenFragment extends Fragment {
    private List<Request> requestList;
    private RecyclerView recyclerView;
    private OpenRequestAdapter mAdapter;
    String openRequestsUrl = "http://sict-iis.nmmu.ac.za/wefixx/student/student_open.php";
    String student_no, name;
    Context context;

    public StudentOpenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //********************************************
        SharedPreferences preferences =  this.getActivity().getSharedPreferences("MYPREFS", context.MODE_PRIVATE);

        student_no = preferences.getString("student_no", "");
        name = preferences.getString("name", "");
        //********************************************
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_student_assigned, container, false);

        requestList = new ArrayList<>();
        recyclerView = (RecyclerView) myFragmentView.findViewById(R.id.recylcerView);

        mAdapter = new OpenRequestAdapter(requestList);

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
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, openRequestsUrl, new Response.Listener<String>() {
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
                                request.getString("request_type"),
                                request.getString("description"),
                                request.getString("photo")
                        ));


                    }

                    //creating adapter object and setting it to recyclerview
                    StudentOpenAdapter adapter = new StudentOpenAdapter(getActivity(), requestList);
                    recyclerView.setAdapter(adapter);
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

                params.put("student_no", student_no);
                return params;
            }
        };
        queue.add(stringRequest);
        mAdapter.notifyDataSetChanged();
    }

}