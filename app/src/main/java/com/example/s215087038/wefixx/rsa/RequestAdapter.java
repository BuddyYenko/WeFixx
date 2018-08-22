package com.example.s215087038.wefixx.rsa;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.models.Request;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private List<Request> requestList;
    private Context mCtx;
    private static int currentPosition = 0;

    public RequestAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public RequestAdapter(Context mCtx,List<Request> requestList) {
        this.mCtx = mCtx;
        this.requestList = requestList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView request_date, request_type, room, description, textView;
        public ImageView imageView;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            request_date = (TextView) view.findViewById(R.id.tv_date);
            request_type = (TextView) view.findViewById(R.id.tv_type);
            description = (TextView) view.findViewById(R.id.tv_desc);
            room = (TextView) view.findViewById(R.id.tv_room);
            textView = (TextView) view.findViewById(R.id.room);
            imageView = (ImageView)view.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Request request = requestList.get(position);
        holder.request_date.setText(request.getRequestDate());
        holder.request_type.setText(request.getRequestType());
        holder.description.setText(request.getDescription());
        holder.room.setText(request.getRoom());
        Glide.with(mCtx).load(request.getImageUrl()).into(holder.imageView);
        holder.linearLayout.setVisibility(View.GONE);

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

                //reloding the list
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}