package com.example.s215087038.wefixx.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.model.PriorityDataObject;
import com.example.s215087038.wefixx.model.ProviderDataObject;
import com.example.s215087038.wefixx.model.Request;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.List;

public class StudentClosedAdapter extends  RecyclerView.Adapter<StudentClosedAdapter.MyViewHolder> {

    private List<Request> requestList;
    private Context mCtx;
    private static Context context = null;

    private static int currentPosition = -1;
    AlertDialog.Builder builder;

    protected List<ProviderDataObject> providerData;
    protected List<PriorityDataObject> priorityData;
    String fault_id, fault_type_id;

    public StudentClosedAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public StudentClosedAdapter(Context mCtx, List<Request> requestList) {
        this.mCtx = mCtx;
        this.requestList = requestList;
        this.context = mCtx;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView no_photo, request_date, request_type, room, description,comment, textView, date_label, date_closed, date_assigned, provider, priority, file_name;
        public ImageView imageView;
        public LinearLayout linearLayout, row;
        public Button view_photo;
        public SimpleRatingBar rating;


        public MyViewHolder(View view) {
            super(view);
            request_date = (TextView) view.findViewById(R.id.tv_date_opened);
            request_type = (TextView) view.findViewById(R.id.tv_type);
            description = (TextView) view.findViewById(R.id.tv_desc);
            room = (TextView) view.findViewById(R.id.tv_room);
            textView = (TextView) view.findViewById(R.id.room_label);
            date_label = (TextView) view.findViewById(R.id.date_label);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            priority = (TextView) view.findViewById(R.id.tv_priority);
            provider = (TextView) view.findViewById(R.id.tv_provider);
            date_assigned = (TextView) view.findViewById(R.id.tv_date_assigned);
            date_closed= (TextView) view.findViewById(R.id.tv_date_closed);
            comment = (TextView) view.findViewById(R.id.tv_comment);
            rating = (SimpleRatingBar) view.findViewById(R.id.rb_rating);
            row = (LinearLayout) itemView.findViewById(R.id.row);
            no_photo = (TextView) view.findViewById(R.id.tv_no_photo);
            view_photo = (Button) view.findViewById(R.id.btn_view_photo);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_student_closed, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Request request = requestList.get(position);
        holder.request_date.setText(request.getRequestDate());
        holder.date_assigned.setText(request.getDateAssigned());
        holder.date_closed.setText(request.getDateClosed());

        holder.request_type.setText(request.getRequestType());
        holder.description.setText(request.getDescription());
        //holder.room.setText(request.getRoom());
        holder.textView.setText(request.getRoom());
        holder.date_label.setText(request.getRequestDate());

        fault_type_id = request.getFaultTypeID();
        fault_id = request.getFaultID();
        holder.date_assigned.setText(request.getDateAssigned());
        holder.provider.setText(request.getProvider());
        holder.priority.setText(request.getPriority());
        if(request.getComment() != "null"){
            holder.comment.setText(request.getComment());
        }else{
            holder.comment.setText("***No Comment***");
        }
            holder.rating.setRating(request.getRating());

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

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloading the list
                notifyDataSetChanged();
            }
        });
      //  holder.choose_file.setOnClickListener(mCtx);



    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }
}