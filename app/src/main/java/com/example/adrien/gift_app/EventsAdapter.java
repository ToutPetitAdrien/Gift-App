package com.example.adrien.gift_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class EventsAdapter extends ArrayAdapter<Event> {

    private DatabaseReference mDatabase;

    public EventsAdapter(Context context, ArrayList<Event> events){

        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Event event = getItem(position);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }
        TextView tvDay = (TextView)convertView.findViewById(R.id.textView_day);
        TextView tvMonth = (TextView)convertView.findViewById(R.id.textView_month);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.textView_titleEvent);
        TextView tvPlace = (TextView)convertView.findViewById(R.id.textView_placeEvent);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView_cancelButton);

        tvTitle.setText(event.getTitle());
        tvDay.setText(event.getDay()+"");
        tvPlace.setText("à " +event.getPlace());
        switch (event.getMonth()){
            case 0:
                tvMonth.setText("Janv.");
                break;
            case 1:
                tvMonth.setText("Fevr.");
                break;
            case 2:
                tvMonth.setText("Mars");
                break;
            case 3:
                tvMonth.setText("Avril");
                break;
            case 4:
                tvMonth.setText("Mai");
                break;
            case 5:
                tvMonth.setText("Juin");
                break;
            case 6:
                tvMonth.setText("Juil");
                break;
            case 7:
                tvMonth.setText("Août");
                break;
            case 8:
                tvMonth.setText("Sept.");
                break;
            case 9:
                tvMonth.setText("Octob.");
                break;
            case 10:
                tvMonth.setText("Nov.");
                break;
            case 11:
                tvMonth.setText("Dec.");
                break;
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                event.removeFromFirebase(mDatabase);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });

        return convertView;
    }
}