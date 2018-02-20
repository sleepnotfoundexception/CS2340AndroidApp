package com.example.jack.cs2340androidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class Registration extends AppCompatActivity {

    private String adminPass = "a";
    private String adminPassGet = "";
    boolean adminPassRequirementMet = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        EditText namefield = findViewById(R.id.nameField);
        namefield.requestFocus();
    }

    public void createUser (View view) {
        EditText namefield = findViewById(R.id.nameField);
        EditText emailfield = findViewById(R.id.emailField);
        EditText cityfield = findViewById(R.id.cityField);
        EditText phonefield = findViewById(R.id.phoneField);
        String name = namefield.getText().toString();
        String email = emailfield.getText().toString();
        String city = cityfield.getText().toString();
        String phone = phonefield.getText().toString();
        boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean takenEmail = false;
        for (User user: User.UserList) {
            if (user.getEmail().equals(email)) {
                takenEmail = true;
            }
        }
        if (((phone.length() >= 9 && phone.length() <= 10) || phone.equals("")) && validEmail && !takenEmail) {
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
                            adminPassRequirementMet = false;
                        } else {
                            adminPassRequirementMet = true;
                            adminPassGet = "";
                        }
                        createUser(adminPassRequirementMet);
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
        } else if (!validEmail) {
            AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            alertDialog.setTitle("Bad Email");
            alertDialog.setMessage("Email address format is invalid.");
            alertDialog.show();
        } else if (takenEmail) {
            AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            alertDialog.setTitle("Account Taken");
            alertDialog.setMessage("An account already exists with that email.");
            alertDialog.show();
        } else if ((phone.length() > 10 || phone.length() < 9) && !phone.equals("")) {
            AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            alertDialog.setTitle("Bad Phone Number");
            alertDialog.setMessage("Phone number does not match correct format.");
            alertDialog.show();
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
            alertDialog.setTitle("Bad Passwords");
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