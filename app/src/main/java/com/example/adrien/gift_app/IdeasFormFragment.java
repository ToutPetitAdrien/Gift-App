package com.example.adrien.gift_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.ByteArrayOutputStream;
import java.io.File;


public class IdeasFormFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private ImageView imagePhoto;
    private String imageForFirebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ideas_form_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Matrix matrix = new Matrix();
            matrix.postRotate(0);
            Bitmap imageBitmapFinal = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
            imageForFirebase = encodeBitmapAndSaveToFirebase(imageBitmapFinal);
            Bitmap bmpForPreview;

            if(imageBitmapFinal.getWidth() >= imageBitmapFinal.getHeight()){
                bmpForPreview = Bitmap.createBitmap(imageBitmapFinal, imageBitmapFinal.getWidth()/2 - imageBitmapFinal.getHeight()/2, 0, imageBitmapFinal.getHeight(), imageBitmapFinal.getHeight());
            } else {
                bmpForPreview = Bitmap.createBitmap(imageBitmapFinal, 0, imageBitmapFinal.getHeight()/2 - imageBitmapFinal.getWidth()/2, imageBitmapFinal.getWidth(), imageBitmapFinal.getWidth());
            }
            imagePhoto.setImageBitmap(bmpForPreview);

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final EditText textTitle = (EditText)view.findViewById(R.id.form_title);
        final EditText textRecipient = (EditText)view.findViewById(R.id.form_who);
        final EditText textUrl = (EditText)view.findViewById(R.id.form_url);
        final EditText textPhoto = (EditText)view.findViewById(R.id.form_photo);
        final EditText textPrice = (EditText)view.findViewById(R.id.form_price);
        final AutoCompleteTextView textForWhen = (AutoCompleteTextView)view.findViewById(R.id.form_when);
        final TextView submit_button = (TextView) view.findViewById(R.id.addbutton2_text);
        imagePhoto = (ImageView)view.findViewById(R.id.form_photo_image);

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
                newIdea.setPhoto(imageForFirebase);
                newIdea.setPrice(Integer.parseInt(textPrice.getText().toString()));
                newIdea.setForWhen(textForWhen.getText().toString());
                newIdea.addToFirebase(user.getUid(), mDatabase);
                Toast.makeText(getActivity(),"Idées ajoutée", Toast.LENGTH_SHORT).show();
            }
        });

        textPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    public String encodeBitmapAndSaveToFirebase(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
        return imageEncoded;
    }
}
