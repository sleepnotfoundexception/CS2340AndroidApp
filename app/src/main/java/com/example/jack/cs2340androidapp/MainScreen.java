package com.example.jack.cs2340androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    private static SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        FirebaseApp.initializeApp(this);
        FirebaseHandler firebase = new FirebaseHandler();
        loadUserList();
    }

    public void openLogin(View view) {
        Intent moveToLogin = new Intent(MainScreen.this, login.class);
        startActivity(moveToLogin);
    }

    public void openRegistration(View view) {
        Intent moveToRegistration = new Intent(MainScreen.this, Registration.class);
        startActivity(moveToRegistration);
    }

    public void loadUserList() {
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.contains("UserList")) {
            String defaultValue = "";
            String userList = sharedPref.getString("UserList", defaultValue);
            Type type = new TypeToken<List<User>>() {}.getType();
            Gson gson = new Gson();
            User.UserList = gson.fromJson(userList, type);
        }
    }

    public static void saveUserList() {
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(User.UserList);
            editor.putString("UserList", json);
            editor.commit();
        }
    }
}
