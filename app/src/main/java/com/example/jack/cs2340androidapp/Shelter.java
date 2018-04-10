package com.example.jack.cs2340androidapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Storage class for shelters.
 */
public class Shelter {

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

    /**
     * Builds a new Shelter with given parameters.
     * @param name Shelter name
     * @param capacity Shelter capacity
     * @param phoneNumber Shelter phone number
     * @param restrictions Shelter living restrictions
     * @param specialNotes Any notes about the shelter
     */
    public Shelter(String name, String capacity, String phoneNumber, String restrictions,
                   String specialNotes) {
        this.name = name;
        this.capacity = capacity;
        this.phoneNumber = phoneNumber;
        this.restrictions = restrictions;
        this.specialNotes = specialNotes;
    }

    /**
     * Creates a new shelter with default parameters.
     */
    public Shelter() {
        this("","","","","");
    }
    /**
        Returns the public list of shelters fetched by FirebaseHandler.
        @return List<Shelter> of all shelters in the database.
         */
    public static List<Shelter> getShelters() {
        return shelters;
    }

    /**
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

    /**
     * Gets the shelter's name
     * @return The shelter's name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the shelter's name
     * @param name The new name
     */
    public void setName(String name) {
     this.name = name;
    }

    /**
     * Getter for the capacity
     * @return The shelter's capacity
     */
    public String getCapacity() {
        return capacity;
    }

    /**
     * Getter for the latitude
     * @return The latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter for the longitude
     * @return The longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter for the phone number
     * @return The phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Getter for the restrictions
     * @return The restrictions
     */
    public String getRestrictions() {
        return restrictions;
    }

    /**
     * Getter for the shelter's special notes
     * @return Any special notes taken
     */
    public String getSpecialNotes() {
        return specialNotes;
    }

    /**
     * Getter for the shelter's uID.
     * @return The shelter's uID.
     */
    public int getUniqueKey() {
        return uniqueKey;
    }

    /**
     * Getter for the vacancies.
     * @return The shelter's vacancies.
     */
    public int getVacancies() {
        return vacancies;
    }

    /**
     * Setter for the shelter's vacancies.
     * @param vacancies The new vacancy count
     */
    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }

    /**
     * Serializes the shelter and saves it to Firebase.
     */
    public void save() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("shelters");
        DatabaseReference snapOfThis = ref.child(this.name);
        snapOfThis.setValue(name);
        DatabaseReference vac = snapOfThis.child("Vacancies");
        vac.setValue(vacancies);
        DatabaseReference uID = snapOfThis.child("Unique Key");
        uID.setValue(""+uniqueKey);
        DatabaseReference sN = snapOfThis.child("Special Notes");
        sN.setValue(specialNotes);
        DatabaseReference res = snapOfThis.child("Restrictions");
        res.setValue(restrictions);
        DatabaseReference phone = snapOfThis.child("Phone Number");
        phone.setValue(phoneNumber);
        DatabaseReference longitude = snapOfThis.child("Longitude");
        longitude.setValue(""+longitude);
        DatabaseReference latitude = snapOfThis.child("Latitude");
        latitude.setValue(""+latitude);
        DatabaseReference cap = snapOfThis.child("Capacity");
        cap.setValue(capacity);

    }

    /**
     * Setter for shelter capacity.
     * @param capacity Shelter capacity.
     */
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    /**
     * Setter for latitude
     * @param latitude The shelter's new latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Setter for longitude
     * @param longitude The new longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Setter for phone number
     * @param phoneNumber The new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Setter for shelter restrictions
     * @param restrictions New restrictions
     */
    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    /**
     * Setter for special notes
     * @param specialNotes The new set of special notes
     */
    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    /**
     * Setter for unique key
     * @param uniqueKey The new uID.
     */
    public void setUniqueKey(int uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
}