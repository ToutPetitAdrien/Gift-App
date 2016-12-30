package com.example.adrien.gift_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IdeasFormFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ideas_form_fragment, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final EditText textTitle = (EditText)view.findViewById(R.id.editText_title);
        final EditText textRecipient = (EditText)view.findViewById(R.id.editText_recipient);
        final EditText textDescription = (EditText)view.findViewById(R.id.editText_description);
        final EditText textUrl = (EditText)view.findViewById(R.id.editText_url);
        final EditText textPhoto = (EditText)view.findViewById(R.id.editText_photo);
        final EditText textPrice = (EditText)view.findViewById(R.id.editText_price);
        Button submit_button = (Button)view.findViewById(R.id.submit_button);
        // Inflate the layout for this fragment

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Idea newIdea = new Idea();
                newIdea.setTitle(textTitle.getText().toString());
                newIdea.setRecipient(textRecipient.getText().toString());
                newIdea.setDescription(textDescription.getText().toString());
                newIdea.setUrl(textUrl.getText().toString());
                newIdea.setPhoto(textPhoto.getText().toString());
                newIdea.setPrice((Integer)textPrice.getText().toString());
                newIdea.addToFirebase(user.getUid(), mDatabase);
            }
        });
        return view;
    }


}
