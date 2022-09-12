package com.kuldeep.mye_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mLoginEmail, mLoginPass;
    private Button btnLogin, btnRegister;
    private TextView tvForgotPass;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginEmail = findViewById(R.id.et_login_email);
        mLoginPass = findViewById(R.id.et_login_pass);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        tvForgotPass = findViewById(R.id.tv_forgot_pass);
        progressBar = findViewById(R.id.progressBar);
        relativeLayout = findViewById(R.id.relative_m2);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null){
            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));
        }


            getSupportActionBar().hide();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Signup.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mLoginEmail.getText().toString().trim();
                String password = mLoginPass.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "enter both email and password!", Toast.LENGTH_SHORT).show();
                } else {

                    //code for signup
                    progressBar.setVisibility(View.VISIBLE);
                    relativeLayout.setAlpha(0.2f);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                progressBar.setVisibility(View.GONE);
                                relativeLayout.setAlpha(1.0f);
                                checkEmailVerification();

                                //login successful activity
                            } else {
                                progressBar.setVisibility(View.GONE);
                                relativeLayout.setAlpha(1.0f);
                                Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ForgotPass.class);
                startActivity(i);
            }
        });
    }

    private void checkEmailVerification() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified()) {
            Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        } else {
            Toast.makeText(MainActivity.this, "email is not verified", Toast.LENGTH_SHORT).show();
        }
    }



}