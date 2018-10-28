package com.example.s215087038.wefixx.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.s215087038.wefixx.History;
import com.example.s215087038.wefixx.LoginActivity;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.model.MySingleton;
import com.example.s215087038.wefixx.rsa.Manage;
import com.example.s215087038.wefixx.rsa.RSA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Manager extends AppCompatActivity {
    ImageButton history, delayed, add_rsa;
    NotificationManager notificationManager;
    private Context mContext;
    private Resources mResources;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/manager/check_delayed.php";

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
            Intent logout = new Intent(Manager.this, LoginActivity.class);
            startActivity(logout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager);
        history = findViewById(R.id.btn_history);
        delayed = findViewById(R.id.btn_delayed);
        add_rsa = findViewById(R.id.btn_add_rsa);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(Manager.this, ProviderHistory.class);
                startActivity(history);
            }
        });
        delayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent delayed = new Intent(Manager.this, ProviderDelayed.class);
                startActivity(delayed);
            }
        });

        add_rsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent delayed = new Intent(Manager.this, RegisterActivity.class);
                startActivity(delayed);
            }
        });
        getData();
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            Integer count = jsonObject.getInt("count");
                            String list;

                            if (count > 0) {
                                JSONArray array = new JSONArray(response);
                                list = jsonObject.getString("provider");
                                for (int i = 1; i < array.length(); i++) {

                                    //getting request object from json array
                                    JSONObject request = array.getJSONObject(i);
                                    if(i < array.length())
                                    {
                                        list = list + ", " + request.getString("provider");
                                    }
                                    else{
                                        list = list + " and " + request.getString("provider");
                                    }
                                }
                                sendNotification(list, count);

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

                return params;
            }
        };
        MySingleton.getInstance(Manager.this).addToRequestque(stringRequest);
    }

    public void sendNotification(String list, int count ) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(Manager.this);
        builder.setSmallIcon(R.drawable.alert);
        Intent intent = new Intent(Manager.this, ProviderDelayed.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(Manager.this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        builder.setContentTitle("Delayed Requests");
        builder.setContentText("There are " + count + " delays by " + list + ".");
        builder.setSubText("Tap to see delayed requests.");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
    }
}
