package com.example.s215087038.wefixx.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.model.FaqDataObject;
import com.example.s215087038.wefixx.model.Request;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.MyViewHolder> {

    private List<FaqDataObject> faqList;
    private Context mCtx;
    private static int currentPosition = -1;
    AlertDialog.Builder builder;
    public FaqAdapter(List<FaqDataObject> faqList) {
        this.faqList = faqList;
    }

    public FaqAdapter(Context mCtx, List<FaqDataObject> faqList) {
        this.mCtx = mCtx;
        this.faqList = faqList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView question, answer;
        public LinearLayout linearLayout, row;

        public MyViewHolder(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.question_label);
            answer = (TextView) view.findViewById(R.id.tv_answer);
            row = (LinearLayout) view.findViewById(R.id.row);
            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_faq, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        FaqDataObject faq = faqList.get(position);
        holder.question.setText(faq.getQuestion());
        holder.answer.setText(faq.getAnswer());
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

        holder.row.setOnClickListener(new View.OnClickListener() {
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
        return faqList.size();
    }
}