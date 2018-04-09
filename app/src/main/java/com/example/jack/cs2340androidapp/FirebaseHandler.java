package com.example.jack.cs2340androidapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jack on 2/27/18.
 */

public class FirebaseHandler {

    public void initialize() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("shelters");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, HashMap<String, String>> map =
                        (HashMap<String, HashMap<String, String>>)dataSnapshot.getValue();
                List<String> shelterNames = new ArrayList();
                try {
                    shelterNames = new ArrayList<>(map.keySet());
                } catch (java.lang.NullPointerException e) {}
                List<Shelter> newShelterList = new ArrayList<>();
                for (String name: shelterNames) {
                    try {
                        Shelter tempShelter = new Shelter();
                        HashMap<String, String> params = map.get(name);
                        tempShelter.setCapacity(params.get("Capacity"));
                        tempShelter.setLatitude(Double.parseDouble(params.get("Latitude")));
                        tempShelter.setLongitude(Double.parseDouble(params.get("Longitude")));
                        tempShelter.setName(name);
                        tempShelter.setPhoneNumber(params.get("Phone Number"));
                        tempShelter.setRestrictions(params.get("Restrictions"));
                        tempShelter.setSpecialNotes(params.get("Special Notes"));
                        Long vacancies = new Long(0);
                        try {
                            vacancies = (Long)dataSnapshot.child(name)
                                    .child("Vacancies").getValue();
                        } catch (java.lang.NullPointerException e) {}
                        tempShelter.setVacancies(((int)(long)vacancies));
                        tempShelter.setUniqueKey(Integer.parseInt(params.get("Unique Key")));
                        newShelterList.add(tempShelter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ShelterModel.setShelters(newShelterList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.err.println(error.getMessage());
            }
        });
    }
}
