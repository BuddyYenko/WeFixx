package com.example.s215087038.wefixx.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HAdapter extends RecyclerView.Adapter<HAdapter.MyViewHolder> {

    private List<Request> requestList;
    private Context mCtx;
    private static int currentPosition = -1;
    String historyUrl = "http://sict-iis.nmmu.ac.za/wefixx/history.php";


    protected List<ProviderDataObject> providerData;
    protected List<PriorityDataObject> priorityData;

    public HAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public HAdapter(Context mCtx, List<Request> requestList) {
        this.mCtx = mCtx;
        this.requestList = requestList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView no_photo, request_date, request_type, room, description, textView, date_label, priority, provider, date_assigned, date_closed, comment, desc_label, tv_requester;
        public ImageView imageView;
        public LinearLayout linearLayout, row;
        public Button view_photo;

        public MyViewHolder(View view) {
            super(view);
            request_date = (TextView) view.findViewById(R.id.tv_date);
            date_assigned = (TextView) view.findViewById(R.id.tv_date_assigned);
            date_closed = (TextView) view.findViewById(R.id.tv_date_closed);
            desc_label = (TextView) view.findViewById(R.id.desc_label);

            request_type = (TextView) view.findViewById(R.id.tv_type);
            description = (TextView) view.findViewById(R.id.tv_desc);
            room = (TextView) view.findViewById(R.id.tv_room);
            textView = (TextView) view.findViewById(R.id.room_label);
            date_label = (TextView) view.findViewById(R.id.date_label);
            imageView = (ImageView)view.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            priority = (TextView) view.findViewById(R.id.tv_priority);
            provider = (TextView) view.findViewById(R.id.tv_provider);
            comment = (TextView) view.findViewById(R.id.tv_comment);
            no_photo = (TextView) view.findViewById(R.id.tv_no_photo);
            view_photo = (Button) view.findViewById(R.id.btn_view_photo);
            tv_requester = (TextView) view.findViewById(R.id.tv_requester);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_history, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Request request = requestList.get(position);
        holder.request_date.setText(request.getRequestDate());
        holder.date_assigned.setText(request.getDateAssigned());
        holder.date_closed.setText(request.getDateClosed());
        holder.desc_label.setText(request.getDescription());
        holder.tv_requester.setText(request.getRequester());

        holder.request_type.setText(request.getRequestType());
        holder.description.setText(request.getDescription());

        holder.room.setText(request.getRoom());
        holder.textView.setText(request.getRoom());
        holder.date_label.setText(request.getRequestDate());
        holder.priority.setText(request.getPriority());
        holder.provider.setText(request.getProvider());
        holder.comment.setText(request.getComment());

        if( request.getImageUrl() != "null") {
            Glide.with(mCtx).load(request.getImageUrl()).into(holder.imageView);
        }
        holder.linearLayout.setVisibility(View.GONE);
        holder.view_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.view_photo.setVisibility(View.GONE);
                if(request.getImageUrl()!= "null"){
                    holder.imageView.setVisibility(View.VISIBLE);

                }
                else{
                    holder.no_photo.setVisibility(View.VISIBLE);
                    //holder.hide_photo.setVisibility(View.VISIBLE);
                }

            }
        });

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

            //toggling visibility
            holder.linearLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.linearLayout.startAnimation(slideDown);
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloading the list
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}