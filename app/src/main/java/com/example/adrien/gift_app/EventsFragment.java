package com.example.adrien.gift_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
        Calendar calendar = Calendar.getInstance(); //to deal with date

        final TextView create_event = (TextView) view.findViewById(R.id.newbutton_text);
        final ListView listView = (ListView) view.findViewById(R.id.listevents);
        final RelativeLayout create_event_layout = (RelativeLayout) view.findViewById(R.id.newbutton);

        // Button for new event
        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction  = fm.beginTransaction();
                Fragment addEventFragment = new EventsFormFragment();
                fragmentTransaction.replace(R.id.fragment_eventsform_frame, addEventFragment);
                fragmentTransaction.commit();
                create_event_layout.setVisibility(View.GONE); //hide button when user clicked
            }
        });

        ArrayList<Event> arrayOfEvents = new ArrayList<Event>();
        adapter = new EventsAdapter(this.getContext(), arrayOfEvents);
        listView.setAdapter(adapter); //give the list of Event to the adapter
        final int currentIntegerDate = calendar.get(Calendar.DAY_OF_MONTH) + 100*calendar.get(Calendar.MONTH) + 10000*calendar.get(Calendar.YEAR); // int to represent current date
        Query eventsSortedByDate = mDatabase.child("events").orderByChild("integerdate"); //Order event with the most recent on the top

        eventsSortedByDate.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event newEvent = dataSnapshot.getValue(Event.class); //target the event just created
                newEvent.setKey(dataSnapshot.getKey());
                if(user.getUid().equals(newEvent.getCreatedBy()) && newEvent.getIntegerdate() > currentIntegerDate){ //check if the event date is past and if the event belong to the current user
                    adapter.add(newEvent); //populate the adapter
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Event newEvent = dataSnapshot.getValue(Event.class); //target the event just removed
                if(user.getUid().equals(newEvent.getCreatedBy())){ //check if the event belong to the current user
                    adapter.remove(newEvent); //remove from the adapter
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