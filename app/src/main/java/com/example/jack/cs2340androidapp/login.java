package com.example.jack.cs2340androidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.app.AlertDialog;

public class login extends AppCompatActivity {

    private String adminUser = "user";
    private String adminPass = "pass";

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
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(login.this).create();
            alertDialog.setTitle("Bad Login");
            alertDialog.setMessage("Account with given username and password not found.");
            alertDialog.show();
        }
    }

    public void back(View view) {
        Intent intent = new Intent(login.this, MainScreen.class);
        startActivity(intent);
    }
}
