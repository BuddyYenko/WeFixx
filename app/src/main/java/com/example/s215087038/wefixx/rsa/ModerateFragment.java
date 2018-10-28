package com.example.s215087038.wefixx.rsa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.FilePath;
import com.example.s215087038.wefixx.MyDividerItemDecoration;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.adapter.AssignedRequestAdapter;
import com.example.s215087038.wefixx.adapter.CarpentryCloseAdapter;
import com.example.s215087038.wefixx.adapter.ModerateAdapter;
import com.example.s215087038.wefixx.model.Request;
import com.example.s215087038.wefixx.model.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ModerateFragment extends Fragment {
        private List<Request> moderateList;
        private RecyclerView moderateRecyclerView;
        private AssignedRequestAdapter aAdapter;
        String moderateUrl = "http://sict-iis.nmmu.ac.za/wefixx/rsa/moderate.php";
        String id, path;
        String UPLOAD_URL = "http://sict-iis.nmmu.ac.za/wefixx/rsa/update_request.php";
        Uri fileuri;
        byte[] result;
        AlertDialog.Builder builder;

    private static final String LOG_TAG = AssignedFragment.class.getSimpleName();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ModerateFragment() {
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
            View myFragmentView = inflater.inflate(R.layout.fragment_moderate, container, false);
            moderateList = new ArrayList<>();
            moderateRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.moderateRecylcerView);

            aAdapter = new AssignedRequestAdapter(moderateList);

            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            moderateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            moderateRecyclerView.setItemAnimator(new DefaultItemAnimator());
            moderateRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
            moderateRecyclerView.setAdapter(aAdapter);
            builder = new AlertDialog.Builder(getActivity());

            prepareRequestData();

            mSwipeRefreshLayout = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.swiperefresh);
            mSwipeRefreshLayout.setColorScheme(
                    R.color.swipe_color_1, R.color.swipe_color_2,
                    R.color.swipe_color_3, R.color.swipe_color_4);

            return myFragmentView;
        }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                initiateRefresh();
            }
        });

    }
    private void initiateRefresh() {
        Log.i(LOG_TAG, "initiateRefresh");

        /**
         * Execute the background task, which uses {@link android.os.AsyncTask} to load the data.
         */
        new ModerateFragment.DummyBackgroundTask().execute();
    }
    private void onRefreshComplete(List<String> result) {
        Log.i(LOG_TAG, "onRefreshComplete");

        // Remove all items from the ListAdapter, and then replace them with the new items
        prepareRequestData();
        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }
    private class DummyBackgroundTask extends AsyncTask<Void, Void, List<String>> {

        static final int TASK_DURATION = 3 * 1000; // 3 seconds

        @Override
        protected List<String> doInBackground(Void... params) {
            // Sleep for a small amount of time to simulate a background-task
            try {
                Thread.sleep(TASK_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Return a new random list of cheeses
            return null;//Cheeses.randomList(LIST_ITEM_COUNT);
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            // Tell the Fragment that the refresh has completed
            onRefreshComplete(result);
        }

    }

        private void prepareRequestData() {


            RequestQueue queue = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest1 = new StringRequest(com.android.volley.Request.Method.GET, moderateUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //converting the string to json array object
                        JSONArray array = new JSONArray(response);
                        moderateList.clear();

                        //traversing through all the object
                        for (int i = 0; i < array.length(); i++) {

                            //getting request object from json array
                            JSONObject request = array.getJSONObject(i);

                            //adding the request to request list_open
                            moderateList.add(new Request(
                                    request.getString("fault_id"),
                                    request.getString("request_date"),
                                    request.getString("date_assigned"),
                                    request.getString("expected_close"),
                                    request.getString("days_overdue"),
                                    request.getString("room"),
                                    request.getString("request_type"),
                                    request.getString("description"),
                                    request.getString("provider"),
                                    request.getString("status"),
                                    request.getString("photo")
                            ));

                        }

                        //creating adapter object and setting it to recyclerview
                        ModerateAdapter adapter = new ModerateAdapter(getActivity(), moderateList, ModerateFragment.this);
                        moderateRecyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            queue.add(stringRequest1);

        }
    public void getFaultID(){
        id =this.getArguments().getString("id").toString();
        if(fileuri != null && !fileuri.equals(Uri.EMPTY)) {

            String path = FilePath.getPath(getActivity(), fileuri);

            //Toast.makeText(ManageByProvider.this,id + " " + path ,Toast.LENGTH_SHORT).show();

            try {

                File file = new File(path);
                int length = (int) file.length();
                BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
                byte[] bytes = new byte[length];
                reader.read(bytes, 0, length);
                reader.close();
                result = bytes;

                //encodedfile = Base64.encodeBase64(bytes).toString();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch <span id="IL_AD1" class="IL_AD">block</span>
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(com.android.volley.Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            try {
                                //   Toast.makeText(ManageByProvider.this, response.data.toString() + " " + response.toString(), Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = new JSONArray(new String(response.data));
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");

                                builder.setTitle("WeFixx Response");
                                builder.setMessage(message);
                                DisplayAlert(code);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Toast.makeText(getApplicationContext(), error.getMessage() +"error " , Toast.LENGTH_LONG).show();

                        }
                    }) {

                //Posting parameters to php script
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("fault_id", id);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {


                    Map<String, DataPart> params = new HashMap<>();
                    if (fileuri != null) {
                        String imagename = "report";
                        params.put("report", new DataPart(imagename + ".pdf", result));
                    }
                    return params;
                }

            };
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //adding the request to volley
            Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
        }
        else{
            builder.setTitle("No Report Chosen");
            builder.setMessage("Please select a document to upload");
            DisplayAlert("input_error");
        }
    }

    public void DisplayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prepareRequestData();
            }
        });
        prepareRequestData();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onActivityResult(int req, int result, Intent data)
    {
        ModerateAdapter adapter = new ModerateAdapter(getActivity(), moderateList, ModerateFragment.this);
        super.onActivityResult(req, result, data);
        if (result == RESULT_OK)
        {
            fileuri = data.getData();
            String file = fileuri.getPath();
            path = FilePath.getPath(getActivity(), fileuri);
            Toast.makeText(getActivity(), "Document chosen: " + path, Toast.LENGTH_LONG).show();

            //adapter = new CarpentryCloseAdapter(path);

        }
        else
        {
            Toast.makeText(getActivity(), "result " +result, Toast.LENGTH_SHORT).show();

        }

    }

    }
