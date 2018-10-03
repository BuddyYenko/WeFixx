package com.example.s215087038.wefixx.rsa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.s215087038.wefixx.History;
import com.example.s215087038.wefixx.R;

public class ManageBy extends AppCompatActivity {
    ImageButton by_provider, by_priority, by_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_by);

        by_provider = (ImageButton) findViewById(R.id.btn_by_provider);
        by_priority = (ImageButton) findViewById(R.id.btn_by_priority);
        by_status = (ImageButton) findViewById(R.id.btn_by_status);

        by_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(ManageBy.this, ManageByProvider.class);
                startActivity(activity);
            }
        });
        by_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(ManageBy.this, Manage.class);
                startActivity(activity);
            }
        });
        by_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(ManageBy.this, ManageByPriority.class);
                startActivity(activity);
            }
        });
    }
}
