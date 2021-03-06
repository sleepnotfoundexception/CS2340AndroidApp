package com.example.jack.cs2340androidapp;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Fetches data from Firebase.
 */
class FirebaseHandler {

    public static FirebaseUser activeUser;
    public static User userData;

    /**
    Loads the Firebase database into the ShelterModel's public global list.
    Must be called in order for data to be loaded.
     */
    public void initialize() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("shelters");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String, HashMap<String, String>> map =
                        (HashMap<String, HashMap<String, String>>)dataSnapshot.getValue();
                Iterable<String> shelterNames;
                assert map != null;
                shelterNames = new ArrayList<>(map.keySet());
                Collection<Shelter> newShelterList = new ArrayList<>();
                for (String name: shelterNames) {
                    try {
                        Shelter tempShelter = new Shelter();
                        Map<String, String> params = map.get(name);
                        tempShelter.setCapacity(params.get("Capacity"));
                        tempShelter.setLatitude(Double.parseDouble(params.get("Latitude")));
                        tempShelter.setLongitude(Double.parseDouble(params.get("Longitude")));
                        tempShelter.setName(name);
                        tempShelter.setPhoneNumber(params.get("Phone Number"));
                        tempShelter.setRestrictions(params.get("Restrictions"));
                        tempShelter.setSpecialNotes(params.get("Special Notes"));
                        DataSnapshot node1 = dataSnapshot.child(name);
                        DataSnapshot vacanciesSn = node1.child("Vacancies");
                        Long vacancies = (Long)vacanciesSn.getValue();
                        assert vacancies != null;
                        tempShelter.setVacancies(((int)vacancies.longValue()));
                        tempShelter.setUniqueKey(Integer.parseInt(params.get("Unique Key")));
                        newShelterList.add(tempShelter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Shelter.setShelters(newShelterList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
}
