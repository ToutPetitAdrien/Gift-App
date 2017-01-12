package com.example.adrien.gift_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class IdeasAdapter extends ArrayAdapter<Idea> {

    private DatabaseReference mDatabase;

    public IdeasAdapter(Context context, ArrayList<Idea> ideas){

        super(context, 0, ideas);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Idea idea = getItem(position);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageView imageView = (ImageView)convertView.findViewById(R.id.id_imageView);



        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_idea, parent, false);
        }

        return super.getView(position, convertView, parent);
    }
}
