package com.example.jack.cs2340androidapp;


import android.util.Pair;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Container for user data
 * Created by Jack on 2/20/18.
 */



//WARNING WARNING WARNING WARNING
//WARNING WARNING WARNING WARNING
//WARNING WARNING WARNING WARNING
//WARNING WARNING WARNING WARNING

    //This class will always save data to the active user.


@SuppressWarnings("ChainedMethodCall")
public class User {

    private final String name;
    private final String city;
    private final String email;
    private final String phoneNumber;
    private final boolean administrator;
    //first int: ID, second int: beds
    private Pair<Integer,Integer> reservation;

    public User(String name, String city, String email, String phoneNumber, boolean administrator) {
        this.name = name;
        this.email = email;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.administrator = administrator;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public Pair<Integer, Integer> getReservation() {
        return reservation;
    }

    public void setReservation(Pair<Integer, Integer> reservation) {
        this.reservation = reservation;
    }

    public void save() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");
        DatabaseReference snap = ref.child(MainScreen.activeUser.getUid());
        snap.child("Reservation").setValue(reservation);
    }
}
