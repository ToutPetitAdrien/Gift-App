package com.example.adrien.gift_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;


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
            //Rotate image because it's display horizontal
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap imageBitmapFinal = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
            imageForFirebase = encodeBitmapAndSaveToFirebase(imageBitmapFinal); //replace "empty" string in idea object
            // crop the image to be display in the square imageView
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
        Calendar calendar = Calendar.getInstance();

        final EditText textTitle = (EditText)view.findViewById(R.id.form_title);
        final EditText textRecipient = (EditText)view.findViewById(R.id.form_who);
        final EditText textUrl = (EditText)view.findViewById(R.id.form_url);
        final EditText textPrice = (EditText)view.findViewById(R.id.form_price);
        final AutoCompleteTextView textForWhen = (AutoCompleteTextView)view.findViewById(R.id.form_when);
        final TextView submit_button = (TextView) view.findViewById(R.id.addbutton2_text);
        imagePhoto = (ImageView)view.findViewById(R.id.form_photo_image);
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this.getContext(),android.R.layout.simple_list_item_1); //Array List of suggestions
        final int currentIntegerDate = calendar.get(Calendar.DAY_OF_MONTH) + 100*calendar.get(Calendar.MONTH) + 10000*calendar.get(Calendar.YEAR);

        // Suggestion frame with all the event of the user when he start typing
        textForWhen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mDatabase.child("events").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){ //browse all events of user
                            if(user.getUid().equals(suggestionSnapshot.getValue(Event.class).getCreatedBy())&& suggestionSnapshot.getValue(Event.class).getIntegerdate() > currentIntegerDate){
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

        imagePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { // open camera on click
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE); //start methode OnActivityResult
                }
            }
        });
    }

    //fonction for converting bitmap in String to storage in Firebase Realtime DB
    public String encodeBitmapAndSaveToFirebase(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
        return imageEncoded;
    }
}
