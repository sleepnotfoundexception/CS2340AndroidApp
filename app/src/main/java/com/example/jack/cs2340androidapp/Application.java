package com.example.jack.cs2340androidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class Application extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
    }

    public void logout(View view) {
        Intent intent = new Intent(Application.this, login.class);
        startActivity(intent);
    }
}