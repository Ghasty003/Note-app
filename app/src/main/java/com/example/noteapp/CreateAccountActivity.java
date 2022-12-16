package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button button;
    private ProgressBar progressBar;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        button = findViewById(R.id.btn);
        progressBar = findViewById(R.id.progress_bar);
        login = findViewById(R.id.login);

        button.setOnClickListener( view -> createAccount());
        login.setOnClickListener(view -> finish());
    }

    public void createAccount() {
        String emailText = emailEditText.getText().toString();
        String passwordText = passwordEditText.getText().toString();
        String confirmPass = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(emailText, passwordText, confirmPass);

        if (!isValidated) {
            return;
        } else {
            createAccountInFirebase(emailText, passwordText);
        }
    }

    public void createAccountInFirebase(String email, String password) {
        toggleProgressBarVisibility(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                toggleProgressBarVisibility(false);
                if (task.isSuccessful()) {
                    Toast.makeText(CreateAccountActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                } else {
                    Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    public boolean validateData(String email, String password, String confirmPassword) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password is too short");
            return false;
        }

        if (!confirmPassword.equals(password)) {
            confirmPasswordEditText.setError("Password do not match");
            return false;
        }

        return true;
    }
}