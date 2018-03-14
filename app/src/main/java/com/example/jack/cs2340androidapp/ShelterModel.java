package com.example.jack.cs2340androidapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 2/27/18.
 */

public class ShelterModel {

    private static List<Shelter> shelters = new ArrayList<Shelter>();


    public static List<Shelter> getShelters() {
        return shelters;
    }

    public static void setShelters(List<Shelter> shelters) {
        ShelterModel.shelters = shelters;
    }
}

class Shelter {

    private String name;
    private String capacity;
    private double latitude;
    private double longitude;
    private String phonenumber;
    private String restrictions;
    private String specialnotes;
    private int vacancies;
    private int uniqueKey;

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

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getSpecialnotes() {
        return specialnotes;
    }

    public void setSpecialnotes(String specialnotes) {
        this.specialnotes = specialnotes;
    }

    public int getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(int uniqueKey) {
        this.uniqueKey = uniqueKey;
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
        ref.child(this.name).setValue(name);
        ref.child(this.name).child("Vacancies").setValue(vacancies);
        ref.child(this.name).child("Unique Key").setValue(""+uniqueKey);
        ref.child(this.name).child("Special Notes").setValue(specialnotes);
        ref.child(this.name).child("Restrictions").setValue(restrictions);
        ref.child(this.name).child("Phone Number").setValue(phonenumber);
        ref.child(this.name).child("Longitude").setValue(""+longitude);
        ref.child(this.name).child("Latitude").setValue(""+latitude);
        ref.child(this.name).child("Capacity").setValue(capacity);

    }
}