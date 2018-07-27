package com.example.s215087038.wefixx.student;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.SpinnerAdapter;
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

public class NewRequest extends AppCompatActivity {
    private Spinner spinner;
    private static final String urlProblems =  "http://10.102.129.156:8080/wefixx/get_fault_types.php";
    protected List<DataObject> spinnerData;
    private RequestQueue queue;

    String fault_type_name, fault_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_request);
        requestJsonObject();


    }

    private void requestJsonObject() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlProblems, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spinnerData = Arrays.asList(mGson.fromJson(response, DataObject[].class));
                //display first question to the user
                if(null != spinnerData){
                    spinner = (Spinner) findViewById(R.id.sp_fault);
                    assert spinner != null;
                    spinner.setVisibility(View.VISIBLE);
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(NewRequest.this, spinnerData);
                    spinner.setAdapter(spinnerAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }


//    private void requestJsonObject() {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlProblems, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                GsonBuilder builder = new GsonBuilder();
//                Gson mGson = builder.create();
//                spinner_data = Arrays.asList(mGson.fromJson(response, DataObject[].class));
//                //display first question to the user
//                if(null != spinner_data){
//                    spinner = (Spinner) findViewById(R.id.sp_fault);
//                    spinner.setOnItemSelectedListener(
//                            new AdapterView.OnItemSelectedListener() {
//                                public void onItemSelected(
//
//                                        AdapterView<?> parent, View view, int position, long fault_type_id) {
//                                    DataObject selected = (DataObject) parent.getItemAtPosition(position);
//                                    fault_type = selected.getFaultyTypeId();
//                                    String selectedFaultType = selected.getName();
////                                    description  = description + selectedDescription + "\n";
////
//                                    mylist.add(fault_type);
//                                    myMap.put("fault_type_id", fault_type);
//
////                                    String [] listSelectedProblems = {selectedFaultType};
////
////                                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NewRequest.this,
////                                            android.R.layout.simple_list_item_1, listSelectedProblems){
////                                        @Override
////                                        public View getView(int position, View convertView, ViewGroup parent){
////                                            /// Get the Item from ListView
////                                            View view = super.getView(position, convertView, parent);
////
////                                            TextView tv = (TextView) view.findViewById(android.R.id.text1);
////
////                                            // Set the text size for ListView each item
////                                            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
////
////                                            // Return the view
////                                            return view;
////                                        }
////                                    };
//                                    //listViewProblems.setAdapter(arrayAdapter);
//
//
//                                }
//
//                                public void onNothingSelected(AdapterView<?> parent) {
//                                    //selectedOption.setText("Spinner1: unselected");
//                                }
//                            });
//
//                    assert spinner != null;
//                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(NewRequest.this, spinner_data);
//                    spinner.setAdapter(spinnerAdapter);
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        queue.add(stringRequest);
//    }
}
