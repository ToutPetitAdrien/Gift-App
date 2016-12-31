package com.example.adrien.gift_app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventsFormFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_events_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final EditText textTitle = (EditText)view.findViewById(R.id.editText_title_event);
        final EditText textDate = (EditText)view.findViewById(R.id.editText_date_event);
        final EditText textPlace = (EditText)view.findViewById(R.id.editText_place_event);
        Button addEvent = (Button)view.findViewById(R.id.id_addButton);
        Button cancelEvent = (Button)view.findViewById(R.id.id_cancelButton);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event newEvent = new Event();
                newEvent.setTitle(textTitle.getText().toString());
                newEvent.setDate(textDate.getText().toString());
                newEvent.setPlace(textPlace.getText().toString());
                newEvent.addToFirebase(user.getUid(), mDatabase);
            }
        });

        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.id_fragment_addEvents)).commit();
            }
        });

    }
}
