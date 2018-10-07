package com.example.s215087038.wefixx.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ByteArrayPool;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.s215087038.wefixx.FilePath;
import com.example.s215087038.wefixx.MainActivity;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.model.MySingleton;
import com.example.s215087038.wefixx.model.PriorityDataObject;
import com.example.s215087038.wefixx.model.ProviderDataObject;
import com.example.s215087038.wefixx.model.Request;
import com.example.s215087038.wefixx.rsa.Manage;
import com.example.s215087038.wefixx.rsa.ManageByProvider;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.s215087038.wefixx.EndPoints.UPLOAD_URL;

public class ByProviderAdapter extends RecyclerView.Adapter<ByProviderAdapter.MyViewHolder>{
    private List<Request> requestList;
    private Context mCtx;
    private static Context context = null;
    private Uri filePath;
    Uri uri;
    File file ;
    String stringPath;
    String path;
    private static int currentPosition = -1;
    String UPLOAD_URL = "http://sict-iis.nmmu.ac.za/wefixx/rsa/update_request.php";
    AlertDialog.Builder builder;

    String fault_id, fault_type_id;

    public ByProviderAdapter() {
    }
    public ByProviderAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public ByProviderAdapter(Context mCtx, List<Request> requestList) {
        this.mCtx = mCtx;
        this.requestList = requestList;
        this.context = mCtx;
    }
    public ByProviderAdapter(String path) {
        this.path = path;
        this.stringPath = "ssss";
        notifyDataSetChanged();
    }
   // public void onActivityResult(int req, int result, Intent data) {
     //   Log.d("ManageByProvider", "onActivityResult");
//        if (result == RESULT_OK) {
//            uri = data.getData();
//            File myFile = new File(uri.toString());
//            //filePath = myFile.getAbsolutePath();
//            //filePath = data.getData();
//            //String docFilePath = getFileNameByUri(mCtx, uri);
//            Toast.makeText(mCtx, "path ada " + uri, Toast.LENGTH_LONG).show();
//            Log.d("ManageByProvider", uri.toString());
//            notifyDataSetChanged();
//
//        }
//        if (req == 1 && result == RESULT_OK && data != null && data.getData() != null) {
//            uri = data.getData();
//            filePath = data.getData();
//            Toast.makeText(mCtx, uri.toString(), Toast.LENGTH_SHORT).show();
//
//        }
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M || Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
//            final Uri uri = data.getData();
//            file = new File(uri.getPath());
//            stringPath = file.getAbsolutePath();
//
//            notifyDataSetChanged();
//
//            Toast.makeText(mCtx, "path " + file, Toast.LENGTH_SHORT).show();
//
//            // now you can upload your image file
//        }else{
//            // in android version lower than M your method must work
//            Toast.makeText(mCtx, "path veres " , Toast.LENGTH_SHORT).show();
//
//        }
  //  }

    public interface CallbackInterface {
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_fault_id, priority, expected_close, days_overdue, no_photo, request_date, request_type, room, description, textView, date_label, date_assigned, provider, status, file_name, desc_label;
        public ImageView imageView;
        public LinearLayout linearLayout;
        public Button bn_close, view_photo;
        public ImageButton choose_file;

        public MyViewHolder(View view) {
            super(view);
            request_date = (TextView) view.findViewById(R.id.tv_date);
            request_type = (TextView) view.findViewById(R.id.tv_type);
            description = (TextView) view.findViewById(R.id.tv_desc);
            desc_label = (TextView) view.findViewById(R.id.desc_label);
            expected_close = (TextView) view.findViewById(R.id.tv_expected_close);
            days_overdue = (TextView) view.findViewById(R.id.tv_overdue);

            textView = (TextView) view.findViewById(R.id.room_label);
            date_label = (TextView) view.findViewById(R.id.date_label);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            status = (TextView) view.findViewById(R.id.tv_status);
            priority = (TextView) view.findViewById(R.id.tv_priority);
            date_assigned = (TextView) view.findViewById(R.id.tv_date_assigned);
            file_name = (TextView) view.findViewById(R.id.tv_file_name);
            choose_file = (ImageButton) view.findViewById(R.id.ib_report);
            bn_close = (Button) view.findViewById(R.id.btn_close);
            no_photo = (TextView) view.findViewById(R.id.tv_no_photo);
            view_photo = (Button) view.findViewById(R.id.btn_view_photo);
            tv_fault_id = (TextView) view.findViewById(R.id.tv_fault_id);


        }

    }
    @Override
    public ByProviderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_by_provider, parent, false);

        return new ByProviderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Request request = requestList.get(position);
        holder.request_date.setText(request.getRequestDate());
        holder.request_type.setText(request.getRequestType());
        holder.description.setText(request.getDescription());
        holder.textView.setText(request.getRoom());
        holder.date_label.setText(request.getRequestDate());
        holder.desc_label.setText(request.getDescription());
        holder.status.setText(request.getRequestStatus());
        holder.expected_close.setText(request.getExpectedClose());
        holder.days_overdue.setText(request.getDaysOverdue());

        fault_type_id = request.getFaultTypeID();
        fault_id = request.getFaultID();
        holder.tv_fault_id.setText(request.getFaultID());
        holder.date_assigned.setText(request.getDateAssigned());
        holder.priority.setText(request.getPriority());
        if( request.getImageUrl() != "null") {
            Glide.with(mCtx).load(request.getImageUrl()).into(holder.imageView);
        }
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

        if(filePath != null)
        {
            holder.file_name.setText("fileNme");
        }

        //  holder.choose_file.setOnClickListener(mCtx);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloading the list
                notifyDataSetChanged();
            }
        });
        holder.choose_file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ManageByProvider origin = (ManageByProvider) mCtx;
              ((ManageByProvider) mCtx).startActivityForResult(Intent.createChooser(intent, "Select PDF"), 1);

            }
        });


        holder.bn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("custom-message");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("id",holder.tv_fault_id.getText().toString());
                LocalBroadcastManager.getInstance(mCtx).sendBroadcast(intent);
//                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, closeUrl,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                try {
//                                    JSONArray jsonArray = new JSONArray(response);
//                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                                    String code = jsonObject.getString("code");
//                                    String message = jsonObject.getString("message");
//
//                                    final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
//                                    builder.setTitle("WeFixx Response");
//                                    builder.setMessage(message);
//                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            notifyDataSetChanged();
//                                        }
//                                    });
//                                    builder.show();
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        //String path = FilePath.getPath(this, filePath);
//
//
//                        params.put("fault_id", fault_id);
//                        return params;
//                    }
//                };
//                MySingleton.getInstance(mCtx).addToRequestque(stringRequest);
              //  Toast.makeText(mCtx, "Uriiii " + uri.toString(), Toast.LENGTH_SHORT).show();
                //holder.file_name.setText(filePath.toString());
               // String patyh = uri.toString();
                //final File file = new File(uri.getPath());

               // String path = FilePath.getPath(mCtx, filePath);
               // Toast.makeText(mCtx, "Paaaaath " + path, Toast.LENGTH_SHORT).show();
               // String pathe = FilePath.getPath(mCtx, filePath);

//                if (path == null) {
//
//                    Toast.makeText(mCtx, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
//                } else {
//                    //Uploading code
//                    try {
//                        String uploadId = UUID.randomUUID().toString();
//
//                        //Creating a multi part request
//                        new MultipartUploadRequest(mCtx, uploadId, UPLOAD_URL)
//                                .addFileToUpload(file.toString(), "report") //Adding file
//                                .addParameter("name", "report") //Adding text parameter to the request
//                                .setNotificationConfig(new UploadNotificationConfig())
//                                .setMaxRetries(2)
//                                .startUpload(); //Starting the upload
//                        Toast.makeText(mCtx, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
//
//                    } catch (Exception exc) {
//                        Toast.makeText(mCtx, exc.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }

            }

            public void DisplayAlert() {
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent refresh = new Intent(mCtx, Manage.class);
                        mCtx.startActivity(refresh);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
