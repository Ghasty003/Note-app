package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText titleEditText, textEditText;
    private ImageButton imageButton;
    TextView textView;
    String title, content, docId;
    boolean isEditMode = false;

    ImageButton deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.title);
        textEditText = findViewById(R.id.text);
        imageButton = findViewById(R.id.save_btn);
        textView = findViewById(R.id.header_title);
        deleteBtn = findViewById(R.id.deleteNote);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        titleEditText.setText(title);
        textEditText.setText(content);

        if (isEditMode) {
            textView.setText("Edit your note");
        }

        imageButton.setOnClickListener(v -> saveNote());

        deleteBtn.setOnClickListener(view -> {
            deleteNote();
        });
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

        DocumentReference documentReference;

        if (isEditMode) {
             documentReference = Utility.getCollectionRefrenceForNotes().document(docId);
                documentReference.set(note).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Utility.showToast(NoteDetailsActivity.this, task.getException().getLocalizedMessage());
                        return;
                    }

                    Utility.showToast(NoteDetailsActivity.this, "Note updated successfully");
                    finish();
                });
        } else {
             documentReference = Utility.getCollectionRefrenceForNotes().document();
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

    void deleteNote() {
        String documentReference = Utility.getCollectionRefrenceForNotes().document().getId();

        Utility.showToast(NoteDetailsActivity.this, documentReference);

//        documentReference.delete().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Utility.showToast(NoteDetailsActivity.this, "Unable to delete note");
//                return;
//            }
//
//            Utility.showToast(NoteDetailsActivity.this, "Note deleted successfully");
//        });
    }
}