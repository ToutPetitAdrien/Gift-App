package com.example.adrien.gift_app;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrien on 28/12/2016.
 */

public class Idea {

    private String title;
    private int price;
    private String url;
    private String photo;
    private String description;
    private String recipient;
    private String createdBy;

    public Idea(){

    }

    public String getTitle(){
        return this.title;
    }

    public int getPrice(){
        return this.price;
    }

    public String getUrl(){
        return this.url;
    }

    public String getPhoto(){
        return this.photo;
    }

    public String getDescription(){
        return this.description;
    }

    public String getRecipient(){
        return this.recipient;
    }

    public String getCreatedBy(){
        return this.createdBy;
    }

    public void setTitle(String pTitle){
        this.title = pTitle;
    }

    public void setPrice(int pPrix){
        this.price = pPrix;
    }

    public void setUrl(String pUrl){
        this.url = pUrl;
    }

    public void setPhoto(String pPhoto){
        this.photo = pPhoto;
    }

    public void setDescription(String pDescription){
        this.description = pDescription;
    }

    public void setRecipient(String pRecipient){
        this.recipient = pRecipient;
    }

    public void addToFirebase(String userId, DatabaseReference mDatabase){
        String key = mDatabase.child("ideas").push().getKey();

        HashMap<String, Object> ideaValues = new HashMap<>();
        ideaValues.put("CreatedBy", userId);
        ideaValues.put("Recipient", this.recipient);
        ideaValues.put("Title", this.title);
        ideaValues.put("Price", this.price);
        ideaValues.put("Url", this.url);
        ideaValues.put("Photo", this.photo);
        ideaValues.put("Description", this.description);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/ideas/" + key, ideaValues);

        mDatabase.updateChildren(childUpdates);

    }
}