package com.example.jack.cs2340androidapp;


import android.util.Pair;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jack on 2/20/18.
 */



//WARNING WARNING WARNING WARNING
//WARNING WARNING WARNING WARNING
//WARNING WARNING WARNING WARNING
//WARNING WARNING WARNING WARNING

    //This class will always save data to the active user.


public class User {

    private String name;
    private String city;
    private String email;
    private String phoneNumber;
    private boolean administrator;
    //first int: ID, second int: beds
    private Pair<Integer,Integer> reservation;

    public User(String name, String city, String email, String phoneNumber, boolean administrator) {
        this.name = name;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.administrator = administrator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
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
        //hacky
        ref.child(MainScreen.activeUser.getUid()).child("Reservation").setValue(reservation);
    }
}
