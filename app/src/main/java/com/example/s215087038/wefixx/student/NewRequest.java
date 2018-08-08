package com.example.s215087038.wefixx.student;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.Login;
import com.example.s215087038.wefixx.MySingleton;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.SpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewRequest extends AppCompatActivity {
    private Spinner spinner;
    private EditText Description;
    private ImageButton Photo;
    String description, photo;
    private Button btn_request;
    private static final String urlProblems =  "http://10.202.105.211:8080/wefixx/get_fault_types.php";
    private static final String urlRequest =  "http://10.202.105.211:8080/wefixx/student/new_request.php";
    protected List<DataObject> spinnerData;
    private RequestQueue queue;
    AlertDialog.Builder builder;
    Bitmap bitmap = null;

    //photo





    String fault_type_name, fault_type;
    public static final int GET_FROM_GALLERY = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_request);
        requestJsonObject();

        //Getting id by their xml
        Photo = (ImageButton) findViewById(R.id.ib_photo);
        Description = (EditText) findViewById(R.id.et_description);
        btn_request = (Button) findViewById(R.id.btn_request);

        builder = new AlertDialog.Builder(NewRequest.this);

        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = Description.getText().toString();


                if (description.equals("")) {
                    builder.setTitle("Something Went Wrong...");
                    builder.setMessage("Please fill in description");
                    displayAlert("input_error");
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRequest,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");

                                        if(code.equals("login_success")){
                                            String passengerID = jsonObject.getString("user_id");
                                            String passengerName = jsonObject.getString("name");
                                        }
                                        builder.setTitle("Yenko Buddy Response");
                                        builder.setMessage(message);
                                        displayAlert(code);
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

                            params.put("description", description);
                            params.put("photo", photo);

                            return params;
                        }
                    };
                    MySingleton.getInstance(NewRequest.this).addToRequestque(stringRequest);

                }
            }
        });

    }

    private void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {

                } else if (code.equals("login_failed")) {
                    //Password.setText("");
                } else if (code.equals("login_success")) {
                    Description.setText("");
                    Intent scannerPage = new Intent(NewRequest.this, Student.class);
                    startActivity(scannerPage);
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == NewRequest.RESULT_OK) {
            Uri selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //photo = P bitmap;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }




}
