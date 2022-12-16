package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

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
    }
}