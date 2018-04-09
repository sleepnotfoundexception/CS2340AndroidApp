package com.example.jack.cs2340androidapp;


import android.util.Pair;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




//WARNING WARNING WARNING WARNING
//WARNING WARNING WARNING WARNING
//WARNING WARNING WARNING WARNING
//WARNING WARNING WARNING WARNING

    //This class will always save data to the active user.


/**
 * Container for user data
 */
public class User {

    private final String name;
    private final String city;
    private final String phoneNumber;
    private final boolean administrator;
    //first int: ID, second int: beds
    private Pair<Integer,Integer> reservation;

    /**
     * Creates a new user object.
     * @param name The user's name.
     * @param city City of residence.
     * @param phoneNumber Standard 9-10 char phone number.
     * @param administrator Whether or not this user is an admin.
     */
    public User(String name, String city, String phoneNumber, boolean administrator) {
        this.name = name;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.administrator = administrator;
    }

    /**
     * Gets the user's name.
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's city.
     * @return The user's city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the user's phone number.
     * @return The user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets whether or not the user is an administrator.
     * @return Whether or not the user is an administrator [boolean].
     */
    public boolean isAdministrator() {
        return administrator;
    }

    /**
     * Returns the user's current reservation as a pair between ID and seats.
     * @return The user's current reservation.
     */
    public Pair<Integer, Integer> getReservation() {
        return reservation;
    }

    /**
     * Sets the user's reservation. Used by the Application class.
     * @param reservation The user's current saved reservation.
     */
    public void setReservation(Pair<Integer, Integer> reservation) {
        this.reservation = reservation;
    }

    /**
     * Serializes the user and saves it in the database.
     */
    public void save() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");
        DatabaseReference snap = ref.child(FirebaseHandler.activeUser.getUid());
        snap.child("Reservation").setValue(reservation);
    }
}
