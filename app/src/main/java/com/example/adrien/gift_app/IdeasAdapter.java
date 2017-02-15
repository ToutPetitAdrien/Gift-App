package com.example.adrien.gift_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        ImageView crossButton = (ImageView)convertView.findViewById(R.id.cardidea_cross);

        //check if the idea has the default value, this means there is no image
        if(!(idea.getPhoto().equals("empty"))){
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

        crossButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){ //Open dialog to confirm a removal
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                idea.removeFromFirebase(mDatabase);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Etes-vous sûr de vouloir supprimer l'évènement ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
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

    @Override
    public Filter getFilter() {//Method to filter ideas when user start writing in the searchview, call in setOnQueryTextListener.
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Idea> FilteredArrayIdeas = new ArrayList<Idea>();//ArrayList with only ideas which match whith constrains

                if(constraint == null || constraint.length() == 0){
                    results.count = ideasArrayFilter.size();
                    results.values = ideasArrayFilter;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for(int i=0; i < ideasArray.size(); i++){
                        Idea ideaCursor = ideasArray.get(i);
                        if(ideaCursor.getTitle().toLowerCase().startsWith(constraint.toString())){ //check if the beginning of a title match with few little written by user
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

    //function to transform String data from Firebase to a Bitmap object
    public static Bitmap decodeFromFirebasebase64(String image) throws IOException {
        byte[] decodeByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodeByteArray, 0, decodeByteArray.length);
    }
}
