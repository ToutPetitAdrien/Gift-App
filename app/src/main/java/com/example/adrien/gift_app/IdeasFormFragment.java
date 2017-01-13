package com.example.adrien.gift_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IdeasFormFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private EventsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ideas_form_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final EditText textTitle = (EditText)view.findViewById(R.id.editText_title);
        final EditText textRecipient = (EditText)view.findViewById(R.id.editText_recipient);
        final EditText textDescription = (EditText)view.findViewById(R.id.editText_description);
        final EditText textUrl = (EditText)view.findViewById(R.id.editText_url);
        final EditText textPhoto = (EditText)view.findViewById(R.id.editText_photo);
        final EditText textPrice = (EditText)view.findViewById(R.id.editText_price);
        final AutoCompleteTextView textForWhen = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteText_forwhen);
        Button submit_button = (Button)view.findViewById(R.id.submit_button);

        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this.getContext(),android.R.layout.simple_list_item_1);

        textForWhen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mDatabase.child("events").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){
                            if(user.getUid().equals(suggestionSnapshot.getValue(Event.class).getCreatedBy())){
                                String suggestion = suggestionSnapshot.getValue(Event.class).getTitle();
                                autoComplete.add(suggestion);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                textForWhen.setAdapter(autoComplete);
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Idea newIdea = new Idea();
                newIdea.setTitle(textTitle.getText().toString());
                newIdea.setRecipient(textRecipient.getText().toString());
                newIdea.setDescription(textDescription.getText().toString());
                newIdea.setUrl(textUrl.getText().toString());
                newIdea.setPhoto(textPhoto.getText().toString());
                newIdea.setPrice(textPrice.getText().toString());
                newIdea.setForWhen(textForWhen.getText().toString());
                newIdea.addToFirebase(user.getUid(), mDatabase);
            }
        });
    }
}
