package com.example.s215087038.wefixx.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.s215087038.wefixx.History;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.rsa.RSA;

public class Manager extends AppCompatActivity {
    ImageButton history, delayed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager);
        history = findViewById(R.id.btn_history);
        delayed = findViewById(R.id.btn_delayed);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(Manager.this, ProviderHistory.class);
                startActivity(history);
            }
        });
        delayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent delayed = new Intent(Manager.this, ProviderDelayed.class);
                startActivity(delayed);
            }
        });

    }
}
