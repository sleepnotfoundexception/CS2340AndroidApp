package com.example.jack.cs2340androidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Application extends FragmentActivity implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback
{

    private static String[] filter = {"", "", ""};

    public static String[] getFilter() {
        return filter;
    }

    public static void setFilter(String[] filter) {
        Application.filter = filter;
    }

    private GoogleMap mMap;
    private static boolean firebaseChange = false;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<Shelter> shelters = getFilteredShelters();
        double minLati = 500, maxLati = -500;
        double minLngi = 500, maxLngi = -500;
        for (Shelter s: shelters) {
            maxLngi = (s.getLongitude() > maxLngi) ? s.getLongitude() : maxLngi;
            minLngi = (s.getLongitude() < minLngi) ? s.getLongitude() : minLngi;
            maxLati = (s.getLatitude() > maxLati) ? s.getLatitude() : maxLati;
            minLati = (s.getLatitude() < minLati) ? s.getLatitude() : minLati;
            Marker mark = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(s.getLatitude(), s.getLongitude()))
                    .title(s.getName()));
            mark.setTag(s);
        }
        final double minLng = minLngi;
        final double maxLng = maxLngi;
        final double minLat = minLati;
        final double maxLat = maxLati;
        if (firebaseChange) {
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    LatLngBounds BOUNDS = new LatLngBounds(
                            new LatLng(minLat, minLng), new LatLng(maxLat, maxLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(BOUNDS, 50));
                    firebaseChange = false;
                }
            });
        } else {
            LatLngBounds BOUNDS = new LatLngBounds(
                    new LatLng(minLat, minLng), new LatLng(maxLat, maxLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(BOUNDS, 50));
        }
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
        }
        mMap.setOnMarkerClickListener(this);
    }

    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Shelter s = (Shelter) marker.getTag();
        showAlertDialog(s);

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    public List<Shelter> getFilteredShelters () {
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
        return filteredShelters;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                    firebaseChange = true;
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
                    firebaseChange = true;
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
                    firebaseChange = true;
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
        numberPicker.setMaxValue(s.getVacancies());
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
                    Snackbar claimed = Snackbar.make(findViewById(R.id.coordinatorLayout), "Claimed " + numberPicker.getValue() + " bed(s).", Snackbar.LENGTH_LONG);
                    claimed.show();
                }
                alertDialog.cancel();
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
