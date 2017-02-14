package com.example.adrien.gift_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class IdeasFormFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ideas_form_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        StorageReference storageRef = storage.getReferenceFromUrl("gs://gift-app-fd6d4.appspot.com");

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            progressDialog.setMessage("Uploading ...");
            progressDialog.show();
            Uri uri = data.getData();
            StorageReference filepath = storageRef.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Upload Done.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        storage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final EditText textTitle = (EditText)view.findViewById(R.id.form_title);
        final EditText textRecipient = (EditText)view.findViewById(R.id.form_who);
        final EditText textUrl = (EditText)view.findViewById(R.id.form_url);
        final EditText textPhoto = (EditText)view.findViewById(R.id.form_photo);
        final EditText textPrice = (EditText)view.findViewById(R.id.form_price);
        final AutoCompleteTextView textForWhen = (AutoCompleteTextView)view.findViewById(R.id.form_when);
        final TextView submit_button = (TextView) view.findViewById(R.id.addbutton2_text);

        progressDialog = new ProgressDialog(getContext());



        // Suggestion event on textForWhen field

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

        // add Idea to firebase

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Idea newIdea = new Idea();
                newIdea.setTitle(textTitle.getText().toString());
                newIdea.setRecipient(textRecipient.getText().toString());
                newIdea.setUrl(textUrl.getText().toString());
                newIdea.setPhoto(textPhoto.getText().toString());
                newIdea.setPrice(Integer.parseInt(textPrice.getText().toString()));
                newIdea.setForWhen(textForWhen.getText().toString());
                newIdea.addToFirebase(user.getUid(), mDatabase);
                Toast.makeText(getActivity(),"Idées ajoutée", Toast.LENGTH_SHORT).show();
            }
        });

        textPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });


    }
}
