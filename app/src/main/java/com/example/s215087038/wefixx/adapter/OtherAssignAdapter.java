package com.example.s215087038.wefixx.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.model.MySingleton;
import com.example.s215087038.wefixx.model.PriorityDataObject;
import com.example.s215087038.wefixx.model.ProviderDataObject;
import com.example.s215087038.wefixx.model.Request;
import com.example.s215087038.wefixx.rsa.Manage;
import com.example.s215087038.wefixx.rsa.OtherAssignFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tooltip.Tooltip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherAssignAdapter extends RecyclerView.Adapter<OtherAssignAdapter.MyViewHolder> {

    private List<Request> requestList;
    private Context mCtx;
    private static int currentPosition = -1;
    String providerUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/fault_provider.php";
    String priorityUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/priority.php";
    String assignUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/update_request.php";
    AlertDialog.Builder builder;
    OtherAssignFragment fragment;
    ProgressDialog progressDialog;

    protected List<ProviderDataObject> providerData;
    protected List<PriorityDataObject> priorityData;
    String provider, priority, fault_id, fault_type_id;

    public OtherAssignAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public OtherAssignAdapter(Context mCtx, List<Request> requestList , OtherAssignFragment fragment) {
        this.mCtx = mCtx;
        this.requestList = requestList;
        this.fragment = fragment;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView no_photo, request_date, request_type, room, description, textView, date_label, desc_label, status, tv_fault_id, tv_fault_type_id;
        public TextView expected_close, days_overdue, tv_provider, tv_priority;
        public ImageView imageView;
        public LinearLayout linearLayout;
        public Spinner sp_priority, sp_provider;
        public Button bn_assign, view_photo;
        public ImageButton tip;

        public MyViewHolder(View view) {
            super(view);
            tip = view.findViewById(R.id.tip);
            sp_priority = (Spinner) view.findViewById(R.id.sp_priority);
            sp_provider = (Spinner) view.findViewById(R.id.sp_provider);
            bn_assign = (Button) view.findViewById(R.id.btn_submit);
            request_date = (TextView) view.findViewById(R.id.tv_date);
            request_type = (TextView) view.findViewById(R.id.tv_type);
            description = (TextView) view.findViewById(R.id.tv_desc);
            room = (TextView) view.findViewById(R.id.tv_room);
            textView = (TextView) view.findViewById(R.id.room_label);
            date_label = (TextView) view.findViewById(R.id.date_label);
            desc_label = (TextView) view.findViewById(R.id.desc_label);
            status = (TextView) view.findViewById(R.id.tv_status);
            tv_fault_id = (TextView) view.findViewById(R.id.tv_fault_id);
            tv_fault_type_id = (TextView) view.findViewById(R.id.tv_fault_type_id);
            imageView = (ImageView)view.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            no_photo = (TextView) view.findViewById(R.id.tv_no_photo);
            view_photo = (Button) view.findViewById(R.id.btn_view_photo);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_carpentry_assign, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.tip:
                        priorityTooltip(v, Gravity.BOTTOM);
                        break;
                }
            }
        });
        final Request request = requestList.get(position);
        holder.request_date.setText(request.getRequestDate());
        holder.request_type.setText(request.getRequestType());
        holder.description.setText(request.getDescription());
        holder.room.setText(request.getRoom());
        holder.textView.setText("Room " + request.getRoom());
        holder.date_label.setText(request.getRequestDate());
        holder.desc_label.setText(request.getDescription());
        holder.status.setText(request.getRequestStatus());

        fault_type_id = request.getFaultTypeID();
        fault_id = request.getFaultID();
        holder.tv_fault_type_id.setText(request.getFaultTypeID());
        holder.tv_fault_id.setText(request.getFaultID());
        if (request.getImageUrl() != "null") {
            Glide.with(mCtx).load(request.getImageUrl()).into(holder.imageView);
        }
        holder.linearLayout.setVisibility(View.GONE);
        holder.tv_fault_type_id.setText(request.getFaultTypeID());
        holder.tv_fault_id.setText(request.getFaultID());
        holder.view_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.view_photo.setVisibility(View.GONE);
                if (request.getImageUrl() != "null") {
                    holder.imageView.setVisibility(View.VISIBLE);
                } else {
                    holder.no_photo.setVisibility(View.VISIBLE);
                    //holder.hide_photo.setVisibility(View.VISIBLE);
                }

            }
        });

        builder = new AlertDialog.Builder(mCtx);
            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, providerUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            providerData = Arrays.asList(mGson.fromJson(response, ProviderDataObject[].class));
                            //display first question to the user
                            if (null != providerData) {
                                holder.sp_provider.setOnItemSelectedListener(
                                        new AdapterView.OnItemSelectedListener() {
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long fault_id) {
                                                ProviderDataObject selected = (ProviderDataObject) parent.getItemAtPosition(position);
                                                //get selected fault type
                                                provider = selected.getID();
                                            }
                                            public void onNothingSelected(AdapterView<?> parent) {
                                            }
                                        });
                                assert holder.sp_provider != null;
                                ProviderSpinnerAdapter spinnerAdapter = new ProviderSpinnerAdapter(mCtx, providerData);
                                holder.sp_provider.setAdapter(spinnerAdapter);
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
                    params.put("fault_type_id", holder.tv_fault_type_id.getText().toString());
                    return params;
                }
            };
            MySingleton.getInstance(mCtx).addToRequestque(stringRequest);

            RequestQueue que = Volley.newRequestQueue(mCtx);
            StringRequest stringReq = new StringRequest(com.android.volley.Request.Method.GET, priorityUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    priorityData = Arrays.asList(mGson.fromJson(response, PriorityDataObject[].class));
                    //display first question to the user
                    if (null != priorityData) {
                        holder.sp_priority.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long provider_id) {
                                        PriorityDataObject selected = (PriorityDataObject) parent.getItemAtPosition(position);
                                        //get selected fault type
                                        priority = selected.getID();
                                    }

                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                        assert holder.sp_priority != null;
                        PrioritySpinnerAdapter spinnerAdapter = new PrioritySpinnerAdapter(mCtx, priorityData);
                        holder.sp_priority.setAdapter(spinnerAdapter);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            que.add(stringReq);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

            //toggling visibility
            holder.linearLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.linearLayout.startAnimation(slideDown);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloading the list
                notifyDataSetChanged();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloading the list
                notifyDataSetChanged();
            }
        });
            holder.bn_assign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, assignUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");
                                        dismissProgressBar();

                                        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                                        builder.setTitle("WeFixx Response");
                                        builder.setMessage(message);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                currentPosition = -1;
                                                fragment.prepareRequestData();
                                                notifyDataSetChanged();
                                            }
                                        });
                                        builder.show();

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

                            params.put("fault_id", holder.tv_fault_id.getText().toString());
                            params.put("priority", priority);
                            params.put("provider", provider);

                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy( 30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(mCtx).addToRequestque(stringRequest);

                }

                public void DisplayAlert() {
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent refresh = new Intent(mCtx, Manage.class);
                            mCtx.startActivity(refresh);
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
    }
    private void priorityTooltip(View v, int gravity) {
        ImageView imgV =(ImageView) v;
        Tooltip tooltip = new Tooltip.Builder(imgV)
                .setText("Specifies the request turnaround time.\nLow: 3-7 days\nModerate: 2-3 days\nCritical: 1-2 days")
                .setTextColor(Color.BLACK)
                .setGravity(gravity)
                .setCornerRadius(8f)
                .setBackgroundColor(Color.LTGRAY)
                .setDismissOnClick(true)
                .show();
    }
    @Override
    public int getItemCount() {
        return requestList.size();
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Assigning Request ...");
        progressDialog.show();
    }
    private void  dismissProgressBar() {
        // To Dismiss progress dialog
        progressDialog.dismiss();
    }
}