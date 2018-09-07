package com.example.s215087038.wefixx.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.s215087038.wefixx.History;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.rsa.RSA;

public class Manager extends AppCompatActivity {
    ImageButton history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager);
        history = (ImageButton) findViewById(R.id.btn_history);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(Manager.this, History.class);
                startActivity(history);
            }
        });
    }
}
