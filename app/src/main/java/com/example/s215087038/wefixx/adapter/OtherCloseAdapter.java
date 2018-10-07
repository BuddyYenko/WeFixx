package com.example.s215087038.wefixx.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.model.PriorityDataObject;
import com.example.s215087038.wefixx.model.ProviderDataObject;
import com.example.s215087038.wefixx.model.Request;

import java.util.List;

public class OtherCloseAdapter extends RecyclerView.Adapter<OtherCloseAdapter.MyViewHolder> {

    private List<Request> requestList;
    private Context mCtx;
    private static int currentPosition = -1;
    String providerUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/fault_provider.php";
    String priorityUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/priority.php";
    String assignUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/update_request.php";
    AlertDialog.Builder builder;

    protected List<ProviderDataObject> providerData;
    protected List<PriorityDataObject> priorityData;
    String provider, priority, fault_id, fault_type_id;

    public OtherCloseAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public OtherCloseAdapter(Context mCtx, List<Request> requestList) {
        this.mCtx = mCtx;
        this.requestList = requestList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView no_photo, request_date, request_type, room, description, textView, date_label, desc_label, status, tv_fault_id;
        public TextView expected_close, days_overdue, tv_provider, tv_priority;
        public ImageView imageView;
        public LinearLayout linearLayout;
        public Spinner sp_priority, sp_provider;
        public Button bn_assign, view_photo;

        public MyViewHolder(View view) {
            super(view);

            expected_close = (TextView) view.findViewById(R.id.tv_expected_close);
            days_overdue = (TextView) view.findViewById(R.id.tv_overdue);
            tv_priority = (TextView) view.findViewById(R.id.tv_priority);
            tv_provider = (TextView) view.findViewById(R.id.tv_provider);
            request_date = (TextView) view.findViewById(R.id.tv_date);
            request_type = (TextView) view.findViewById(R.id.tv_type);
            description = (TextView) view.findViewById(R.id.tv_desc);
            room = (TextView) view.findViewById(R.id.tv_room);
            textView = (TextView) view.findViewById(R.id.room_label);
            date_label = (TextView) view.findViewById(R.id.date_label);
            desc_label = (TextView) view.findViewById(R.id.desc_label);
            status = (TextView) view.findViewById(R.id.tv_status);
            imageView = (ImageView)view.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            no_photo = (TextView) view.findViewById(R.id.tv_no_photo);
            view_photo = (Button) view.findViewById(R.id.btn_view_photo);
            tv_fault_id = (TextView) view.findViewById(R.id.tv_fault_id);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_carpentry_close, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Request request = requestList.get(position);
        holder.request_date.setText(request.getRequestDate());
        holder.request_type.setText(request.getRequestType());
        holder.description.setText(request.getDescription());
        holder.room.setText(request.getRoom());
        holder.textView.setText(request.getRoom());
        holder.date_label.setText(request.getRequestDate());
        holder.desc_label.setText(request.getDescription());
        holder.status.setText(request.getRequestStatus());

        fault_type_id = request.getFaultTypeID();
        fault_id = request.getFaultID();
        if (request.getImageUrl() != "null") {
            Glide.with(mCtx).load(request.getImageUrl()).into(holder.imageView);
        }
        holder.linearLayout.setVisibility(View.GONE);

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
        holder.expected_close.setText(request.getExpectedClose());
        holder.tv_provider.setText(request.getProvider());
        holder.tv_priority.setText(request.getPriority());
        holder.days_overdue.setText(request.getDaysOverdue());

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
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}