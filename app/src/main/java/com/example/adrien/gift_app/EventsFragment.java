package com.example.adrien.gift_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class EventsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private EventsAdapter adapter;

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final TextView create_event = (TextView) view.findViewById(R.id.button_create_event);
        final ListView listView = (ListView) view.findViewById(R.id.listevents);
        final RelativeLayout newbutton = (RelativeLayout) view.findViewById(R.id.newbutton);

        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction  = fm.beginTransaction();
                Fragment addEventFragment = new EventsFormFragment();
                fragmentTransaction.replace(R.id.fragment_eventsform_frame, addEventFragment);
                fragmentTransaction.commit();
                newbutton.setVisibility(View.GONE);
            }
        });

        // Creation event adapter

        ArrayList<Event> arrayOfEvents = new ArrayList<Event>();
        adapter = new EventsAdapter(this.getContext(), arrayOfEvents);
        listView.setAdapter(adapter);

        // To sort event by date

        Calendar calendar = Calendar.getInstance();
        final int currentIntegerDate = calendar.get(Calendar.DAY_OF_MONTH) + 100*calendar.get(Calendar.MONTH) + 10000*calendar.get(Calendar.YEAR);
        Query eventsSortedByDate = mDatabase.child("events").orderByChild("integerdate");

        // Update UI when a new event is created

        eventsSortedByDate.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("EventsFragment", "onChildAdded:"+ dataSnapshot.getKey());
                Event newEvent = dataSnapshot.getValue(Event.class);
                newEvent.setKey(dataSnapshot.getKey());

                if(user.getUid().equals(newEvent.getCreatedBy()) && newEvent.getIntegerdate() > currentIntegerDate){
                    adapter.add(newEvent);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("EventsFragment", "onChildRemoved:"+ dataSnapshot.getValue(Event.class).getTitle());
                Event newEvent = dataSnapshot.getValue(Event.class);
                if(user.getUid().equals(newEvent.getCreatedBy())){
                    adapter.remove(newEvent);
                }
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}