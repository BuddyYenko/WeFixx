package com.example.s215087038.wefixx.rsa;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.models.Request;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private List<Request> requestList;
    private Context mCtx;

    public RequestAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public RequestAdapter(Context mCtx,List<Request> requestList) {
        this.mCtx = mCtx;
        this.requestList = requestList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView request_date, request_type, room, description;

        public MyViewHolder(View view) {
            super(view);
            request_date = (TextView) view.findViewById(R.id.request_date);
            request_type = (TextView) view.findViewById(R.id.request_type);
            description = (TextView) view.findViewById(R.id.description);
            room = (TextView) view.findViewById(R.id.room);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Request request = requestList.get(position);
        holder.request_date.setText(request.getRequestDate());
        holder.request_type.setText(request.getRequestType());
        holder.description.setText(request.getDescription());
        holder.room.setText(request.getRoom());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}