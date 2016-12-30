package com.example.adrien.gift_app;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        final TabLayout navbar = (TabLayout)findViewById(R.id.id_tab);
        Log.d("MainActivity", "Hello :" + navbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("MainActivity","hello: "+ user.getUid());

        navbar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(navbar.getSelectedTabPosition()) {
                    case 0:
                        Log.d("MainActivity", "Hello : c'est la case 1");
                        break;
                    case 1:
                        Log.d("MainActivity", "Hello : c'est la case 2");
                        break;
                    case 2:
                        Log.d("MainActivity", "Hello : c'est la case 3");
                        break;
                    default:
                        Log.d("MainActivity", "Hello : c'est un test");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



}


