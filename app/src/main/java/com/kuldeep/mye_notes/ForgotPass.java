package com.kuldeep.mye_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {

    private EditText etForgotEmail;
    private Button mPasswordRecoverBtn;
    private TextView mBacktoLogin;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        getSupportActionBar().hide();
        etForgotEmail = findViewById(R.id.et_reset_email);
        mPasswordRecoverBtn = findViewById(R.id.btn_reset_pass);
        mBacktoLogin = findViewById(R.id.tv_back_to_login);
        progressBar = findViewById(R.id.progressBar3);
        relativeLayout = findViewById(R.id.relative_forgot2);

        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();

        mBacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        mPasswordRecoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etForgotEmail.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(ForgotPass.this, "email is required", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Here comes reset password code

                    progressBar.setVisibility(View.VISIBLE);
                    relativeLayout.setAlpha(0.1f);

                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()){
                              progressBar.setVisibility(View.GONE);
                              relativeLayout.setAlpha(1.0f);
                              Toast.makeText(ForgotPass.this, "password reset link sent to registered email!", Toast.LENGTH_SHORT).show();
                          }
                          else{
                              progressBar.setVisibility(View.GONE);
                              relativeLayout.setAlpha(1.0f);
                              Toast.makeText(ForgotPass.this, "didn't found any account is registered with this email", Toast.LENGTH_SHORT).show();
                          }
                        }
                    });
                }

            }
        });


    }
}