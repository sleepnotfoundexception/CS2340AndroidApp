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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    private String adminPass = "a";
    private String adminPassGet = "";
    boolean adminPassRequirementMet = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        EditText cityfield = findViewById(R.id.cityField);
        EditText phonefield = findViewById(R.id.phoneField);
        if (MainScreen.activeUser.getPhoneNumber() != null) {
            phonefield.setText(MainScreen.activeUser.getPhoneNumber().substring(2));
        }
        EditText namefield = findViewById(R.id.nameField);
        if (MainScreen.activeUser.getDisplayName() != null) {
            namefield.setText(MainScreen.activeUser.getDisplayName());
        }
        namefield.requestFocus();
    }

    public void createUser (View view) {
        EditText cityfield = findViewById(R.id.cityField);
        EditText phonefield = findViewById(R.id.phoneField);
        String city = cityfield.getText().toString();
        String phone = phonefield.getText().toString();
        if (((phone.length() >= 9 && phone.length() <= 10) || phone.equals(""))) {
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
        } else if ((phone.length() > 10 || phone.length() < 9) && !phone.equals("")) {
            AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            alertDialog.setTitle("Bad Phone Number");
            alertDialog.setMessage("Phone number does not match correct format.");
            alertDialog.show();
        }
    }

    public void createUser(boolean continueLogin) {
        EditText city = findViewById(R.id.cityField);
        EditText phone = findViewById(R.id.phoneField);
        EditText namefield = findViewById(R.id.nameField);
        CheckBox administrator = findViewById(R.id.Administrator);
        if (continueLogin) {
            User user = new User(
                    namefield.getText().toString(),
                    city.getText().toString(),
                    MainScreen.activeUser.getEmail(),
                    phone.getText().toString(),
                    administrator.isChecked());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("users");
            ref.child(MainScreen.activeUser.getUid()).child("name").setValue(user.getName());
            ref.child(MainScreen.activeUser.getUid()).child("city").setValue(user.getCity());
            ref.child(MainScreen.activeUser.getUid()).child("phone").setValue(user.getPhoneNumber());
            if (user.isAdministrator()) {
                ref.child(MainScreen.activeUser.getUid()).child("admin").setValue("true");
            } else {
                ref.child(MainScreen.activeUser.getUid()).child("admin").setValue("false");
            }
            Intent intent = new Intent(Registration.this, Application.class);
            startActivity(intent);

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
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}