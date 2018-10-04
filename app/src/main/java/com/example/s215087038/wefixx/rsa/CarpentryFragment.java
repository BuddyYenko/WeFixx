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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.adapter.AssignedRequestAdapter;
import com.example.s215087038.wefixx.adapter.CarpentryAdapter;
import com.example.s215087038.wefixx.adapter.LowAdapter;
import com.example.s215087038.wefixx.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarpentryFragment extends Fragment {
    private List<Request> carpentryList;
    private RecyclerView recyclerView;
    private RadioButton rb_action;
    private CarpentryAdapter aAdapter;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/rsa/carpentry.php";
    String action = "Close Request";
    public CarpentryFragment() {
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
        final View myFragmentView = inflater.inflate(R.layout.fragment_carpentry, container, false);
        carpentryList = new ArrayList<>();
        recyclerView = (RecyclerView) myFragmentView.findViewById(R.id.recylcerView);
//        rb_assign = (RadioButton) myFragmentView.findViewById(R.id.radioAssign);
//        rb_close = (RadioButton) myFragmentView.findViewById(R.id.radioClose);

        aAdapter = new CarpentryAdapter(carpentryList);

        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(aAdapter);
        RadioGroup radioGroup = (RadioGroup) myFragmentView.findViewById(R.id.radioAction);
        prepareRequestData();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                rb_action = (RadioButton) myFragmentView.findViewById(checkedId);
                action = rb_action.getText().toString();
                Toast.makeText(getActivity(), action, Toast.LENGTH_SHORT).show();
                prepareRequestData();

            }
        });
        return myFragmentView;
    }

    private void prepareRequestData() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);
                    carpentryList.clear();
                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting request object from json array
                        JSONObject request = array.getJSONObject(i);

                        //adding the request to request list_open
                        if (action == "Assign Request") {
                            carpentryList.add(new Request(
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
                        else if(action == "Close Request")
                        {
                            carpentryList.add(new Request(
                                    request.getInt("user_id"),
                                    request.getString("fault_id"),
                                    request.getString("request_date"),
                                    request.getString("date_assigned"),
                                    request.getString("expected_close"),
                                    request.getString("days_overdue"),
                                    request.getString("priority"),
                                    request.getString("request_type"),
                                    request.getString("description"),
                                    request.getString("provider"),
                                    request.getString("status"),
                                    request.getString("photo"),
                                    request.getString("room")
                                    ));
                        }

                    }

                    //creating adapter object and setting it to recyclerview
                    CarpentryAdapter adapter = new CarpentryAdapter(getActivity(), carpentryList, action);
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

                params.put("action", action);
                return params;
            }
        };
        queue.add(stringRequest1);

    }

}
