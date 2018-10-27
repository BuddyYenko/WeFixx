package com.example.s215087038.wefixx.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.s215087038.wefixx.History;
import com.example.s215087038.wefixx.LoginActivity;
import com.example.s215087038.wefixx.R;
import com.example.s215087038.wefixx.rsa.Manage;
import com.example.s215087038.wefixx.rsa.RSA;

public class Manager extends AppCompatActivity {
    ImageButton history, delayed, add_rsa;

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
            Intent logout = new Intent(Manager.this, LoginActivity.class);
            startActivity(logout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager);
        history = findViewById(R.id.btn_history);
        delayed = findViewById(R.id.btn_delayed);
        add_rsa = findViewById(R.id.btn_add_rsa);

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

        add_rsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent delayed = new Intent(Manager.this, RegisterActivity.class);
                startActivity(delayed);
            }
        });
    }
}
