package com.example.s215087038.wefixx.manager;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.NewRequest;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.SpinnerAdapter;
import com.example.s215087038.wefixx.adapter.OpenRequestAdapter;
import com.example.s215087038.wefixx.model.DataObject;
import com.example.s215087038.wefixx.model.Request;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
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
import java.util.Calendar;
import java.util.List;


public class ProviderRequests extends Fragment {
    String url = "http://sict-iis.nmmu.ac.za/wefixx/manager/reports/provider_report.php";

    private RelativeLayout mainLayout;
    private PieChart mChart;
    // we're going to display pie chart for smartphones martket shares
    private float yData[] ;//= { 5, 10, 15, 30, 40 };
    private String xData[];// = { "Sony", "Huawei", "LG", "Apple", "Samsung" };
    private Spinner sp_month;
    Calendar cal = Calendar.getInstance();
    String month;
    int selected_month;
    String[] monthName =new String[] { "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December" };
    public ProviderRequests() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        month = monthName[cal.get(Calendar.MONTH)];

        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_provider_requests, container, false);

        mainLayout = (RelativeLayout) myFragmentView.findViewById(R.id.mainLayout);
        sp_month = (Spinner) myFragmentView.findViewById(R.id.sp_month);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, monthName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_month.setAdapter(adapter);

        sp_month.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long fault_id) {
                        //DataObject selected = (DataObject) parent.getItemAtPosition(position);

                        //get selected fault type
                        selected_month = position + 1;
                        //Toast.makeText(getActivity(), monthName[position], Toast.LENGTH_SHORT).show();

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        mChart = new PieChart(getActivity());
        // add pie chart to main layout
        mainLayout.addView(mChart);
        mainLayout.setBackgroundColor(Color.parseColor("#55656C"));

        // configure pie chart
        mChart.setUsePercentValues(true);
        String desc = month + " Requests Per Provider";
        mChart.setDescription(desc);
        mChart.setDescriptionTextSize(18f);

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(1);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;
                String data = xData[e.getXIndex()] +" = " + String.format("%.0f", yData[e.getXIndex()]) + " request(s)" ;
                Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();

                //Toast.makeText(Ratings.this,data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        ////


        ////

        getData();
        // add data

        // customize legends
        Legend l = mChart.getLegend();
        l.setFormSize(18f);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setTextSize(15f);
        l.setXEntrySpace(10);
        l.setYEntrySpace(10);

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
                    xData = new String[array.length()];
                    yData = new float[array.length()];

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting request object from json array
                        JSONObject request = array.getJSONObject(i);

                        //adding the request to request list_open
                        String x = request.getString("provider_name");
                        float y = request.getLong("amount");
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
        xData[i] = x;
        yData[i]=y;
    }

    private void addData() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(18f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }

}
