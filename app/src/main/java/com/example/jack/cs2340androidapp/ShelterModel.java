package com.example.jack.cs2340androidapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores shelter data from Firebase
 * Created by jack on 2/27/18.
 */

@SuppressWarnings("UtilityClass")
class ShelterModel {

    private static List<Shelter> shelters = new ArrayList<>();

    public static List<Shelter> getShelters() {
        return shelters;
    }

    public static void setShelters(List<Shelter> shelters) {
        ShelterModel.shelters = shelters;
    }
}