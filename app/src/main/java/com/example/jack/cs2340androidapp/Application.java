package com.example.jack.cs2340androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Application extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        ArrayAdapter<Shelter> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ShelterModel.getShelters());
        ListView shelterList = (ListView) findViewById(R.id.ShelterList);
        shelterList.setAdapter(itemsAdapter);
        if (login.getLoggedUser().isAdministrator()) {
            TextView administrator = findViewById(R.id.adminConfirmation);
            administrator.setVisibility(View.VISIBLE);
        } else {
            TextView administrator = findViewById(R.id.adminConfirmation);
            administrator.setVisibility(View.INVISIBLE);
        }
    }

    public void logout(View view) {
        login.clearLoggedUser();
        Intent intent = new Intent(Application.this, login.class);
        startActivity(intent);
    }

}
