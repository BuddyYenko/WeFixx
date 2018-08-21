package com.example.s215087038.wefixx.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.NewRequest;

public class Student extends AppCompatActivity {
    ImageButton new_request, help, notifications, request_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);

        new_request = (ImageButton) findViewById(R.id.btn_new_request);
        help = (ImageButton) findViewById(R.id.btn_help);
        notifications = (ImageButton) findViewById(R.id.btn_notifications);
        request_history = (ImageButton) findViewById(R.id.btn_history);

        new_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_req = new Intent(Student.this, NewRequest.class);
                startActivity(new_req);
            }
        });

//        help.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent creatAcc = new Intent(Login.this, Register1.class);
//                startActivity(creatAcc);
//            }
//        });
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
}
