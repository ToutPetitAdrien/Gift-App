package com.example.adrien.gift_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class IdeasAdapter extends ArrayAdapter<Idea> implements Filterable {

    private DatabaseReference mDatabase;
    private Idea idea;
    private ArrayList<Idea> ideasArray;

    public IdeasAdapter(Context context, ArrayList<Idea> ideas){

        super(context, 0, ideas);
        this.ideasArray = ideas;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        idea = getItem(position);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_idea, parent, false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.id_imageView);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.id_ideatitle);
        TextView tvRecipient = (TextView)convertView.findViewById(R.id.id_idearecipient);
        TextView tvDate = (TextView)convertView.findViewById(R.id.id_ideadate);
        TextView tvUrl = (TextView)convertView.findViewById(R.id.id_ideaurl);
        TextView tvPrice = (TextView)convertView.findViewById(R.id.id_ideaprice);
        TextView tvDescription = (TextView)convertView.findViewById(R.id.id_ideadescription);

        tvTitle.setText(idea.getTitle());
        tvDate.setText(idea.getForWhen());
        tvUrl.setText(idea.getUrl());
        tvRecipient.setText(idea.getRecipient());
        tvPrice.setText(idea.getPrice());
        tvDescription.setText(idea.getDescription());

        return convertView;
    }
//
//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();
//                ArrayList<Idea> FilteredArrayIdeas = new ArrayList<Idea>();
//
//                constraint = constraint.toString().toLowerCase();
//                for(int i=0; i < ideasArray.size(); i++){
//                    Idea ideaCursor = ideasArray.get(i);
//                    if(ideaCursor.getTitle().toLowerCase().startsWith(constraint.toString())){
//                        FilteredArrayIdeas.add(ideaCursor);
//                    }
//                }
//
//                results.count = FilteredArrayIdeas.size();
//                results.values = FilteredArrayIdeas;
//
//                return results;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                ideasArray = (ArrayList<Idea>)results.values;
//                notifyDataSetChanged();
//            }
//        };
//
//    }
}
