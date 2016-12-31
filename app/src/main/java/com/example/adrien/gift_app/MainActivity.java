package com.example.adrien.gift_app;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabLayout navbar = (TabLayout)findViewById(R.id.id_tab);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("MainActivity","hello: "+ user.getUid());

        navbar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = fm.beginTransaction();
                switch(navbar.getSelectedTabPosition()) {
                    case 0:
                        Fragment listEventsFragment = new EventsFragment();
                        fragmentTransaction.replace(R.id.id_frame, listEventsFragment);
                        break;
                    case 1:
                        Fragment addIdeasFragment = new IdeasFormFragment();
                        fragmentTransaction.replace(R.id.id_frame, addIdeasFragment);
                        break;
                    case 2:
                        break;
                    default:
                        Log.d("MainActivity", "Hello : c'est un test");
                }
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                
            }
        });

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("EventsFragment", "onChildAdded:"+ dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("EventsFragment", "onChildChanged:"+ dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("EventsFragment", "onChildRemoved:"+ dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("EventsFragment", "onChildMoved:"+ dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}


