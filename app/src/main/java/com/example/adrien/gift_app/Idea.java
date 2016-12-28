package com.example.adrien.gift_app;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.net.URI;

/**
 * Created by Adrien on 28/12/2016.
 */

public class Idea {

    public String title;
    public int price;
    public String url;
    public String photo;
    public String description;
    public String recipient;
    public String createdBy;

    public Idea(){

    }

    public Idea(String pCreatedBy, String pTitle, int pPrix, String pUrl, String pPhoto, String pDescription, String pDestinataire){

        this.title = pTitle;
        this.price = pPrix;
        this.url = pUrl;
        this.photo = pPhoto;
        this.description = pDescription;
        this.recipient = pDestinataire;
        this.createdBy = pCreatedBy;
    }



    private String getTitle(){
        return this.title;
    }

    private int getPrice(){
        return this.price;
    }

    private String getUrl(){
        return this.url;
    }

    private String getPhoto(){
        return this.photo;
    }

    private String getDescription(){
        return this.description;
    }

    private String getRecipient(){
        return this.recipient;
    }
}