package com.example.s215087038.wefixx.student;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.s215087038.wefixx.FAQ;
import com.example.s215087038.wefixx.History;
import com.example.s215087038.wefixx.LoginActivity;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.NewRequest;
import com.example.s215087038.wefixx.rsa.RSA;

public class Student extends AppCompatActivity {
    ImageButton new_request, help, notifications, request_history;
    private static final int STORAGE_PERMISSION_CODE = 123;
    @Override
    public void onBackPressed() {
        finish();
    }
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
            Intent logout = new Intent(Student.this, LoginActivity.class);
            startActivity(logout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);
        requestStoragePermission();

        new_request = (ImageButton) findViewById(R.id.btn_new_request);
        help = (ImageButton) findViewById(R.id.btn_help);
        request_history = (ImageButton) findViewById(R.id.btn_history);

        new_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_req = new Intent(Student.this, NewRequest.class);
                startActivity(new_req);
            }
        });
        request_history = (ImageButton) findViewById(R.id.btn_history);
        request_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(Student.this, StudentHistory.class);
                startActivity(history);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent faq = new Intent(Student.this, FAQ.class);
                startActivity(faq);
            }
        });
//
//        notifications.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent tutorial = new Intent(Login.this, TutorialActivity.class);
//                startActivity(tutorial);
//            }
//        });
//        request_history.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent tutorial = new Intent(Login.this, TutorialActivity.class);
//                startActivity(tutorial);
//            }
//        });
    }
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(Student.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(Student.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(Student.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
               // Toast.makeText(Student.this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                //Toast.makeText(Student.this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
