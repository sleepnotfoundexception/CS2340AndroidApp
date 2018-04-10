package com.example.jack.cs2340androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The opening screen for the app.
 */
public class MainScreen extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        FirebaseApp.initializeApp(this);
        FirebaseHandler firebase = new FirebaseHandler();
        firebase.initialize();
    }

    /**
    Creates a Google Firebase login screen and starts the activity. Entirely Google-provided
    boilerplate code.
    @param view Required by buttonPress function.
     */
    public void openLogin(View view) {
// ...

        //Google boilerplate code
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
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseAuth auth = FirebaseAuth.getInstance();
                final FirebaseUser user = auth.getCurrentUser();
                FirebaseHandler.activeUser = user;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference("users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, HashMap<String, String>> map
                                = (HashMap<String, HashMap<String, String>>)dataSnapshot.getValue();
                            assert map != null;
                            Collection<String> uIDs = new ArrayList<>(map.keySet());
                            assert user != null;
                            if (!uIDs.contains(user.getUid())) {
                                //create user
                                Intent moveToRegister = new Intent(MainScreen.this,
                                        Registration.class);
                                startActivity(moveToRegister);
                            } else {
                                //is returning user
                                HashMap activeUser = map.get(FirebaseHandler.activeUser.getUid());
                                String name = (String)activeUser.get("name");
                                String city = (String)activeUser.get("city");
                                String phone = (String)activeUser.get("phone");
                                String admin = (String)activeUser.get("admin");
                                Pair<Integer, Integer> reservation = new Pair<>(0, 0);
                                Map<String, String> uid = map.get(FirebaseHandler.activeUser
                                        .getUid());
                                if (uid.get("Reservation") != null) {
                  //Java doesn't have reified generics, so type erasure is inevitable here
                                        DataSnapshot user =
                                                dataSnapshot.child(FirebaseHandler.activeUser
                                                        .getUid());
                                        DataSnapshot reservationSnap = user.child("Reservation");
                                        HashMap<String, Long> reservationMap =
                                                (HashMap<String, Long>) reservationSnap.getValue();
                                        assert reservationMap != null;
                                        reservation =
                                                new Pair((int)(long)reservationMap.get("first"),
                                                        (int)(long)reservationMap.get("second"));
                                }
                                boolean isAdmin = false;
                                if ((admin != null) && "true".equals(admin)) {
                                    isAdmin = true;
                                }
                                FirebaseHandler.userData = new User(name, city,
                                        //FirebaseHandler.activeUser.getEmail(),
                                        phone, isAdmin);
                                FirebaseHandler.userData.setReservation(reservation);
                                Intent moveToApp = new Intent(MainScreen.this,
                                        Application.class);
                                moveToApp.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                moveToApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                moveToApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(moveToApp);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
                // ...
            }
        }
    }
}
