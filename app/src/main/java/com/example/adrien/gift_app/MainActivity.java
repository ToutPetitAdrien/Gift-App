package com.example.adrien.gift_app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // To check if the user is logged in or not
        if (AccessToken.getCurrentAccessToken() == null){
            Intent goToFacebookLoginActivity = new Intent(this, FacebookLoginActivity.class);
            startActivity(goToFacebookLoginActivity);
        }

        final LinearLayout eventButton = (LinearLayout)findViewById(R.id.navbar_event);
        final LinearLayout addIdeasButton = (LinearLayout)findViewById(R.id.navbar_addideas);
        final LinearLayout ideasButton = (LinearLayout)findViewById(R.id.navbar_idea);
        final View eventButtonUnderline = findViewById(R.id.navbar_event_underline);
        final View addIdeasButtonUnderline = findViewById(R.id.navbar_addideas_underline);
        final View ideasButtonUnderline = findViewById(R.id.navbar_idea_underline);

        // switch between fragments with the bottom navbar
        createFragment("addidea", R.id.id_frame);
        eventButtonUnderline.setVisibility(View.INVISIBLE);
        addIdeasButtonUnderline.setVisibility(View.VISIBLE);
        ideasButtonUnderline.setVisibility(View.INVISIBLE);

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragment("event", R.id.id_frame);
                eventButtonUnderline.setVisibility(View.VISIBLE);
                addIdeasButtonUnderline.setVisibility(View.INVISIBLE);
                ideasButtonUnderline.setVisibility(View.INVISIBLE);
            }
        });

        addIdeasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragment("addidea", R.id.id_frame);
                eventButtonUnderline.setVisibility(View.INVISIBLE);
                addIdeasButtonUnderline.setVisibility(View.VISIBLE);
                ideasButtonUnderline.setVisibility(View.INVISIBLE);
            }
        });

        ideasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragment("idea", R.id.id_frame);
                eventButtonUnderline.setVisibility(View.INVISIBLE);
                addIdeasButtonUnderline.setVisibility(View.INVISIBLE);
                ideasButtonUnderline.setVisibility(View.VISIBLE);
            }
        });
    }

    // fonction for fragment creation process
    public void createFragment(String fragType, int id){
        FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch (fragType){
            case "event": Fragment newEventsFragment = new EventsFragment();
                fragmentTransaction.replace(id, newEventsFragment);
                break;
            case "addidea": Fragment newAddIdeasFragment = new IdeasFormFragment();
                fragmentTransaction.replace(id, newAddIdeasFragment);
                break;
            case "idea": Fragment newIdeasFragment = new IdeasListFragment();
                fragmentTransaction.replace(id, newIdeasFragment);
                break;
        }
        fragmentTransaction.commit();
    }
}


