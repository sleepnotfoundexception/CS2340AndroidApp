package com.example.jack.cs2340androidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

public class login extends AppCompatActivity {

    private String adminUser = "username";
    private String adminPass = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        EditText username = findViewById(R.id.usernameInput);
        EditText password = findViewById(R.id.passwordInput);
        System.out.println(username.getText().toString());
        if (username.getText().toString().equals(adminUser) && password.getText().toString().equals(adminPass)) {
            Intent intent = new Intent(login.this, Application.class);
            startActivity(intent);
        }
    }

    public void back(View view) {
        Intent intent = new Intent(login.this, MainScreen.class);
        startActivity(intent);
    }
}
