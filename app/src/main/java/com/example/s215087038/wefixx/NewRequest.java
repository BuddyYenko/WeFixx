package com.example.s215087038.wefixx;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.manager.Manager;
import com.example.s215087038.wefixx.model.DataObject;
import com.example.s215087038.wefixx.model.MySingleton;
import com.example.s215087038.wefixx.model.VolleyMultipartRequest;
import com.example.s215087038.wefixx.rsa.RSA;
import com.example.s215087038.wefixx.student.Student;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewRequest extends AppCompatActivity {

    //ImageView to display image selected
    ImageView imageView;
    private Spinner spinner;
    private EditText Description, RoomNo;
    String description;
    TextView example;
    private Button btn_request;
    Matrix matrix;
    Bitmap scaledBitmap = null, bitmap = null;
    //url to send student's input
    String newRequestUrl = "http://sict-iis.nmmu.ac.za/wefixx/student/new_request.php";
    //url to get fault catergories for dropdown menu
    String faultUrl = "http://sict-iis.nmmu.ac.za/wefixx/fault_types.php";
    AlertDialog.Builder builder;
    protected List<DataObject> spinnerData;
    private RequestQueue queue;
    String user_id, name, fault, room, student_no;
    private ProgressBar progressBar;
    ProgressDialog progressDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Intent logout = new Intent(NewRequest.this, LoginActivity.class);
            startActivity(logout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_request);
        //********************************************
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        user_id = preferences.getString("user_id", "");
        name = preferences.getString("name", "") + " " + preferences.getString("surname", "");
        student_no = preferences.getString("student_no", "");
        //********************************************
        requestJsonObject();

        //Getting id by their xml
        imageView = (ImageView) findViewById(R.id.ib_photo);
        Description = (EditText) findViewById(R.id.et_description);
        RoomNo = (EditText) findViewById(R.id.tv_room_no);
        btn_request = (Button) findViewById(R.id.btn_request);
        progressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        example = (TextView) findViewById(R.id.tv_example);
        example.setText("i.e 123b".toLowerCase());
        //checking the permission
        //if the permission is not given we will open setting to add permission
        //else app will not open
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }
        //adding click listener to button for photo
        findViewById(R.id.ib_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open image chooser/ photo gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });
        builder = new AlertDialog.Builder(NewRequest.this);
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);
                showProgressDialog();
                description = Description.getText().toString();
                room = RoomNo.getText().toString();
                if (description.equals("")) {
                    builder.setTitle("Empty Fields...");
                    builder.setMessage("Description and room number are required");
                    displayAlert("input_error");
                } else {
                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, newRequestUrl,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {

                                    try {
                                        JSONArray jsonArray = new JSONArray(new String (response.data));
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");
                                        builder.setTitle("WeFixx Response");
                                        builder.setMessage(message);
                                        displayAlert(code);
                                        dismissProgressBar();
                                        //progressBar.setVisibility(View.INVISIBLE);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage() +"error " , Toast.LENGTH_LONG).show();
                                   // progressBar.setVisibility(View.INVISIBLE);
                                    dismissProgressBar();
                                }
                            }) {

                        //Posting parameters to php script
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("user_id", user_id);
                            params.put("description", description);
                            params.put("fault_type", fault);
                            params.put("room_no", room);
                            params.put("name",name);
                            params.put("student_no",student_no);
                            if(bitmap !=null) {
                                params.put("photo_uploaded", "true");
                            }
                            return params;
                        }
                        //Here we are passing image by renaming it with a unique name
                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            if(bitmap !=null)
                            {
                                String imagename = "photo";
                                params.put("photo", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(scaledBitmap)));
                            }
                            return params;
                        }
                    };
                    volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    //adding the request to volley
                    Volley.newRequestQueue(NewRequest.this).add(volleyMultipartRequest);
                }

            }
            private void displayAlert(final String code) {
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (code.equals("req_failed")) {
//                                Intent req = new Intent(NewRequest.this, NewRequest.class);
//                                startActivity(req);
                        } else if (code.equals("req_success")) {
                            Description.setText("");
                            Intent main = new Intent(NewRequest.this, Student.class);
                            startActivity(main);
                        }else if(code.equals("input_error")){

                        }

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    private void requestJsonObject() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, faultUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spinnerData = Arrays.asList(mGson.fromJson(response, DataObject[].class));
                //display first question to the user
                if(null != spinnerData){
                    spinner = (Spinner) findViewById(R.id.sp_fault);
                    spinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long fault_id) {
                                    DataObject selected = (DataObject) parent.getItemAtPosition(position);
                                    //get selected fault type
                                    fault = selected.getID();
                                }
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                    assert spinner != null;
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(NewRequest.this, spinnerData);
                    spinner.setAdapter(spinnerAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                matrix = new Matrix();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // imageView.setImageBitmap(bitmap);
                compressImage(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String compressImage(Uri imageUri) {
        String filePath = getRealPathFromURI(imageUri.toString());
        BitmapFactory.Options options = new BitmapFactory.Options();
//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(scaledBitmap);

        return filename;

    }
    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }
    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Processing Assigning Request ...");
        progressDialog.show();
    }
    private void  dismissProgressBar() {
        // To Dismiss progress dialog
        progressDialog.dismiss();
    }

}