package com.example.adrien.gift_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class IdeasListFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private IdeasAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ideaslist_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        SearchView sv = (SearchView)view.findViewById(R.id.id_searchview_ideas);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("IdeasFormFragment", "Je suis dans le fragment des idées");

        // Display Ideas list and sorted by title

        ArrayList<Idea> arrayOfIdeas = new ArrayList<Idea>();
        adapter = new IdeasAdapter(this.getContext(), arrayOfIdeas);
        final ListView listView = (ListView)view.findViewById(R.id.id_listview_ideas);
        listView.setAdapter(adapter);
        Query ideasSortedByDate = mDatabase.child("ideas").orderByChild("title");

        ideasSortedByDate.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Idea newIdea = dataSnapshot.getValue(Idea.class);
                newIdea.setKey(dataSnapshot.getKey());

                if(user.getUid().equals(newIdea.getCreatedBy())){
                    adapter.add(newIdea);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Get suggestions when user start writing in Searchview

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


}

