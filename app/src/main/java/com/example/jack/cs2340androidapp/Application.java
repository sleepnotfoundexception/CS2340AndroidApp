package com.example.jack.cs2340androidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application extends AppCompatActivity {

    private static String[] filter = {"", "", ""};

    public static String[] getFilter() {
        return filter;
    }

    public static void setFilter(String[] filter) {
        Application.filter = filter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        List<Shelter> shelters = ShelterModel.getShelters();
        List<Shelter> filteredShelters = new ArrayList<Shelter>();
        for (Shelter s: shelters) {
            boolean filterMatch = true;
            if (!filter[0].equals("")) {
                String[] namefilters = filter[0].toLowerCase().trim().split(" ");
                for (String name : namefilters) {
                    if (!s.getName().toLowerCase().contains(name)) {
                        filterMatch = false;
                    }
                }
            }
            if (!filter[1].equals("")) {
                if (filter[1].equals("Female")) {
                    //I know my regex sucks and I could consolidate this sorry
                    //it does work
                    Pattern p = Pattern.compile("^men");
                    Pattern p2 = Pattern.compile(" men");
                    Pattern p3 = Pattern.compile("^male");
                    Pattern p4 = Pattern.compile(" male");
                    Pattern p5 = Pattern.compile("//men");
                    Pattern p6 = Pattern.compile("//male");
                    Matcher m = p.matcher(s.getRestrictions().toLowerCase());
                    Matcher m2 = p2.matcher(s.getRestrictions().toLowerCase());
                    Matcher m3 = p3.matcher(s.getRestrictions().toLowerCase());
                    Matcher m4 = p4.matcher(s.getRestrictions().toLowerCase());
                    Matcher m5 = p5.matcher(s.getRestrictions().toLowerCase());
                    Matcher m6 = p6.matcher(s.getRestrictions().toLowerCase());
                    if (m.find() || m2.find() || m3.find() || m4.find() || m5.find() || m6.find()) {
                        filterMatch = false;
                    }
                }
                if (filter[1].equals("Male")) {
                    if (s.getRestrictions().toLowerCase().contains("women") || s.getRestrictions().toLowerCase().contains("female")) {
                        filterMatch = false;
                    }
                }
            }
            if (!filter[2].equals("") && !filter[2].equals("Families with Newborns")) {
                if (!s.getRestrictions().toLowerCase().contains(filter[2].toLowerCase())) {
                    filterMatch = false;
                }
            } else if (filter[2].equals("Families with Newborns")) {
                if (!s.getRestrictions().toLowerCase().contains(filter[2].toLowerCase())
                        && !(s.getRestrictions().toLowerCase().contains("families") && s.getRestrictions().toLowerCase().contains("newborns"))) {
                    filterMatch = false;
                }
            }
            if (filterMatch) {
                filteredShelters.add(s);
            }
        }
        ArrayAdapter<Shelter> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredShelters);
        ListView shelterList = (ListView) findViewById(R.id.ShelterList);
        shelterList.setAdapter(itemsAdapter);
        shelterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item = ((TextView)view).getText().toString();
                for (Shelter s: ShelterModel.getShelters()) {
                    if (s.getName().equals(item)) {
                        showAlertDialog(s);
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

    public void showAlertDialog(Shelter s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(Application.this).create();
        alertDialog.setTitle(s.getName());
        String message = "";
        message += s.getPhonenumber() + "\n";
        message += "Capacity: " + s.getCapacity() + "\n";
        message += "Restrictions: " + s.getRestrictions() + "\n\n";
        message += "Latitude: " + s.getLatitude() + "\n";
        message += "Longitude: " + s.getLongitude() + "\n\n";
        message += s.getSpecialnotes();
        message += "\n\nVacancies: " + s.getVacancies();
        final Shelter sInnerClassWrapper = s;
        if (MainScreen.userData.getReservation() == null) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Claim Beds", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    claimBeds(sInnerClassWrapper);
                }
            });
        } else if (!(MainScreen.userData.getReservation().first == s.getUniqueKey())) {
            //reservation does not exist at this shelter
            String reservedShelterName = "";
            Shelter reservedShelter = null;
            for (Shelter s2: ShelterModel.getShelters()) {
                if (s2.getUniqueKey() == MainScreen.userData.getReservation().first) {
                    reservedShelterName = s2.getName();
                    reservedShelter = s2;
                }
            }
            final Shelter reservedShelterInner = reservedShelter;
            message += "\n\nReserved beds: " + MainScreen.userData.getReservation().second + " at " + reservedShelterName;
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Release Beds", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reservedShelterInner.setVacancies(reservedShelterInner.getVacancies() + MainScreen.userData.getReservation().second);
                    reservedShelterInner.save();
                    Snackbar claimed = Snackbar.make(findViewById(R.id.coordinatorLayout), "Released " +MainScreen.userData.getReservation().second + " bed(s).", Snackbar.LENGTH_LONG);
                    claimed.show();
                    MainScreen.userData.setReservation(null);
                    MainScreen.userData.save();
                    alertDialog.cancel();
                }
            });
        } else if (MainScreen.userData.getReservation().first == s.getUniqueKey()) {
            //Reservation exists at this shelter
            String reservedShelterName = "";
            Shelter reservedShelter = null;
            for (Shelter s2: ShelterModel.getShelters()) {
                if (s2.getUniqueKey() == MainScreen.userData.getReservation().first) {
                    reservedShelterName = s2.getName();
                    reservedShelter = s2;
                }
            }
            final Shelter reservedShelterInner = reservedShelter;
            message += "\n\nReserved beds: " + MainScreen.userData.getReservation().second;
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Release Beds", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sInnerClassWrapper.setVacancies(sInnerClassWrapper.getVacancies() + MainScreen.userData.getReservation().second);
                    sInnerClassWrapper.save();
                    Snackbar claimed = Snackbar.make(findViewById(R.id.coordinatorLayout), "Released " +MainScreen.userData.getReservation().second + " bed(s).", Snackbar.LENGTH_LONG);
                    claimed.show();
                    MainScreen.userData.setReservation(null);
                    MainScreen.userData.save();
                    alertDialog.cancel();
                }
            });
        }
        alertDialog.setMessage(message);
        alertDialog.show();
    }

    public void claimBeds(Shelter s) {
        //Create another of the original dialog to return to after finishing
        final AlertDialog originalDialog = new AlertDialog.Builder(Application.this).create();
        originalDialog.setTitle(s.getName());
        String message = "";
        message += s.getPhonenumber() + "\n";
        message += "Capacity: " + s.getCapacity() + "\n";
        message += "Restrictions: " + s.getRestrictions() + "\n\n";
        message += "Latitude: " + s.getLatitude() + "\n";
        message += "Longitude: " + s.getLongitude() + "\n\n";
        message += s.getSpecialnotes();
        message += "\n\nVacancies: " + s.getVacancies();
        originalDialog.setMessage(message);
        final Shelter sInnerClassWrapper = s;
        originalDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Claim Beds", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                claimBeds(sInnerClassWrapper);
            }
        });

        //Build the claim beds dialog
        final AlertDialog alertDialog = new AlertDialog.Builder(Application.this).create();
        alertDialog.setTitle("Claim Beds");
        final NumberPicker numberPicker = new NumberPicker(Application.this);
        numberPicker.setMaxValue(Integer.parseInt(s.getCapacity()));
        numberPicker.setMinValue(0);
        alertDialog.setView(numberPicker);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Claim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (numberPicker.getValue() != 0) {
                    MainScreen.userData.setReservation(new Pair<Integer, Integer>(sInnerClassWrapper.getUniqueKey(), numberPicker.getValue()));
                    MainScreen.userData.save();
                    sInnerClassWrapper.setVacancies(sInnerClassWrapper.getVacancies() - numberPicker.getValue());
                    sInnerClassWrapper.save();
                }
                alertDialog.cancel();
                Snackbar claimed = Snackbar.make(findViewById(R.id.coordinatorLayout), "Claimed " + numberPicker.getValue() + " bed(s).", Snackbar.LENGTH_LONG);
                claimed.show();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                originalDialog.show();
            }
        });
        alertDialog.show();
    }

    public void filter(View view) {
        Intent moveToFilter = new Intent(Application.this, Filter.class);
        startActivity(moveToFilter);
    }

    public void logout(View view) {
        AuthUI.getInstance().signOut(this);
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Application.this, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
