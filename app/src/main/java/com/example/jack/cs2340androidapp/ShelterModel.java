package com.example.jack.cs2340androidapp;

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
    private String Capacity;
    private double latitude;
    private double longitude;
    private String phonenumber;
    private String restrictions;
    private String specialnotes;
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
        return Capacity;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
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
}