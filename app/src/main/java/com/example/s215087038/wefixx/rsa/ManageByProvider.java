package com.example.s215087038.wefixx.rsa;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.FilePath;
import com.example.s215087038.wefixx.LoginActivity;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.NewRequest;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.SpinnerAdapter;
import com.example.s215087038.wefixx.adapter.AssignedRequestAdapter;
import com.example.s215087038.wefixx.adapter.ByProviderAdapter;
import com.example.s215087038.wefixx.adapter.LowAdapter;
import com.example.s215087038.wefixx.adapter.ProviderSpinnerAdapter;
import com.example.s215087038.wefixx.model.DataObject;
import com.example.s215087038.wefixx.model.ProviderDataObject;
import com.example.s215087038.wefixx.model.VolleyMultipartRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ManageByProvider extends AppCompatActivity implements ByProviderAdapter.CallbackInterface {
    public Spinner sp_provider;
    protected List<ProviderDataObject> spinnerData;
    String provider, sProvider;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/rsa/providers.php";
    String requestsUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/requests_by_provider.php";
    private List<com.example.s215087038.wefixx.model.Request> requestList;
    private RecyclerView requestRecyclerView;
    private ByProviderAdapter aAdapter;
    TextView tv_details, tv_provider;
    Uri fileuri;
    String path;
    byte[] result;
    private static final int STORAGE_PERMISSION_CODE = 123;
    AlertDialog.Builder builder;
    String id;
    String UPLOAD_URL = "http://sict-iis.nmmu.ac.za/wefixx/rsa/update_request.php";

    public static void startDoc(Intent intent) {
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Intent logout = new Intent(ManageByProvider.this, LoginActivity.class);
            startActivity(logout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_by_provider);

        requestStoragePermission();

        requestList = new ArrayList<>();
        requestRecyclerView = (RecyclerView) findViewById(R.id.requestRecylcerView);
        tv_details = (TextView) findViewById(R.id.tv_details);
        tv_provider = (TextView) findViewById(R.id.tv_provider);


        aAdapter = new ByProviderAdapter(requestList);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(ManageByProvider.this));
        requestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        requestRecyclerView.addItemDecoration(new MyDividerItemDecoration(ManageByProvider.this, LinearLayoutManager.VERTICAL, 16));
        requestRecyclerView.setAdapter(aAdapter);
        requestJsonObject();
       // prepareRequestData("1");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        builder = new AlertDialog.Builder(ManageByProvider.this);

    }
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            id = intent.getStringExtra("id" );
            String path = FilePath.getPath(ManageByProvider.this, fileuri);

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


            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            try {
                             //   Toast.makeText(ManageByProvider.this, response.data.toString() + " " + response.toString(), Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = new JSONArray(new String (response.data));
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
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //adding the request to volley
            Volley.newRequestQueue(ManageByProvider.this).add(volleyMultipartRequest);

        }
    };
    public void DisplayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prepareRequestData(provider);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void requestJsonObject() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spinnerData = Arrays.asList(mGson.fromJson(response, ProviderDataObject[].class));
                //display first question to the user
                if(null != spinnerData){
                    sp_provider = (Spinner) findViewById(R.id.sp_provider);
                    sp_provider.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long fault_id) {
                                    ProviderDataObject selected = (ProviderDataObject) parent.getItemAtPosition(position);
                                    //get selected fault type
                                    provider = selected.getID();
                                    tv_provider.setText(selected.getName());
                                    sProvider = selected.getName();
                                    prepareRequestData(provider);
                                }
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                    assert sp_provider != null;
                    ProviderSpinnerAdapter spinnerAdapter = new ProviderSpinnerAdapter(ManageByProvider.this, spinnerData);
                    sp_provider.setAdapter(spinnerAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }
    private void prepareRequestData(final String provider) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.POST, requestsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    requestList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        //getting request object from json array
                        JSONObject request = array.getJSONObject(i);

                        //adding the request to request list_open
                        requestList.add(new com.example.s215087038.wefixx.model.Request(
                                request.getString("fault_id"),
                                request.getString("request_date"),
                                request.getString("date_assigned"),
                                request.getString("expected_close"),
                                request.getString("days_overdue"),
                                request.getString("priority"),
                                request.getString("provider"),
                                request.getString("description"),
                                request.getString("request_type"),
                                request.getString("status"),
                                request.getString("photo"),
                                request.getString("room")

                                ));
                    }
                    //creating adapter object and setting it to recyclerview
                    ByProviderAdapter adapter = new ByProviderAdapter(ManageByProvider.this, requestList);
                    requestRecyclerView.setAdapter(adapter);
                    if(requestList == null || requestList.isEmpty()){
                        tv_details.setText("No requests for " + sProvider + " to manage");
                        tv_details.setVisibility(View.VISIBLE);
                    }
                    else{
                        tv_details.setVisibility(View.INVISIBLE);
                    }
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

                params.put("provider", provider);

                return params;
            }
        };
        queue.add(stringRequest1);


    }

    @Override
    protected void onActivityResult(int req, int result, Intent data)
    {
        ByProviderAdapter adapter = new ByProviderAdapter(ManageByProvider.this, requestList);
        super.onActivityResult(req, result, data);
        if (result == RESULT_OK)
        {
            fileuri = data.getData();
            String file = fileuri.getPath();
            path = FilePath.getPath(this, fileuri);
            Toast.makeText(this, "Document chosen: " + path, Toast.LENGTH_LONG).show();

            adapter = new ByProviderAdapter(path);

        }
        else
        {
            Toast.makeText(this, "result " +result, Toast.LENGTH_SHORT).show();

        }

    }

}
