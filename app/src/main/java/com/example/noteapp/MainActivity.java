package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;

    RecyclerView recyclerView;
    NoteAdapter noteAdapter;

    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.action_btn);
        recyclerView = findViewById(R.id.recycerView);
        imageButton = findViewById(R.id.menu_btn);

        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, NoteDetailsActivity.class)));

        setUpRecyclerView();

        imageButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, imageButton);
            popupMenu.getMenu().add("Logout");
            popupMenu.show();


            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getTitle().toString()) {
                        case "Logout":
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();

                            return true;
                    }

                    return false;
                }
            });
        });
    }

    void setUpRecyclerView() {
        Query query = Utility.getCollectionRefrenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query, Note.class).build();

        noteAdapter = new NoteAdapter(options, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}