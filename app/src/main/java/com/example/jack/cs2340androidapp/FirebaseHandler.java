package com.example.jack.cs2340androidapp;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
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
public class FirebaseHandler {

    private static FirebaseUser activeUser;

    private static User userData;

    /**
     * Stores the currently authenticated Firebase user.
     */
    public static UserInfo getActiveUser() {
        return activeUser;
    }

    /**
     * Sets the active user to a FirebaseUser
     * @param activeUser The new FirebaseUser.
     */
    public static void setActiveUser(FirebaseUser activeUser) {
        FirebaseHandler.activeUser = activeUser;
    }

    /**
     * Stores the user object associated with the currently authenticated user.
     */
    public static User getUserData() {
        return userData;
    }

    /**
     * Sets user data field.
     */
    public static void setUserData(User userData) {
        FirebaseHandler.userData = userData;
    }

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
                        Long vacancies = (Long)dataSnapshot.child(name)
                                .child("Vacancies").getValue();
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
