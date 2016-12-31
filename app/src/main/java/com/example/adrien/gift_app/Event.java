package com.example.adrien.gift_app;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrien on 30/12/2016.
 */

public class Event {

    private String title;
    private String date;
    private String place;

    public Event(){

    }

    public String getTitle(){
        return this.title;
    }

    public String getDate(){
        return this.date;
    }

    public String getPlace(){
        return this.place;
    }

    public void setTitle(String pTitle){
        this.title = pTitle;
    }

    public void setPlace(String pPlace){
        this.place = pPlace;
    }

    public void setDate(String pDate){
        this.date = pDate;
    }

    public void addToFirebase(String userId, DatabaseReference mDatabase){
        String key = mDatabase.child("events").push().getKey();

        HashMap<String, Object> eventValues = new HashMap<>();
        eventValues.put("CreatedBy", userId);
        eventValues.put("Title", this.title);
        eventValues.put("Date", this.date);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/events/" + key, eventValues);

        mDatabase.updateChildren(childUpdates);
    }
}
