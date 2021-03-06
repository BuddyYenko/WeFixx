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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.adapter.OtherAssignAdapter;
import com.example.s215087038.wefixx.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherAssignFragment extends Fragment {
    private List<Request> list;
    private RecyclerView recyclerView;
    private OtherAssignAdapter aAdapter;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/rsa/by_category.php";
    public OtherAssignFragment() {
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
        final View myFragmentView = inflater.inflate(R.layout.fragment_other_assign, container, false);
        list = new ArrayList<>();
        recyclerView = (RecyclerView) myFragmentView.findViewById(R.id.recylcerView);

        aAdapter = new OtherAssignAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(aAdapter);
        prepareRequestData();
        return myFragmentView;
    }

    public void prepareRequestData() {
        list.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
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
                        list.add(new Request(
                                request.getString("fault_id"),
                                request.getString("request_date"),
                                request.getString("room"),
                                request.getString("request_type"),
                                request.getString("fault_type_id"),
                                request.getInt("user_id"),
                                request.getString("description"),
                                request.getString("status"),
                                request.getString("photo")
                        ));
                    }
                    //creating adapter object and setting it to recyclerview
                    OtherAssignAdapter adapter = new OtherAssignAdapter(getActivity(), list, OtherAssignFragment.this);
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
                params.put("action", "Assign");
                params.put("type", "Other");

                return params;
            }
        };
        queue.add(stringRequest1);

    }

}
