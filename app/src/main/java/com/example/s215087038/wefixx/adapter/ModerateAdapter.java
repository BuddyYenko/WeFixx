package com.example.s215087038.wefixx.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.model.MySingleton;
import com.example.s215087038.wefixx.model.PriorityDataObject;
import com.example.s215087038.wefixx.model.ProviderDataObject;
import com.example.s215087038.wefixx.model.Request;
import com.example.s215087038.wefixx.rsa.LowFragment;
import com.example.s215087038.wefixx.rsa.Manage;
import com.example.s215087038.wefixx.rsa.ModerateFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModerateAdapter extends RecyclerView.Adapter<ModerateAdapter.MyViewHolder>{
    private List<Request> requestList;
    private Context mCtx;
    ModerateFragment fragment;

    private static int currentPosition = -1;
    AlertDialog.Builder builder;

protected List<ProviderDataObject> providerData;
protected List<PriorityDataObject> priorityData;
        String fault_id, fault_type_id;

public ModerateAdapter(List<Request> requestList) {
        this.requestList = requestList;
        }

public ModerateAdapter(Context mCtx, List<Request> requestList, ModerateFragment fragment) {
        this.mCtx = mCtx;
        this.requestList = requestList;
        this.fragment = fragment;

}

public static class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_fault_id, expected_close, days_overdue, no_photo, request_date, request_type, room, description, textView, date_label, date_assigned, provider, status, file_name, desc_label;
    public ImageView imageView;
    public LinearLayout linearLayout;
    public Button bn_close, view_photo;
    public ImageButton choose_file;

    public MyViewHolder(View view) {
        super(view);
        request_date = (TextView) view.findViewById(R.id.tv_date);
        request_type = (TextView) view.findViewById(R.id.tv_type);
        description = (TextView) view.findViewById(R.id.tv_desc);
        desc_label = (TextView) view.findViewById(R.id.desc_label);
        expected_close = (TextView) view.findViewById(R.id.tv_expected_close);
        days_overdue = (TextView) view.findViewById(R.id.tv_overdue);
        tv_fault_id = (TextView) view.findViewById(R.id.tv_fault_id);

        room = (TextView) view.findViewById(R.id.tv_room);
        textView = (TextView) view.findViewById(R.id.room_label);
        date_label = (TextView) view.findViewById(R.id.date_label);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        status = (TextView) view.findViewById(R.id.tv_status);
        provider = (TextView) view.findViewById(R.id.tv_provider);
        date_assigned = (TextView) view.findViewById(R.id.tv_date_assigned);
        file_name = (TextView) view.findViewById(R.id.tv_file_name);
        choose_file = (ImageButton) view.findViewById(R.id.ib_report);
        bn_close = (Button) view.findViewById(R.id.btn_close);
        no_photo = (TextView) view.findViewById(R.id.tv_no_photo);
        view_photo = (Button) view.findViewById(R.id.btn_view_photo);




    }
}

    @Override
    public ModerateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_moderate, parent, false);

        return new ModerateAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Request request = requestList.get(position);
        holder.request_date.setText(request.getRequestDate());
        holder.request_type.setText(request.getRequestType());
        holder.description.setText(request.getDescription());
        holder.room.setText(request.getRoom());
        holder.textView.setText("Room " + request.getRoom());
        holder.date_label.setText(request.getRequestDate());
        holder.desc_label.setText(request.getDescription());
        holder.status.setText(request.getRequestStatus());
        holder.expected_close.setText(request.getExpectedClose());
        holder.days_overdue.setText(request.getDaysOverdue());
        holder.tv_fault_id.setText(request.getFaultID());

        fault_type_id = request.getFaultTypeID();
        fault_id = request.getFaultID();
        holder.date_assigned.setText(request.getDateAssigned());
        holder.provider.setText(request.getProvider());
        if( request.getImageUrl() != "null") {
            Glide.with(mCtx).load(request.getImageUrl()).into(holder.imageView);
        }
        holder.linearLayout.setVisibility(View.GONE);

        builder = new AlertDialog.Builder(mCtx);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

            //toggling visibility
            holder.linearLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.linearLayout.startAnimation(slideDown);
        }


        //  holder.choose_file.setOnClickListener(mCtx);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloading the list
                notifyDataSetChanged();
            }
        });
        holder.choose_file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                fragment.startActivityForResult(Intent.createChooser(intent, "Select PDF"), 1);
            }
        });
        holder.bn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", holder.tv_fault_id.getText().toString());
                currentPosition = -1;

                //PASS OVER THE BUNDLE TO OUR FRAGMENT
                fragment.setArguments(bundle);
                fragment.getFaultID();
            }
        });
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }
}