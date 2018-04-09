package com.example.jack.cs2340androidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
Handles registration.
 */
public class Registration extends AppCompatActivity {

    private final String adminPass = "a";
    private String adminPassGet = "";
    private boolean adminPassRequirementMet = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        EditText phoneField = findViewById(R.id.phoneField);
        if (FirebaseHandler.activeUser.getPhoneNumber() != null) {
                String phone = FirebaseHandler.activeUser.getPhoneNumber();
                assert phone != null;
                phoneField.setText(phone.substring(2));
        }
        EditText nameField = findViewById(R.id.nameField);
        if (FirebaseHandler.activeUser.getDisplayName() != null) {
            nameField.setText(FirebaseHandler.activeUser.getDisplayName());
        }
        nameField.requestFocus();
    }
/**
Builds a new user based on the fields entered. Throws an AlertDialog if errors exist.
@param view Required in order to fetch components and their contents.
 */
    public void createUser (View view) {
        EditText phoneField = findViewById(R.id.phoneField);
        Editable phoneText = phoneField.getText();
        String phone = phoneText.toString();
        if ((((phone.length() >= 9) && (phone.length() <= 10)) || "".equals(phone))) {
            CheckBox administrator = findViewById(R.id.Administrator);
            if (administrator.isChecked()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Password Required");
                builder.setMessage("In order to create an administrator, " +
                        "please enter administrator password.");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                final Editable inputText = input.getText();
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adminPassGet = inputText.toString();
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
        } else if (!"".equals(phone)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
            AlertDialog alertDialog = builder.create();
            alertDialog.setTitle("Bad Phone Number");
            alertDialog.setMessage("Phone number does not match correct format.");
            alertDialog.show();
        }
    }

    //The second bit of the function above. I don't actually remember why I split it up.
    //I think there might be a good reason? Who knows.
    private void createUser(boolean continueLogin) {
        EditText city = findViewById(R.id.cityField);
        EditText phone = findViewById(R.id.phoneField);
        EditText nameField = findViewById(R.id.nameField);
        CheckBox administrator = findViewById(R.id.Administrator);
        Editable nameText = nameField.getText();
        Editable cityText = city.getText();
        Editable phoneText = phone.getText();
        if (continueLogin) {
            User user = new User(
                    nameText.toString(),
                    cityText.toString(),
                    //FirebaseHandler.activeUser.getEmail(),
                    phoneText.toString(),
                    administrator.isChecked());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("users");
            String uID = FirebaseHandler.activeUser.getUid();
            DatabaseReference userSnap = ref.child(uID);
            DatabaseReference nameRef = userSnap.child("name");
            DatabaseReference cityRef = userSnap.child("city");
            DatabaseReference phoneRef = userSnap.child("phone");
            DatabaseReference adminRef = userSnap.child("admin");
            nameRef.setValue(user.getName());
            cityRef.setValue(user.getCity());
            phoneRef.setValue(user.getPhoneNumber());
            if (user.isAdministrator()) {
                adminRef.setValue("true");
            } else {
                adminRef.setValue("false");
            }
            Intent intent = new Intent(Registration.this, Application.class);
            startActivity(intent);

        }
        if (!continueLogin){
            showAdminError();
        }
    }

    private void showAdminError() {
        AlertDialog.Builder builder =  new AlertDialog.Builder(Registration.this);
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Bad Admin Login");
        alertDialog.setMessage("The administrator password does not match.");
        alertDialog.show();
    }

    /**
    Segues to the back intent and clears history.
    @param view Required by the buttonPress system. Not used.
     */
    public void back (View view) {
        Intent intent = new Intent(Registration.this, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}