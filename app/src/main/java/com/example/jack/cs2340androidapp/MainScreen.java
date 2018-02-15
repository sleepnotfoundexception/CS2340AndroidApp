package com.example.jack.cs2340androidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    public void openLogin(View view) {
        Intent moveToLogin = new Intent(MainScreen.this, login.class);
        startActivity(moveToLogin);
    }
}
