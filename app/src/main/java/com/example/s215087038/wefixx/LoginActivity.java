package com.example.s215087038.wefixx;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.s215087038.wefixx.manager.Manager;
import com.example.s215087038.wefixx.model.MySingleton;
import com.example.s215087038.wefixx.rsa.RSA;
import com.example.s215087038.wefixx.student.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //Defining views
    Button login_btn;
    EditText et_username, et_password;
    String username, password;
    TextView tv_forgot_password;

    AlertDialog.Builder builder;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Getting id by their xml
        login_btn = (Button) findViewById(R.id.btn_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_passoword);


        //Next Activities
//        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent forgotPass = new Intent(LoginActivity.this, ForgotPassword.class);
//                startActivity(forgotPass);
//            }
//        });



        builder = new AlertDialog.Builder(LoginActivity.this);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                password = et_password.getText().toString();
                //tv_forgot_password.setText(username + " " + password);

                if (username.equals("") || password.equals("")) {
                    builder.setTitle("Something Went Wrong...");
                    builder.setMessage("Please fill in all fields");
                    DisplayAlert("input_error");
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");

                                        builder.setTitle("WeFixx Response");
                                        builder.setMessage(message);

                                        if(code.equals("login_success")){

                                            String user_type = jsonObject.getString("user_type");
                                            user_type = user_type.toLowerCase();
                                            String name = jsonObject.getString("name");
                                            if (user_type.equals("student")){
                                                String surname = jsonObject.getString("surname");
                                                String student_no = jsonObject.getString("student_no");
                                                String res = jsonObject.getString("res");
                                                StudentSessions(name, surname, student_no, res );
                                            }
                                            else{
                                                String user_id = jsonObject.getString("user_id");
                                                CreateSessions(user_id, name);
                                            }
                                            DisplayAlert(code, user_type);

                                        }

                                        if(code.equals("login_failed")){
                                            DisplayAlert(code);
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

                            params.put("username", username);
                            params.put("password", password);

                            return params;
                        }
                    };
                    MySingleton.getInstance(LoginActivity.this).addToRequestque(stringRequest);

                }
            }
        });
    }

    public void CreateSessions(String user_id, String name) {
        //***************** Session *****************
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);

        String user_id_session = preferences.getString(user_id + "data", user_id);
        String name_session = preferences.getString(name + "data", name);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", user_id);
        editor.putString("name", name);

        editor.commit();
        //*******************************************
    }
    public void StudentSessions(String name, String surname, String student_no, String res) {
        //***************** Session *****************
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);

        String name_session = preferences.getString(name + "data", name);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("surname", surname);
        editor.putString("student_no", student_no);
        editor.putString("res", res);

        editor.commit();
        //*******************************************
    }
    public void DisplayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {
                    et_password.setText("");
                }

                else if (code.equals("login_failed")) {
                    et_password.setText("");
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void DisplayAlert(final String code, final String user_type) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {
                    et_password.setText("");
                }
                else if (code.equals("login_failed")) {
                    et_password.setText("");
                }
                else if (code.equals("login_success")) {
//                    et_password.setText("");
//                    et_username.setText("");


                    if(user_type.equals("student") ){
                        Intent mainPage = new Intent(LoginActivity.this, Student.class);
                        startActivity(mainPage);
                    }
                    else if(user_type.equals("residence manager") ){
                        Intent mainPage = new Intent(LoginActivity.this, Manager.class);
                        startActivity(mainPage);
                    }
                    else  if(user_type.equals("residence student assistant") ){
                        Intent mainPage = new Intent(LoginActivity.this, RSA.class);
                        startActivity(mainPage);
                    }
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
