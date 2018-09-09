package com.example.s215087038.wefixx.manager;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.adapter.OpenRequestAdapter;
import com.example.s215087038.wefixx.model.Request;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Ratings extends Fragment {
    String url = "http://sict-iis.nmmu.ac.za/wefixx/manager/reports/ratings_provider.php";
    ArrayList<String> labels = new ArrayList<>();
    ArrayList<BarDataSet> dataSets;
    ArrayList<BarEntry> entries = new ArrayList<>();
    BarData data;
    BarChart chart;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_ratings, container, false);

        //getData();
        //  openRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.openRecylcerView);

         chart = (BarChart)myFragmentView.findViewById(R.id.chart);
        getData();

      //  data = new BarData(getXAxisValues(), getDataSet() );


        return myFragmentView;
    }
    public void getData(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);
                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting request object from json array
                        JSONObject request = array.getJSONObject(i);

                        //adding the request to request list_open
                        String x = request.getString("provider_name");
                        float y = request.getLong("rating");
                        populateAxis( i,x, y );

                    }
                    addData();

                    //creating adapter object and setting it to recyclerview

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }
    public void populateAxis(int i, String x, float y){
        labels.add(x);
        entries.add(new BarEntry(y, i));
    }

    public  void addData(){
        BarDataSet barDataSet1 = new BarDataSet(entries, "Brand 1");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        data = new BarData(labels, dataSets );
        data.setValueTextSize(15f);
        chart.setData(data);
        chart.setDescription("Maintenance Provider Ratings");
        chart.setDescriptionTextSize(15f);

        chart.animateXY(2000, 5000);
        chart.setDrawGridBackground(true);
        chart.invalidate();
        XAxis  x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setTextSize(15f);



        chart.getLegend().setEnabled(false);
//        l.setFormSize(18f);
//        l.setTextSize(15f);
//        l.setXEntrySpace(10);
//        l.setYEntrySpace(10);
    }
}