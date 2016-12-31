package com.example.adrien.gift_app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EventsFragment extends Fragment {

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final Button create_event = (Button)view.findViewById(R.id.button_create_event);

        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction  = fm.beginTransaction();
                Fragment addEventFragment = new EventsFormFragment();
                fragmentTransaction.replace(R.id.id_fragment_addEvents, addEventFragment);
                fragmentTransaction.commit();

                create_event.setVisibility(View.GONE);
            }

        });
    }
}