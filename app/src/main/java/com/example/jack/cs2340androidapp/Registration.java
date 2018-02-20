package com.example.jack.cs2340androidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class Registration extends AppCompatActivity {

    private String adminPass = "a";
    private String adminPassGet = "";
    boolean continueLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void createUser (View view) {
        CheckBox administrator = findViewById(R.id.Administrator);
        if (administrator.isChecked()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Password Required");
            builder.setMessage("In order to create an administrator, please enter administrator password.");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adminPassGet = input.getText().toString();
                    if (!adminPassGet.equals(adminPass)) {
                        continueLogin = false;
                    } else {
                        continueLogin = true;
                        adminPassGet = "";
                    }
                    createUser(continueLogin);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else {
            createUser(true);
        }
    }

    public void createUser(boolean continueLogin) {
        EditText name = findViewById(R.id.nameField);
        EditText email = findViewById(R.id.emailField);
        EditText city = findViewById(R.id.cityField);
        EditText phone = findViewById(R.id.phoneField);
        EditText password = findViewById(R.id.passwordField);
        EditText passwordReenter = findViewById(R.id.passwordReenterField);
        CheckBox administrator = findViewById(R.id.Administrator);
        if (password.getText().toString().equals(passwordReenter.getText().toString()) && continueLogin) {
            User user = new User(
                    name.getText().toString(),
                    city.getText().toString(),
                    email.getText().toString(),
                    phone.getText().toString(),
                    password.getText().toString(),
                    administrator.isChecked());
            Intent intent = new Intent(Registration.this, login.class);
            startActivity(intent);
        } else if (!password.getText().toString().equals(passwordReenter.getText().toString())) {
            AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            alertDialog.setTitle("Bad Password");
            alertDialog.setMessage("Passwords do not match.");
            alertDialog.show();
        }
        if (!continueLogin){
            AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            alertDialog.setTitle("Bad Admin Login");
            alertDialog.setMessage("The administrator password does not match.");
            alertDialog.show();
        }
    }

    public void back (View view) {
        Intent intent = new Intent(Registration.this, MainScreen.class);
        startActivity(intent);
    }

}