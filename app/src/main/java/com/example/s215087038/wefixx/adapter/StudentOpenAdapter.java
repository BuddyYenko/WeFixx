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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.model.PriorityDataObject;
import com.example.s215087038.wefixx.model.ProviderDataObject;
import com.example.s215087038.wefixx.model.Request;

import java.util.List;

public class StudentOpenAdapter extends  RecyclerView.Adapter<StudentOpenAdapter.MyViewHolder> {

    private List<Request> requestList;
    private Context mCtx;
    private static Context context = null;

    private static int currentPosition = -1;
    String closeUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/update_request.php";
    AlertDialog.Builder builder;

    protected List<ProviderDataObject> providerData;
    protected List<PriorityDataObject> priorityData;
    String fault_id, fault_type_id;

    public StudentOpenAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public StudentOpenAdapter(Context mCtx, List<Request> requestList) {
        this.mCtx = mCtx;
        this.requestList = requestList;
        this.context = mCtx;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView request_date, request_type, room, description, textView, date_label, date_assigned, provider, priority, file_name;
        public ImageView imageView;
        public LinearLayout linearLayout;
        public Button bn_close;
        public ImageButton choose_file;

        public MyViewHolder(View view) {
            super(view);
            request_date = (TextView) view.findViewById(R.id.tv_date);
            request_type = (TextView) view.findViewById(R.id.tv_type);
            description = (TextView) view.findViewById(R.id.tv_desc);
            room = (TextView) view.findViewById(R.id.tv_room);
            textView = (TextView) view.findViewById(R.id.room_label);
            date_label = (TextView) view.findViewById(R.id.date_label);
            imageView = (ImageView)view.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            priority = (TextView) view.findViewById(R.id.tv_priority);
            provider = (TextView) view.findViewById(R.id.tv_provider);
            date_assigned = (TextView) view.findViewById(R.id.tv_date_assigned);
            file_name = (TextView) view.findViewById(R.id.tv_file_name);
            choose_file = (ImageButton) view.findViewById(R.id.ib_report);
            bn_close = (Button) view.findViewById(R.id.btn_close);

            choose_file.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();

                    intent.setType("application/pdf");

                    intent.setAction(Intent.ACTION_GET_CONTENT);

                   // startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ_CODE);

                }
            });


        }
        public  void onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.d("MyAdapter", "onActivityResult");
            //.onActivityResult(requestCode, resultCode, data);
//
//            if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//                filePath = data.getData();
//            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_assigned, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Request request = requestList.get(position);
        holder.request_date.setText(request.getRequestDate());
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

        Glide.with(mCtx).load(request.getImageUrl()).into(holder.imageView);
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

        holder.textView.setOnClickListener(new View.OnClickListener() {
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