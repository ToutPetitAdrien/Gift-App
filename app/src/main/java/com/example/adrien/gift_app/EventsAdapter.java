package com.example.adrien.gift_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Adrien on 01/01/2017.
 */

public class EventsAdapter extends ArrayAdapter<Event> {
    public EventsAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Event event = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }
        TextView tvTitle = (TextView)convertView.findViewById(R.id.textView_Title);
        TextView tvDate = (TextView)convertView.findViewById(R.id.textView_Date);
        TextView tvPlace = (TextView)convertView.findViewById(R.id.textView_Place);

        tvTitle.setText(event.getTitle());
        tvDate.setText(event.getDate().toString());
        tvPlace.setText(event.getPlace());

        return convertView;
    }
}
