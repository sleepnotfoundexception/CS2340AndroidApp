package com.example.jack.cs2340androidapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("ChainedMethodCall")
class Shelter {

    private static List<Shelter> shelters = new ArrayList<>();
    private String name;
    private String capacity;
    private double latitude;
    private double longitude;
    private String phoneNumber;
    private String restrictions;
    private String specialNotes;
    private int vacancies;
    private int uniqueKey;

    /*
        Returns the public list of shelters fetched by FirebaseHandler.
        @return List<Shelter> of all shelters in the database.
         */
    public static List<Shelter> getShelters() {
        return shelters;
    }

    /*
        Sets the shelters in the global list. Used by the FirebaseHandler class.
        @param shelters The new list of shelters to set to.
         */
    public static void setShelters(Collection<Shelter> shelters) {
        Shelter.shelters = new ArrayList<>();
        Shelter.shelters.addAll(shelters);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
     this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public int getUniqueKey() {
        return uniqueKey;
    }

    public int getVacancies() {
        return vacancies;
    }

    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }

    public void save() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("shelters");
        DatabaseReference snapOfThis = ref.child(this.name);
        snapOfThis.setValue(name);
        snapOfThis.child("Vacancies").setValue(vacancies);
        snapOfThis.child("Unique Key").setValue(""+uniqueKey);
        snapOfThis.child("Special Notes").setValue(specialNotes);
        snapOfThis.child("Restrictions").setValue(restrictions);
        snapOfThis.child("Phone Number").setValue(phoneNumber);
        snapOfThis.child("Longitude").setValue(""+longitude);
        snapOfThis.child("Latitude").setValue(""+latitude);
        snapOfThis.child("Capacity").setValue(capacity);

    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    public void setUniqueKey(int uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
}