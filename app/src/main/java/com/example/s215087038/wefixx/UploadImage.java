package com.example.s215087038.wefixx;
import android.Manifest;
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
import android.graphics.drawable.BitmapDrawable;
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
import android.text.style.UpdateLayout;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.s215087038.wefixx.EndPoints;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.VolleyMultipartRequest;
import com.example.s215087038.wefixx.student.DataObject;
import com.example.s215087038.wefixx.student.NewRequest;
import com.example.s215087038.wefixx.student.Student;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.net.Proxy.Type.HTTP;


public class UploadImage extends AppCompatActivity {

    //ImageView to display image selected
    ImageView imageView, resizedView;
    Matrix matrix;
    int newWidth, newHeight, width, height;
    float scaleWidth, scaleHeight;
    Bitmap scaledBitmap = null, bitmap = null;
    ByteArrayOutputStream outputStream;
    ByteArrayOutputStream bytearrayoutputstream;
    byte[] BYTE;
    String uploadUrl = "http://sict-iis.nmmu.ac.za/wefixx/student/new_request.php";
    String urlProblems = "http://sict-iis.nmmu.ac.za/wefixx/fault_types.php";
    AlertDialog.Builder builder;
    String description, photo, fault;
    private Button btn_request;
    private EditText Description;
    TextView text;

    private Spinner spinner;
    protected List<DataObject> spinnerData;
    private RequestQueue queue;

    Map<String,String> myMap = new HashMap<String,String>();
    ArrayList< HashMap <String, String> > listFaults = new ArrayList<>();
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> listSelectedFault = new ArrayList<String>();
    //edittext for getting the tags input
    EditText editTextTags;
    String user_id, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_request);

        //********************************************
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);

        user_id = preferences.getString("user_id", "");
        name = preferences.getString("name", "");
        //********************************************

        queue = Volley.newRequestQueue(this);
        requestJsonObject();

        //initializing views
        imageView = (ImageView) findViewById(R.id.ib_photo);

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

        Description = (EditText) findViewById(R.id.et_description);
        btn_request = (Button) findViewById(R.id.btn_request);

        builder = new AlertDialog.Builder(UploadImage.this);

        //adding click listener to button
        findViewById(R.id.ib_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        findViewById(R.id.btn_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description = Description.getText().toString();
                text.setText(description);

                if (description.equals("")) {
                    builder.setTitle("Something Went Wrong...");
                    builder.setMessage("Please fill in description");
                    displayAlert("input_error");
                } else {
                    //our custom volley request
                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadUrl,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {

                                    try {


                                        JSONArray jsonArray = new JSONArray(new String (response.data));
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");


                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }) {

                        /*
                         * If you want to add more parameters with the image
                         * you can do it here
                         * here we have only one parameter with the image
                         * which is tags
                         * */

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("user_id", user_id);
                            params.put("fault_type", fault);
                            params.put("description", description);
                            return params;
                        }
                        /*
                         * Here we are passing image by renaming it with a unique name
                         * */
//                        @Override
//                        protected Map<String, DataPart> getByteData() {
//                            Map<String, DataPart> params = new HashMap<>();
//                            String imagename = "me";
//                            params.put("pic", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
//                            return params;
//                        }
                    };

                    //adding the request to volley
                    Volley.newRequestQueue(UploadImage.this).add(volleyMultipartRequest);

                }
            }

            private void displayAlert(final String code) {
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (code.equals("input_error")) {

                            } else if (code.equals("login_failed")) {
                                //Password.setText("");
                            } else if (code.equals("login_success")) {
                                Description.setText("");
                                Intent scannerPage = new Intent(UploadImage.this, Student.class);
                                startActivity(scannerPage);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlProblems, new Response.Listener<String>() {
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
                                public void onItemSelected(

                                        AdapterView<?> parent, View view, int position, long problemID) {
                                    DataObject selected = (DataObject) parent.getItemAtPosition(position);
                                    fault = selected.getID();
                                }
                                public void onNothingSelected(AdapterView<?> parent) {
                                    //selectedOption.setText("Spinner1: unselected");
                                }
                            });

                    assert spinner != null;
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(UploadImage.this, spinnerData);
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

}