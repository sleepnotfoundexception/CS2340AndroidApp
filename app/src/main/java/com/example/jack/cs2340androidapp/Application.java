package com.example.jack.cs2340androidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
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

@SuppressWarnings("ChainedMethodCall")
public class Application extends FragmentActivity implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback
{

    private static String[] filter = {"", "", ""};

    public static String[] getFilter() {
        return filter.clone();
    }

    public static void setFilter(String[] filter) {
        Application.filter = filter.clone();
    }

    private GoogleMap mMap;
    private static boolean firebaseChange = false;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<Shelter> shelters = getFilteredShelters();
        double minLatitude = Integer.MAX_VALUE;
        double maxLatitude = Integer.MIN_VALUE;
        double minLongitude = Integer.MAX_VALUE;
        double maxLongitude = Integer.MIN_VALUE;
        for (Shelter s: shelters) {
            maxLongitude = (s.getLongitude() > maxLongitude) ? s.getLongitude() : maxLongitude;
            minLongitude = (s.getLongitude() < minLongitude) ? s.getLongitude() : minLongitude;
            maxLatitude = (s.getLatitude() > maxLatitude) ? s.getLatitude() : maxLatitude;
            minLatitude = (s.getLatitude() < minLatitude) ? s.getLatitude() : minLatitude;
            Marker mark = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(s.getLatitude(), s.getLongitude()))
                    .title(s.getName()));
            mark.setTag(s);
        }
        final double minLng = minLongitude;
        final double maxLng = maxLongitude;
        final double minLat = minLatitude;
        final double maxLat = maxLatitude;
        final int cameraMovementAmount = 50;
        if (firebaseChange) {
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    LatLngBounds BOUNDS = new LatLngBounds(
                            new LatLng(minLat, minLng), new LatLng(maxLat, maxLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(BOUNDS,
                            cameraMovementAmount));
                    firebaseChange = false;
                }
            });
        } else {
            LatLngBounds BOUNDS = new LatLngBounds(
                    new LatLng(minLat, minLng), new LatLng(maxLat, maxLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(BOUNDS, cameraMovementAmount));
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Shelter s = (Shelter) marker.getTag();
        assert s != null;
        showAlertDialog(s);

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private List<Shelter> getFilteredShelters () {
        List<Shelter> shelters = ShelterModel.getShelters();
        List<Shelter> filteredShelters = new ArrayList<>();
        for (Shelter s: shelters) {
            if (runFilters(s)) {
                filteredShelters.add(s);
            }
        }
        return filteredShelters;
    }

    private boolean runFilters(Shelter s) {
        if (!"".equals(filter[0])) {
            if (!runNameFilters(s)) {
                return false;
            }
        }
        return ("".equals(filter[1]) || MFFilter(s)) && ("".equals(filter[2])
                || "Families with Newborns".equals(filter[2]) ||
                s.getRestrictions().toLowerCase().contains(filter[2].toLowerCase()))
                && newbornFilters(s);
    }

    private boolean newbornFilters(Shelter s) {
        return !"Families with Newborns".equals(filter[2]) ||
                s.getRestrictions().toLowerCase().contains(filter[2].toLowerCase()) ||
                (s.getRestrictions().toLowerCase().contains("families") &&
                        s.getRestrictions().toLowerCase().contains("newborns"));
    }

    private boolean runNameFilters(Shelter s) {
        String[] namefilters = filter[0].toLowerCase().trim().split(" ");
        for (String name : namefilters) {
            if (!s.getName().toLowerCase().contains(name)) {
                return false;
            }
        }
        return true;
    }

    private boolean MFFilter(Shelter s) {
        if ("Female".equals(filter[1])) {
            Pattern p = Pattern.compile("^men| men|^male| male|//men|//male");
            Matcher m = p.matcher(s.getRestrictions().toLowerCase());
            if (m.find()) {
                return false;
            }
        }
        return !"Male".equals(filter[1]) || !s.getRestrictions().toLowerCase().contains("women")
                && !s.getRestrictions().toLowerCase().contains("female");
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

    private void showAlertDialog(Shelter s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(Application.this).create();
        alertDialog.setTitle(s.getName());
        String message = "";
        message += s.getPhoneNumber() + "\n";
        message += "Capacity: " + s.getCapacity() + "\n";
        message += "Restrictions: " + s.getRestrictions() + "\n\n";
        message += "Latitude: " + s.getLatitude() + "\n";
        message += "Longitude: " + s.getLongitude() + "\n\n";
        message += s.getSpecialNotes();
        message += "\n\nVacancies: " + s.getVacancies();
        final Shelter sInnerClassWrapper = s;
        if (MainScreen.userData.getReservation() == null) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Claim Beds",
                    new DialogInterface.OnClickListener() {

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
            message += "\n\nReserved beds: " + MainScreen.userData.getReservation().second +
                    " at " + reservedShelterName;
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Release Beds",
                    new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    firebaseChange = true;
                        assert reservedShelterInner != null;
                        reservedShelterInner.setVacancies(reservedShelterInner.getVacancies() +
                                MainScreen.userData.getReservation().second);
                        reservedShelterInner.save();
                    Snackbar claimed = Snackbar.make(findViewById(R.id.coordinatorLayout),
                            "Released " +MainScreen.userData.getReservation().second +
                                    " bed(s).", Snackbar.LENGTH_LONG);
                    claimed.show();
                    MainScreen.userData.setReservation(null);
                    MainScreen.userData.save();
                    alertDialog.cancel();
                }
            });
        } else if (MainScreen.userData.getReservation().first == s.getUniqueKey()) {
            message += "\n\nReserved beds: " + MainScreen.userData.getReservation().second;
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Release Beds",
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    firebaseChange = true;
                    sInnerClassWrapper.setVacancies(sInnerClassWrapper.getVacancies() +
                            MainScreen.userData.getReservation().second);
                    sInnerClassWrapper.save();
                    Snackbar claimed = Snackbar.make(findViewById(R.id.coordinatorLayout),
                            "Released " +MainScreen.userData.getReservation().second +
                                    " bed(s).", Snackbar.LENGTH_LONG);
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

    private void claimBeds(Shelter s) {
        //Create another of the original dialog to return to after finishing
        final AlertDialog originalDialog =
                new AlertDialog.Builder(Application.this).create();
        originalDialog.setTitle(s.getName());
        String message = "";
        message += s.getPhoneNumber() + "\n";
        message += "Capacity: " + s.getCapacity() + "\n";
        message += "Restrictions: " + s.getRestrictions() + "\n\n";
        message += "Latitude: " + s.getLatitude() + "\n";
        message += "Longitude: " + s.getLongitude() + "\n\n";
        message += s.getSpecialNotes();
        message += "\n\nVacancies: " + s.getVacancies();
        originalDialog.setMessage(message);
        final Shelter sInnerClassWrapper = s;
        originalDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Claim Beds",
                new DialogInterface.OnClickListener() {
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Claim", new
                DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (numberPicker.getValue() != 0) {
                    MainScreen.userData.setReservation(new Pair<>(sInnerClassWrapper.getUniqueKey(),
                            numberPicker.getValue()));
                    MainScreen.userData.save();
                    sInnerClassWrapper.setVacancies(sInnerClassWrapper.getVacancies() -
                            numberPicker.getValue());
                    sInnerClassWrapper.save();
                    Snackbar claimed = Snackbar.make(findViewById(R.id.coordinatorLayout),
                            "Claimed " + numberPicker.getValue() + " bed(s).",
                            Snackbar.LENGTH_LONG);
                    claimed.show();
                }
                alertDialog.cancel();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
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
