package com.kuldeep.mye_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    private EditText et_title_note,et_content_note;
    private FloatingActionButton mSaveNote,mEditNote;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private Toolbar toolbar;
    private Boolean isDataSaved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        mSaveNote = findViewById(R.id.save_note_fab);
        mEditNote = findViewById(R.id.edit_note_fab);
        et_title_note = findViewById(R.id.et_create_title_of_note);
        et_content_note = findViewById(R.id.et_create_content_note);
        toolbar = findViewById(R.id.toolbar_create_note);
        isDataSaved = false;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();



        mSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void saveData() {
        String title = et_title_note.getText().toString();
        String content = et_content_note.getText().toString();
        if (title.isEmpty() || content.isEmpty()){
            Toast.makeText(CreateNote.this, "both title and content is required!", Toast.LENGTH_SHORT).show();
        }
        else{
            DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
            Map<String,Object> note = new HashMap<>();
            note.put("title",title);
            note.put("content",content);

            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CreateNote.this, "note saved successfully", Toast.LENGTH_SHORT).show();
                    isDataSaved = true;
                    startActivity(new Intent(CreateNote.this,NotesActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateNote.this, "something went wrong notes is not saved!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){

            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),NotesActivity.class));
    }
}