package com.example.s215087038.wefixx.rsa;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.FilePath;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.adapter.CarpentryCloseAdapter;
import com.example.s215087038.wefixx.adapter.PlumbingCloseAdapter;
import com.example.s215087038.wefixx.model.Request;
import com.example.s215087038.wefixx.model.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class PlumbingCloseFragment extends Fragment {
    private List<Request> list;
    private RecyclerView recyclerView;
    private PlumbingCloseAdapter aAdapter;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/rsa/by_category.php";
    String id, path;
    AlertDialog.Builder builder;
    String UPLOAD_URL = "http://sict-iis.nmmu.ac.za/wefixx/rsa/update_request.php";
    Uri fileuri;
    byte[] result;
    private static final int STORAGE_PERMISSION_CODE = 123;
    public PlumbingCloseFragment() {
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
        final View myFragmentView = inflater.inflate(R.layout.fragment_plumbing_close, container, false);
        //requestStoragePermission();

        list = new ArrayList<>();
        recyclerView = (RecyclerView) myFragmentView.findViewById(R.id.recylcerView);
        aAdapter = new PlumbingCloseAdapter(list);

        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(aAdapter);
        prepareRequestData();
        builder = new AlertDialog.Builder(getActivity());
        return myFragmentView;
    }
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void prepareRequestData() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);
                    list.clear();
                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting request object from json array
                        JSONObject request = array.getJSONObject(i);

                        //adding the request to request list_open

                        list.add(new Request(
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

                    //creating adapter object and setting it to recyclerview

                    PlumbingCloseAdapter adapter = new PlumbingCloseAdapter(getActivity(), list, PlumbingCloseFragment.this);
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

                params.put("action", "Close");
                params.put("type", "Plumbing");
                return params;
            }
        };
        queue.add(stringRequest1);

    }
    public void getFaultID(){
        id =this.getArguments().getString("id").toString();
        if(fileuri != null && !fileuri.equals(Uri.EMPTY)) {

            String path = FilePath.getPath(getActivity(), fileuri);

            //Toast.makeText(ManageByProvider.this,id + " " + path ,Toast.LENGTH_SHORT).show();

            try {

                File file = new File(path);
                int length = (int) file.length();
                BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
                byte[] bytes = new byte[length];
                reader.read(bytes, 0, length);
                reader.close();
                result = bytes;

                //encodedfile = Base64.encodeBase64(bytes).toString();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch <span id="IL_AD1" class="IL_AD">block</span>
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(com.android.volley.Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            try {
                                //   Toast.makeText(ManageByProvider.this, response.data.toString() + " " + response.toString(), Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = new JSONArray(new String(response.data));
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");

                                builder.setTitle("WeFixx Response");
                                builder.setMessage(message);
                                DisplayAlert(code);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Toast.makeText(getApplicationContext(), error.getMessage() +"error " , Toast.LENGTH_LONG).show();
                        }
                    }) {

                //Posting parameters to php script
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("fault_id", id);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {


                    Map<String, DataPart> params = new HashMap<>();
                    if (fileuri != null) {
                        String imagename = "report";
                        params.put("report", new DataPart(imagename + ".pdf", result));
                    }
                    return params;
                }

            };
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //adding the request to volley
            Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
        }
        else{
            builder.setTitle("No Report Chosen");
            builder.setMessage("Please select a document to upload");
            DisplayAlert("input_error");
        }
    }
    public void DisplayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(code == "update_success")
                {
                    prepareRequestData();
                }
                else if (code=="input_error")
                {

                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onActivityResult(int req, int result, Intent data)
    {
        PlumbingCloseAdapter adapter = new PlumbingCloseAdapter(getActivity(), list, PlumbingCloseFragment.this);
        super.onActivityResult(req, result, data);
        if (result == RESULT_OK)
        {
            fileuri = data.getData();
            String file = fileuri.getPath();
            path = FilePath.getPath(getActivity(), fileuri);
            Toast.makeText(getActivity(), "Document chosen: " + path, Toast.LENGTH_LONG).show();

            //adapter = new CarpentryCloseAdapter(path);

        }
        else
        {
            Toast.makeText(getActivity(), "result " +result, Toast.LENGTH_SHORT).show();

        }

    }
}