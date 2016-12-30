package com.example.adrien.gift_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText textTitle = (EditText)findViewById(R.id.editText_title);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("MainActivity","hello: "+ user.getUid());

        Idea idea = new Idea();
        idea.setTitle("PS4");
        idea.setPrice(400);
        idea.setDescription("Un kdo de l'enfer !");
        idea.setUrl("http:// Un URL");
        idea.setPhoto("Une photo trop belle");
        idea.setRecipient("Océane");
        idea.addToFirebase(user.getUid(), mDatabase);
    }

}


