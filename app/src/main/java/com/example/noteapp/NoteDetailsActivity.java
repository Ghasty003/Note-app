package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText titleEditText, textEditText;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.title);
        textEditText = findViewById(R.id.text);
        imageButton = findViewById(R.id.save_btn);

        imageButton.setOnClickListener(v -> saveNote());
    }

    public void saveNote() {
        String title = titleEditText.getText().toString();
        String text = textEditText.getText().toString();

        if (title == null || title.isEmpty()) {
            titleEditText.setError("Title is required");
            return;
        }

        Note note = new Note();

        note.setText(text);
        note.setTitle(title);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference = Utility.getCollectionRefrenceForNotes().document();

        documentReference.set(note).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Utility.showToast(NoteDetailsActivity.this, task.getException().getLocalizedMessage());
                return;
            }

            Utility.showToast(NoteDetailsActivity.this, "Note added successfully.");
            finish();
        });

    }
}