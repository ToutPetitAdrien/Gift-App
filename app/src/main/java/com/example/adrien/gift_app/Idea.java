package com.example.adrien.gift_app;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


public class Idea {

    private String title;
    private int price;
    private String url;
    private String photo;
    private String description;
    private String recipient;
    private String createdBy;
    private String forWhen;
    private String key;

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

    public String getForWhen(){
        return this.forWhen;
    }

    public String getKey(){
        return this.key;
    }

    public void setKey(String pKey){
        this.key = pKey;
    }

    public void setForWhen(String pForWhen){
        this.forWhen = pForWhen;
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
        ideaValues.put("createdBy", userId);
        ideaValues.put("recipient", this.recipient);
        ideaValues.put("title", this.title);
        ideaValues.put("price", this.price);
        ideaValues.put("url", this.url);
        ideaValues.put("photo", this.photo);
        ideaValues.put("description", this.description);
        ideaValues.put("forWhen", this.forWhen);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/ideas/" + key, ideaValues);

        mDatabase.updateChildren(childUpdates);
    }
}