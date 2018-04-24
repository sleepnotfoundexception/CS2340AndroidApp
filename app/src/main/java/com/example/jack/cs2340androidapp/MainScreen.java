package com.example.jack.cs2340androidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    private static SharedPreferences sharedPref;
    private static final int RC_SIGN_IN = 123;
    public static FirebaseUser activeUser;
    public static User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        FirebaseApp.initializeApp(this);
        FirebaseHandler firebase = new FirebaseHandler();
    }

    public void openLogin(View view) {
// ...

// Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                /*new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()*/);

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.BlueAuth)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                activeUser = user;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference("users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, HashMap<String, String>> map =
                                (HashMap<String, HashMap<String, String>>)dataSnapshot.getValue();
                        List<String> uids = new ArrayList<String>(map.keySet());
                        if (!uids.contains(user.getUid())) {
                            //create user
                            Intent moveToRegister = new Intent(MainScreen.this, Registration.class);
                            startActivity(moveToRegister);
                        } else {
                            //is returning user
                            String name = map.get(MainScreen.activeUser.getUid()).get("name");
                            String city = map.get(MainScreen.activeUser.getUid()).get("city");
                            String phone = map.get(MainScreen.activeUser.getUid()).get("phone");
                            String admin = map.get(MainScreen.activeUser.getUid()).get("admin");
                            Pair<Integer, Integer> reservation = null;
                            if (map.get(MainScreen.activeUser.getUid()).get("Reservation") != null) {
                                HashMap<String, Long> reservationMap = (HashMap<String, Long>)dataSnapshot.child(MainScreen.activeUser.getUid()).child("Reservation").getValue();
                                reservation = new Pair((int)(long)(Long)reservationMap.get("first"), (int)(long)(Long)reservationMap.get("second"));
                            }
                            boolean isAdmin = false;
                            if (admin != null && admin.equals("true")) {
                                isAdmin = true;
                            }
                            userData = new User(name, city, MainScreen.activeUser.getEmail(), phone, isAdmin);
                            userData.setReservation(reservation);
                            Intent moveToApp = new Intent(MainScreen.this, Application.class);
                            moveToApp.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            moveToApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            moveToApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(moveToApp);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println(error.getMessage());
                    }
                });
                // ...
            } else {
                activeUser = null;
                userData = null;
            }
        }
    }
}
