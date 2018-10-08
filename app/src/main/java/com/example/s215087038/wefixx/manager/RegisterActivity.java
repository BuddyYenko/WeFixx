package com.example.s215087038.wefixx.manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.s215087038.wefixx.GMailSender;
import com.example.s215087038.wefixx.LoginActivity;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.model.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    Button register_btn;
    EditText et_username, et_email, et_firstname, et_lastname;
    String username, email, firstname, lastname, password;
    AlertDialog.Builder builder;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/manager/register.php";
    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static Random RANDOM = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_btn = (Button) findViewById(R.id.btn_register);
        et_username = (EditText) findViewById(R.id.et_username);
        et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_email = (EditText) findViewById(R.id.et_email);

        builder = new AlertDialog.Builder(RegisterActivity.this);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                email = et_email.getText().toString();
                firstname = et_firstname.getText().toString();
                lastname = et_lastname.getText().toString();
                username = et_username.getText().toString();
                password = generatePassword();

                if (username.equals("") || email.equals("") || firstname.equals("") || lastname.equals("")) {
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
                                        if(code == "reg_success")
                                        {
                                            sendEmail();
                                        }
                                        else {
                                            builder.setTitle("WeFixx Response");
                                            builder.setMessage(message);
                                        }
                                        DisplayAlert(code);
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

                            params.put("email", email);
                            params.put("username", username);
                            params.put("firstname", firstname);
                            params.put("lastname", lastname);
                            params.put("password", password);

                            return params;
                        }
                    };
                    MySingleton.getInstance(RegisterActivity.this).addToRequestque(stringRequest);

                }
            }
        });
    }

    private void sendEmail() {
            Log.i("Send email", "");
        try {
            GMailSender sender = new GMailSender("weeluthuli@gmail.com", "password");
            sender.sendMail("This is Subject",
                    "This is Body",
                    "user@gmail.com",
                    email);
            builder.setTitle("WeFixx Response");
            builder.setMessage("Reg successful");
            DisplayAlert("reg_success");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }

    }

    public static String generatePassword() {
        StringBuilder sb = new StringBuilder(7);
        for (int i = 0; i < 7; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();

    }
    public void DisplayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("reg_success")) {
                    et_email.setText("");
                    et_username.setText("");
                    et_firstname.setText("");
                    et_lastname.setText("");
                    Intent delayed = new Intent(RegisterActivity.this, Manager.class);
                    startActivity(delayed);
                }
                else if (code.equals("reg_failed")) {
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
