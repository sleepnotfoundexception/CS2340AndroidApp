package com.example.jack.cs2340androidapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class login extends AppCompatActivity {

    private static User loggedUser;

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void clearLoggedUser() {
        loggedUser = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText username = findViewById(R.id.usernameInput);
        username.requestFocus();
    }

    public void login(View view) {
        EditText username = findViewById(R.id.usernameInput);
        EditText password = findViewById(R.id.passwordInput);
        boolean matches = false;
        for (User user: User.UserList) {
            if (user.getEmail().equals(username.getText().toString()) && user.getPassword().equals(password.getText().toString())) {
                matches = true;
                loggedUser = user;
            }
        }
        if (matches) {
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
