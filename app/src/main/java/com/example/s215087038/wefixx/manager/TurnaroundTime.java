package com.example.s215087038.wefixx.manager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s215087038.wefixx.R;


public class TurnaroundTime extends Fragment {
    String url = "http://sict-iis.nmmu.ac.za/wefixx/rsa/reports/.php";

    public TurnaroundTime() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_turnaround_time, container, false);

        //  openRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.openRecylcerView);


        return myFragmentView;
    }
}