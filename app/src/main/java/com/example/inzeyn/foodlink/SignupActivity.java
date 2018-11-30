package com.example.inzeyn.foodlink;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.inzeyn.foodlink.Adapters.*;
import com.google.android.gms.tasks.*;

import com.google.firebase.auth.*;

import com.google.firebase.firestore.*;


public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    private static final String Tagd = "tagDebug";
    private EditText userName, name, email, password;
    private Button signupBtn;
    private ProgressBar loadingBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //views
        userName = findViewById(R.id.signupUsername);
        name = findViewById(R.id.signupName);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        signupBtn = findViewById(R.id.signupBtn);
        loadingBar = findViewById(R.id.signupLoading);
        //
        loadingBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signupBtn.setVisibility(View.INVISIBLE);
                loadingBar.setVisibility(View.VISIBLE);
                final String USERNAME = userName.getText().toString();
                final String NAME = name.getText().toString();
                final String EMAIL = email.getText().toString();
                final String PASSWORD = password.getText().toString();

                if(USERNAME.isEmpty()||NAME.isEmpty()||EMAIL.isEmpty()||PASSWORD.isEmpty()){
                    // TODO: 11/28/2018  check fields
                    Log.d(Tagd,"check fields");
                }else{
                    createNewUser(USERNAME,NAME,EMAIL,PASSWORD);
                }
            }
        });


    }

    private void createNewUser(final String username, final String name, final String email, String password) {
    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Log.i(TAG, "createNewUser: success");
                Log.d(TAG, "createUserWithEmail:success");
                Toast.makeText(SignupActivity.this, "Authentication success.",
                        Toast.LENGTH_SHORT).show();
                FirebaseUser user = mAuth.getCurrentUser();
                updateUser(username, name, email,user);
                Intent myIntent = new Intent(SignupActivity.this, MenuActivity.class);
                SignupActivity.this.startActivity(myIntent);

            }else{
                Log.i(TAG,"createNewUser fail" + task.getException());
                Toast.makeText(SignupActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                signupBtn.setVisibility(View.VISIBLE);
                loadingBar.setVisibility(View.INVISIBLE);
            }
        }
    });

    }

    private void updateUser(String username, String name, String email,FirebaseUser user) {
        UserAdapter userAdapter = new UserAdapter(username, email, name);
        mFirestore.collection("users").add(userAdapter).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });

    }
}
