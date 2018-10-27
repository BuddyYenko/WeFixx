package com.example.s215087038.wefixx.manager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.s215087038.wefixx.LoginActivity;
import com.example.s215087038.wefixx.model.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.adapter.ProviderHistoryAdapter;
import com.example.s215087038.wefixx.adapter.ProviderSpinnerAdapter;
import com.example.s215087038.wefixx.model.ProviderDataObject;
import com.example.s215087038.wefixx.rsa.RSA;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProviderHistory extends AppCompatActivity {
    public Spinner sp_provider;
    protected List<ProviderDataObject> spinnerData;
    String provider, sProvider, provider_id;
    String url = "http://sict-iis.nmmu.ac.za/wefixx/rsa/providers.php";
    String provider_history = "http://sict-iis.nmmu.ac.za/wefixx/manager/provider_history.php";
    private List<Request> requestList;
    private RecyclerView requestRecyclerView;
    private ProviderHistoryAdapter aAdapter;
    TextView tv_details, tv_provider, tv_count;
    AlertDialog.Builder builder;
    Button btn_go;
    SimpleDateFormat dateFormatter, dateFormatter2;

    int mYear, mMonth, mDay;
    String to, from, date_to, date_from, from_formatted, to_formatted;
    TextView tvFrom, tvTo;
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
            Intent logout = new Intent(ProviderHistory.this, LoginActivity.class);
            startActivity(logout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_history);

        requestList = new ArrayList<>();
        requestRecyclerView = (RecyclerView) findViewById(R.id.requestRecylcerView);
        tv_details = (TextView) findViewById(R.id.tv_details);
        tv_provider = (TextView) findViewById(R.id.tv_provider);
        tv_count = findViewById(R.id.tv_count);
        btn_go = findViewById(R.id.btn_go);

        aAdapter = new ProviderHistoryAdapter(requestList);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(ProviderHistory.this));
        requestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        requestRecyclerView.addItemDecoration(new MyDividerItemDecoration(ProviderHistory.this, LinearLayoutManager.VERTICAL, 16));
        requestRecyclerView.setAdapter(aAdapter);
        requestJsonObject();


        builder = new AlertDialog.Builder(ProviderHistory.this);

        tvTo = (TextView) findViewById(R.id.tv_to);
        tvFrom = (TextView) findViewById(R.id.tv_from);

        Date now = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -15);
        Date dateBefore30Days = cal.getTime();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");

        tvFrom.setText(dateFormatter.format(dateBefore30Days));
        tvTo.setText(dateFormatter.format(now));
        date_from = tvFrom.getText().toString();
        date_to = tvTo.getText().toString();
        from_formatted = dateFormatter2.format(dateBefore30Days);
        to_formatted = dateFormatter2.format(now);

        //prepareRequestData("1");

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareRequestData(provider);
            }
        });
        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(ProviderHistory.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                if (year < mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear,mMonth,mDay);
                                {
                                    from = "0" + dayOfMonth;
                                }

                                SimpleDateFormat dateFormatter;
                                dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

                                Calendar calander = Calendar.getInstance();
                                calander.setTimeInMillis(0);
                                calander.set(year, monthOfYear, dayOfMonth, 0, 0, 0);

                                tvFrom.setText(dateFormatter.format(calander.getTime()));
                                from_formatted = dateFormatter2.format(calander.getTime());
                                date_from = tvFrom.getText().toString();

                            }
                        }, mYear, mMonth, mDay);
                //dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });
        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(ProviderHistory.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                if (year < mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear,mMonth,mDay);
                                {
                                    to = "0" + dayOfMonth;
                                }

                                SimpleDateFormat dateFormatter;
                                dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

                                Calendar calander = Calendar.getInstance();
                                calander.setTimeInMillis(0);
                                calander.set(year, monthOfYear, dayOfMonth, 0, 0, 0);

                                tvTo.setText(dateFormatter.format(calander.getTime()));
                                to_formatted = dateFormatter2.format(calander.getTime());
                                date_to = tvTo.getText().toString();

                            }
                        }, mYear, mMonth, mDay);
                //dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });
    }

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
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
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
                                    sProvider = selected.getName();
                                    tv_provider.setText(sProvider);
                                    prepareRequestData(provider);
                                }
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                    assert sp_provider != null;
                    ProviderSpinnerAdapter spinnerAdapter = new ProviderSpinnerAdapter(ProviderHistory.this, spinnerData);
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
        StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.POST, provider_history, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    requestList.clear();
                    tv_count.setText("Amount: 0");

                    for (int i = 0; i < array.length(); i++) {

                            //getting request object from json array
                        JSONObject request = array.getJSONObject(i);

                            //adding the request to request list_open
                        requestList.add(new Request(
                                request.getString("request_date"),
                                request.getString("date_assigned"),
                                request.getString("date_closed"),
                                request.getString("request_type"),
                                request.getString("description"),
                                request.getString("room"),
                                request.getString("comment"),
                                request.getLong("rating"),
                                request.getString("provider"),
                                request.getString("priority"),
                                request.getString("requester"),
                                request.getString("photo")
                        ));
                        int count = request.getInt("count");
                        tv_count.setText("Amount: " + count);

                        }

                    //creating adapter object and setting it to recyclerview
                    ProviderHistoryAdapter adapter = new ProviderHistoryAdapter(ProviderHistory.this, requestList);
                    requestRecyclerView.setAdapter(adapter);
                    if(requestList == null || requestList.isEmpty()){
                        tv_details.setText("No requests between " + date_from + " and " + date_to);
                        tv_details.setVisibility(View.VISIBLE);
                        requestRecyclerView.setVisibility(View.INVISIBLE);
                    }
                    else{
                        tv_details.setVisibility(View.INVISIBLE);
                        requestRecyclerView.setVisibility(View.VISIBLE);

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

                params.put("from", from_formatted);
                params.put("to", to_formatted);
                params.put("provider", provider);
                params.put("provider", provider);
                return params;
            }
        };
        queue.add(stringRequest1);


    }
}
