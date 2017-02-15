package com.example.adrien.gift_app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventsFormFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final EditText textTitle = (EditText)view.findViewById(R.id.editText_title_event);
        final EditText textDate = (EditText)view.findViewById(R.id.editText_date_event);
        final EditText textPlace = (EditText)view.findViewById(R.id.editText_place_event);
        final Calendar myCalendar = Calendar.getInstance();
        final TextView add_event = (TextView) view.findViewById(R.id.addbutton_text);
        final TextView cancel_event = (TextView) view.findViewById(R.id.cancelbutton_text);
        final RelativeLayout newbutton = (RelativeLayout) getActivity().findViewById(R.id.newbutton);

        //Dialog to select a date on the Calendar UI
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                textDate.setText(sdf.format(myCalendar.getTime())); //put all parameters in the date field
            }
        };

        //Open the dialog on click
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCurrentCalendar = Calendar.getInstance();
                int currentIntegerDate = myCurrentCalendar.get(Calendar.DAY_OF_MONTH) + 100*myCurrentCalendar.get(Calendar.MONTH) + 10000*myCurrentCalendar.get(Calendar.YEAR); // integer to represent current date
                Event newEvent = new Event();
                newEvent.setTitle(textTitle.getText().toString());
                newEvent.setDay(myCalendar.get(Calendar.DAY_OF_MONTH));
                newEvent.setMonth(myCalendar.get(Calendar.MONTH));
                newEvent.setYear(myCalendar.get(Calendar.YEAR));
                newEvent.setPlace(textPlace.getText().toString());
                if(textTitle.getText().toString().matches("") || textPlace.getText().toString().matches("") || textDate.getText().toString().matches("")){ //check that any field are empty
                    Toast.makeText(getActivity(),"Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else if (newEvent.getIntegerdate() > currentIntegerDate){ //check if the new event in the future
                    Toast.makeText(getActivity(),"Vous ne pouvez pas ajouter un évènement dans le passé.", Toast.LENGTH_SHORT).show();
                } else {
                    newEvent.addToFirebase(user.getUid(), mDatabase); //add the object to firebase
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_eventsform_frame)).commit(); //destroy the form after creating a new event
                    newbutton.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),"Evènement ajouté", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Remove Events button
        cancel_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_eventsform_frame)).commit();
                newbutton.setVisibility(View.VISIBLE);
            }
        });
    }
}
