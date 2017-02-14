package com.example.adrien.gift_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IdeasAdapter extends ArrayAdapter<Idea> implements Filterable {

    private DatabaseReference mDatabase;
    private Idea idea;
    private ArrayList<Idea> ideasArray;
    private ArrayList<Idea> ideasArrayFilter;

    public IdeasAdapter(Context context, ArrayList<Idea> ideas){

        super(context, 0, ideas);
        this.ideasArray = ideas;
        this.ideasArrayFilter = ideas;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Init global variables

        idea = getItem(position);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_ideas_item, parent, false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.cardidea_photo_image);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.cardidea_info_title);
        TextView tvRecipient = (TextView)convertView.findViewById(R.id.cardidea_photo_text);
        TextView tvDate = (TextView)convertView.findViewById(R.id.cardidea_info_date_text);
        TextView tvUrl = (TextView)convertView.findViewById(R.id.cardidea_info_url_text);
        TextView tvPrice = (TextView)convertView.findViewById(R.id.cardidea_info_price_text);
        ImageView imageViewIcon = (ImageView)convertView.findViewById(R.id.cardidea_photo_icon);

        if(idea.getPhoto() != "pas d'image"){
            imageViewIcon.setVisibility(View.INVISIBLE);
            try {
                Bitmap imageBitmap = decodeFromFirebasebase64(idea.getPhoto());
                imageView.setImageBitmap(imageBitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }



        tvTitle.setText(idea.getTitle());
        tvDate.setText(idea.getForWhen());
        tvUrl.setText(idea.getUrl());
        tvRecipient.setText(idea.getRecipient());
        tvPrice.setText(""+idea.getPrice());

        return convertView;
    }

    @Nullable
    @Override
    public Idea getItem(int position) {
        return ideasArray.get(position);
    }

    @Override
    public int getCount() {
        return ideasArray.size();
    }

    // method for displaying suggestions when user start writing

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Idea> FilteredArrayIdeas = new ArrayList<Idea>();

                if(constraint == null || constraint.length() == 0){
                    results.count = ideasArrayFilter.size();
                    results.values = ideasArrayFilter;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for(int i=0; i < ideasArray.size(); i++){
                        Idea ideaCursor = ideasArray.get(i);
                        if(ideaCursor.getTitle().toLowerCase().startsWith(constraint.toString())){
                            FilteredArrayIdeas.add(ideaCursor);
                        }
                    }
                    results.count = FilteredArrayIdeas.size();
                    results.values = FilteredArrayIdeas;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results.count != 0){

                    ideasArray = (ArrayList<Idea>)results.values;
                    notifyDataSetChanged();
                }

            }
        };

    }

    public static Bitmap decodeFromFirebasebase64(String image) throws IOException {
        byte[] decodeByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodeByteArray, 0, decodeByteArray.length);
    }
}
