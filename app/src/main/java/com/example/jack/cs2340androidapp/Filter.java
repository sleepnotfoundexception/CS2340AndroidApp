package com.example.jack.cs2340androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Filter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        String[] genders = new String[] {"All", "Male", "Female"};
        ArrayAdapter<String> genderadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderadapter.addAll(genders);
        Spinner genderspin = (Spinner)findViewById(R.id.genders);
        genderspin.setAdapter(genderadapter);
        String[] ages = new String[] {"All", "Families with Newborns", "Children", "Young Adults", "Anyone"};
        ArrayAdapter<String> ageadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        ageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageadapter.addAll(ages);
        Spinner agespin = (Spinner)findViewById(R.id.ages);
        agespin.setAdapter(ageadapter);

        String[] currentFilterList = Application.getFilter();
        EditText name = (EditText)findViewById(R.id.editText);
        name.setText(currentFilterList[0]);
        if (currentFilterList[1].equals("Male")) {
            genderspin.setSelection(1);
        } else if (currentFilterList[1].equals("Female")) {
            genderspin.setSelection(2);
        }
        if (currentFilterList[2].equals("Families with Newborns")) {
            agespin.setSelection(1);
        } else if (currentFilterList[2].equals("Children")) {
            agespin.setSelection(2);
        } else if (currentFilterList[2].equals("Young Adults")) {
            agespin.setSelection(3);
        } else if (currentFilterList[2].equals("Anyone")) {
            agespin.setSelection(4);
        }
    }
    public void back (View view) {
        Application.setFilter(new String[]{"", "", ""});
        Intent intent = new Intent(Filter.this, Application.class);
        startActivity(intent);
    }

    public void filter (View view) {
        Spinner genderspin = (Spinner)findViewById(R.id.genders);
        Spinner agespin = (Spinner)findViewById(R.id.ages);
        EditText name = (EditText)findViewById(R.id.editText);
        String[] filter = {
                name.getText().toString(),
                genderspin.getSelectedItem().toString(),
                agespin.getSelectedItem().toString()};
        if (genderspin.getSelectedItem().toString().equals("All")) {
            filter[1] = "";
        }
        if (agespin.getSelectedItem().toString().equals("All")) {
            filter[2] = "";
        }
        Application.setFilter(filter);
        Intent intent = new Intent(Filter.this, Application.class);
        startActivity(intent);
    }
}
