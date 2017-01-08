package com.example.adrien.gift_app;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import java.util.HashMap;
import java.util.Map;

public class Event {

    private String title;
    private int day;
    private String place;
    private String createdBy;
    private int month;
    private String key;

    public Event(){

    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getTitle(){
        return this.title;
    }

    public int getDay(){
        return this.day;
    }

    public int getMonth(){
        return this.month;
    }

    public String getPlace(){
        return this.place;
    }

    public String getKey(){
        return this.key;
    }

    public void setKey(String pKey){
        this.key = pKey;
    }

    public void setTitle(String pTitle){
        this.title = pTitle;
    }

    public void setPlace(String pPlace){
        this.place = pPlace;
    }

    public void setDay(int pDay){
        this.day = pDay;
    }

    public void setMonth(int pMonth){
        this.month = pMonth;
    }

    public void addToFirebase(String userId, DatabaseReference mDatabase){
        String key = mDatabase.child("events").push().getKey();

        HashMap<String, Object> eventValues = new HashMap<>();
        eventValues.put("createdBy", userId);
        eventValues.put("title", this.title);
        eventValues.put("day", this.day);
        eventValues.put("month", this.month);
        eventValues.put("place", this.place);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/events/" + key, eventValues);
        mDatabase.updateChildren(childUpdates);
    }

    public void removeFromFirebase(DatabaseReference mDatabase){
        mDatabase.child("events").child(this.key).removeValue();
    }
}
