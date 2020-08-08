package com.example.finalproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.finalproject.R;
import com.example.finalproject.common.ToastNotify;
import com.example.finalproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Button login_button;
    private Button register_button;
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        inputEmail = findViewById(R.id.email_input_edit);
        inputPassword = findViewById(R.id.password_input_edit);
        final Context context = getApplicationContext();
        register_button = findViewById(R.id.reg_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    ToastNotify.showLongMsg(context, getString(R.string.empty_entries));
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(email, password);
                                        String userId = task.getResult().getUser().getUid();
                                        db.collection("users").document(userId).set(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            startIntent();
                                                        } else {
                                                            ToastNotify.showLongMsg(context, getString(R.string.error_register));
                                                        }
                                                    }
                                                });

                                    } else {
                                        ToastNotify.showLongMsg(context, getString(R.string.error_register));
                                    }
                                }
                            });
                }
            }
        });
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    ToastNotify.showLongMsg(context, getString(R.string.empty_entries));
                } else {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startIntent();
                                    } else {
                                        ToastNotify.showLongMsg(context, getString(R.string.error_sign_in));
                                    }
                                }
                            });
                }
            }
        });
    }

    private void startIntent() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

}