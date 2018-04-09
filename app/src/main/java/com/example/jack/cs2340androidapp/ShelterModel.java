package com.example.jack.cs2340androidapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores shelter data from Firebase
 * Created by jack on 2/27/18.
 */

class ShelterModel {

    private static List<Shelter> shelters = new ArrayList<>();

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
    public static void setShelters(List<Shelter> shelters) {
        ShelterModel.shelters = shelters;
    }
}