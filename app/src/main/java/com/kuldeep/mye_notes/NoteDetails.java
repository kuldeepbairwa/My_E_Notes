package com.kuldeep.mye_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
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

public class NoteDetails extends AppCompatActivity {



    private FloatingActionButton editNotesFab, saveEditedNoteFab;
    private Toolbar tv_Toolbar, et_Toolbar;
    private EditText etEditNoteTitle, etEditNoteContent;
    private TextView tvEditNoteTitle, tvEditNoteContent;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    private String title, content, noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        editNotesFab = findViewById(R.id.edit_note_fab);
        saveEditedNoteFab = findViewById(R.id.save_edited_note_fab);
        etEditNoteTitle = findViewById(R.id.et_edit_title_of_note);
        etEditNoteContent = findViewById(R.id.et_edit_content_note);
        tvEditNoteContent = findViewById(R.id.tv_content_of_note_detail);
        tvEditNoteTitle = findViewById(R.id.tv_title_of_note_detail);
        tv_Toolbar = findViewById(R.id.tv_toolbar_note_detail);
        et_Toolbar = findViewById(R.id.et_toolbar_note_detail);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        Intent data = getIntent();
        title = data.getStringExtra("title");
        content = data.getStringExtra("content");
        noteId = data.getStringExtra("noteId");

        tvEditNoteContent.setText(content);
        tvEditNoteTitle.setText(title);


        editNotesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_Toolbar.setVisibility(View.GONE);
                tvEditNoteTitle.setVisibility(View.GONE);
                tvEditNoteContent.setVisibility(View.GONE);
                et_Toolbar.setVisibility(View.VISIBLE);
                etEditNoteTitle.setVisibility(View.VISIBLE);
                etEditNoteContent.setVisibility(View.VISIBLE);


                etEditNoteTitle.setFocusableInTouchMode(true);
                etEditNoteTitle.requestFocus();


                etEditNoteTitle.setText(title);
                etEditNoteContent.setText(content);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etEditNoteTitle, InputMethodManager.SHOW_IMPLICIT);

            }
        });


        saveEditedNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //save edited notes
                et_Toolbar.setVisibility(View.GONE);
                etEditNoteTitle.setVisibility(View.GONE);
                etEditNoteContent.setVisibility(View.GONE);
                tv_Toolbar.setVisibility(View.VISIBLE);
                tvEditNoteTitle.setVisibility(View.VISIBLE);
                tvEditNoteContent.setVisibility(View.VISIBLE);
                String newTitle = etEditNoteTitle.getText().toString();
                String newContent = etEditNoteContent.getText().toString();

                if(newTitle.isEmpty() || newContent.isEmpty()){
                    Toast.makeText(NoteDetails.this, "no changes were made!", Toast.LENGTH_SHORT).show();
                }
                else{
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(noteId);
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",newTitle);
                    note.put("content",newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            onBackPressed();
                            Toast.makeText(NoteDetails.this, "note is updated!", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NoteDetails.this, "failed to update note!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }


                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etEditNoteTitle.getWindowToken(), 0);




            }
        });


        tvEditNoteTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_Toolbar.setVisibility(View.GONE);
                tvEditNoteTitle.setVisibility(View.GONE);
                tvEditNoteContent.setVisibility(View.GONE);
                et_Toolbar.setVisibility(View.VISIBLE);
                etEditNoteTitle.setVisibility(View.VISIBLE);
                etEditNoteContent.setVisibility(View.VISIBLE);


                etEditNoteTitle.setFocusableInTouchMode(true);
                etEditNoteTitle.requestFocus();


                etEditNoteTitle.setText(title);
                etEditNoteContent.setText(content);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etEditNoteTitle, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        tvEditNoteContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_Toolbar.setVisibility(View.GONE);
                tvEditNoteTitle.setVisibility(View.GONE);
                tvEditNoteContent.setVisibility(View.GONE);
                et_Toolbar.setVisibility(View.VISIBLE);
                etEditNoteTitle.setVisibility(View.VISIBLE);
                etEditNoteContent.setVisibility(View.VISIBLE);


                etEditNoteContent.setFocusableInTouchMode(true);
                etEditNoteContent.requestFocus();


                etEditNoteTitle.setText(title);
                etEditNoteContent.setText(content);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etEditNoteContent, InputMethodManager.SHOW_IMPLICIT);

            }
        });



    }

    private void viewNotes() {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), NotesActivity.class));
            finish();
    }
}