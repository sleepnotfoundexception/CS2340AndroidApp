package com.example.jack.cs2340androidapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Application extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        ArrayAdapter<Shelter> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ShelterModel.getShelters());
        ListView shelterList = (ListView) findViewById(R.id.ShelterList);
        shelterList.setAdapter(itemsAdapter);
        shelterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView)view).getText().toString();
                for (Shelter s: ShelterModel.getShelters()) {
                    if (s.getName().equals(item)) {
                        AlertDialog alertDialog = new AlertDialog.Builder(Application.this).create();
                        alertDialog.setTitle(item);
                        String message = "";
                        message += s.getPhonenumber() + "\n";
                        message += "Capacity: " + s.getCapacity() + "\n";
                        message += "Restrictions: " + s.getRestrictions() + "\n\n";
                        message += "Latitude: " + s.getLatitude() + "\n";
                        message += "Longitude: " + s.getLongitude() + "\n\n";
                        message += s.getSpecialnotes();
                        alertDialog.setMessage(message);
                        alertDialog.show();
                    }
                }

            }
        });

        if (MainScreen.userData.isAdministrator()) {
            TextView administrator = findViewById(R.id.adminConfirmation);
            administrator.setVisibility(View.VISIBLE);
        } else {
            TextView administrator = findViewById(R.id.adminConfirmation);
            administrator.setVisibility(View.INVISIBLE);
        }
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Application.this, MainScreen.class);
        startActivity(intent);
    }

}
