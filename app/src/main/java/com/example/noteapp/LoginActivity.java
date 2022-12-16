package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button button;
    private ProgressBar progressBar;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        button = findViewById(R.id.btn);
        progressBar = findViewById(R.id.progress_bar);
        register = findViewById(R.id.register);

        button.setOnClickListener(view -> signIn());
        register.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }

    public void signIn() {
        String emailText = emailEditText.getText().toString();
        String passwordText = passwordEditText.getText().toString();

        boolean isValidated = validateData(emailText, passwordText);

        if (!isValidated) {
            return;
        } else {
            signInInFirebase(emailText, passwordText);
        }
    }

    public void signInInFirebase(String email, String password) {
        toggleProgressBarVisibility(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                   if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                       Utility.showToast(LoginActivity.this, "Success!");
                       startActivity(new Intent(LoginActivity.this, MainActivity.class));
                       finish();
                   } else {
                       Utility.showToast(LoginActivity.this, "Please verify email via the email verification message");
                       toggleProgressBarVisibility(false);
                   }
                } else {
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    public void toggleProgressBarVisibility(boolean bool) {
        if (bool) {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
    }

    public boolean validateData(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password is too short");
            return false;
        }

        return true;
    }
}