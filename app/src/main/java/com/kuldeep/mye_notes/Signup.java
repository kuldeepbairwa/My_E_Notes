package com.kuldeep.mye_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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

public class Signup extends AppCompatActivity {

    private EditText etEmail, etPass;
    private Button btnRegister;
    private TextView tv_login;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnRegister = findViewById(R.id.btn_register);
        etEmail = findViewById(R.id.et_login_email_signup);
        etPass = findViewById(R.id.et_login_pass_signup);
        tv_login = findViewById(R.id.tv_login);
        progressBar = findViewById(R.id.progressBar2);
        relativeLayout = findViewById(R.id.relative_register2);
        getSupportActionBar().hide();


        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Registration code here

                String email = etEmail.getText().toString().trim();
                String password = etPass.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Signup.this, "enter both email and password!", Toast.LENGTH_SHORT).show();
                } else {

                    //code for signup
                    relativeLayout.setAlpha(0.2f);
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        progressBar.setVisibility(View.GONE);
                                        relativeLayout.setAlpha(1.0f);

                                        Toast.makeText(Signup.this, "Registration Successfull!", Toast.LENGTH_SHORT).show();
                                        sendEmailVerification();
                                        FirebaseUser user = mAuth.getCurrentUser();


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        progressBar.setVisibility(View.GONE);
                                        relativeLayout.setAlpha(1.0f);
                                        Toast.makeText(Signup.this, "Authentication failed! " + task.getException(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Signup.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    //email verification
    private void sendEmailVerification() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Signup.this, "Verification email sent\nverify account and login again!", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    finish();

                    startActivity(new Intent(Signup.this, MainActivity.class));
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "failed to send verification email", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {

        AlertDialog dialog =  new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Verfivation email!")
                .setMessage("If you can't see verification email then check it spams folder!")

                .setNeutralButton("Ok i understand!", null)
                .show();
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.WHITE);
        super.onStart();
    }
}