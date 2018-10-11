package com.example.s215087038.wefixx.rsa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.s215087038.wefixx.History;
import com.example.s215087038.wefixx.R;


public class RSA extends AppCompatActivity {
    ImageButton manage_req, history;
    private static final int STORAGE_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rsa);
        requestStoragePermission();

        manage_req = (ImageButton) findViewById(R.id.manage_request);
        history = (ImageButton) findViewById(R.id.history);

        manage_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manage_req = new Intent(RSA.this, ManageBy.class);
                startActivity(manage_req);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(RSA.this, History.class);
                startActivity(history);
            }
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(RSA.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(RSA.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(RSA.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(RSA.this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(RSA.this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
