package com.example.jack.cs2340androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Handles filtering.
 */
public class Filter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        String[] genders = new String[] {"All", "Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderAdapter.addAll(genders);
        Spinner genderSpin = findViewById(R.id.genders);
        genderSpin.setAdapter(genderAdapter);

        String[] currentFilterList = Application.getFilter();
        EditText name = findViewById(R.id.editText);
        name.setText(currentFilterList[0]);
        if ("Male".equals(currentFilterList[1])) {
            genderSpin.setSelection(1);
        } else if ("Female".equals(currentFilterList[1])) {
            genderSpin.setSelection(2);
        }
        ageSpinSetup();
    }

    private void ageSpinSetup() {
        String[] ages = new String[] {"All", "Families with Newborns", "Children",
                "Young Adults", "Anyone"};
        ArrayAdapter<String> ageadapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        ageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageadapter.addAll(ages);
        Spinner agespin = findViewById(R.id.ages);
        agespin.setAdapter(ageadapter);
        switch (Application.getFilter()[2]) {
            case "Families with Newborns":
                agespin.setSelection(1);
                break;
            case "Children":
                agespin.setSelection(2);
                break;
            case "Young Adults":
                agespin.setSelection(3);
                break;
            case "Anyone":
                agespin.setSelection(4);
                break;
        }
    }

    /**
    Moves to the back activity.
    @param view Required by the intent API. Not used.
    */
    public void back (View view) {
        Application.setFilter(new String[]{"", "", ""});
        Intent intent = new Intent(Filter.this, Application.class);
        startActivity(intent);
    }

    /**
     Moves to the filter activity.
     @param view Required by the intent API. Not used.
     */
    public void filter (View view) {
        Spinner genderSpin = findViewById(R.id.genders);
        Spinner ageSpin = findViewById(R.id.ages);
        EditText name = findViewById(R.id.editText);
        String[] filter = {
                name.getText().toString(),
                genderSpin.getSelectedItem().toString(),
                ageSpin.getSelectedItem().toString()};
        if ("All".equals(genderSpin.getSelectedItem().toString())) {
            filter[1] = "";
        }
        if ("All".equals(ageSpin.getSelectedItem().toString())) {
            filter[2] = "";
        }
        Application.setFilter(filter);
        Intent intent = new Intent(Filter.this, Application.class);
        startActivity(intent);
    }
}
