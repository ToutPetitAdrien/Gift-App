package com.example.adrien.gift_app;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Event {

    private String title;
    private int day;
    private String place;
    private String createdBy;
    private int month;
    private int year;
    private String key;
    private int integerdate;

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

    public int getYear(){
        return this.year;
    }

    public String getPlace(){
        return this.place;
    }

    public String getKey(){
        return this.key;
    }

    public int getIntegerdate() {
        return integerdate;
    }

    public void setYear(int pYear){
        this.year = pYear;
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
        this.integerdate = this.day + 100*this.month + 10000*this.year;

        HashMap<String, Object> eventValues = new HashMap<>();
        eventValues.put("createdBy", userId);
        eventValues.put("title", this.title);
        eventValues.put("day", this.day);
        eventValues.put("month", this.month);
        eventValues.put("year", this.year);
        eventValues.put("place", this.place);
        eventValues.put("integerdate", this.integerdate);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/events/" + key, eventValues);
        mDatabase.updateChildren(childUpdates);
    }

    public void removeFromFirebase(DatabaseReference mDatabase){
        mDatabase.child("events").child(this.key).removeValue();
    }
}
