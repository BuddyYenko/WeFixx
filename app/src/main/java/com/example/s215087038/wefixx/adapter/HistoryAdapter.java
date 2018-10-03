package com.example.s215087038.wefixx.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.model.Request;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<Request> requestList;
    private Context mCtx;
    private static int currentPosition = -1;
    AlertDialog.Builder builder;
    public HistoryAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public HistoryAdapter(Context mCtx, List<Request> requestList) {
        this.mCtx = mCtx;
        this.requestList = requestList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView no_photo, request_date, request_type, room, description, textView, date_label, priority, provider, date_assigned, date_closed, comment, desc_label;
        public SimpleRatingBar rating;
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
            rating = (SimpleRatingBar) view.findViewById(R.id.rb_rating);
            no_photo = (TextView) view.findViewById(R.id.tv_no_photo);
            view_photo = (Button) view.findViewById(R.id.btn_view_photo);

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

        holder.request_type.setText(request.getRequestType());
        holder.description.setText(request.getDescription());

        holder.room.setText(request.getRoom());
        holder.textView.setText(request.getRoom());
        holder.date_label.setText(request.getRequestDate());
        holder.priority.setText(request.getPriority());
        holder.provider.setText(request.getProvider());
        holder.comment.setText(request.getComment());
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
        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

            //toggling visibility
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.desc_label.setVisibility(View.INVISIBLE);

            //adding sliding effect
            holder.linearLayout.startAnimation(slideDown);
        }

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