package com.example.s215087038.wefixx.rsa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.s215087038.wefixx.NewRequest;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.student.Student;


public class RSA extends AppCompatActivity {
ImageButton manage_req;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rsa);

        manage_req = (ImageButton) findViewById(R.id.btn_history);

        manage_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manage_req = new Intent(RSA.this, ManageRequest.class);
                startActivity(manage_req);
            }
        });
    }
}
