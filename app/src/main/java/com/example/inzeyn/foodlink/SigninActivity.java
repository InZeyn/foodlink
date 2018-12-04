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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SigninActivity extends AppCompatActivity {

    EditText signinEmail, signinPassword;
    Button signinBtn;
    ProgressBar loadingBar;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signinEmail = findViewById(R.id.signinEmail);
        signinPassword = findViewById(R.id.signinPassword);
        signinBtn = findViewById(R.id.signinBtn);
        loadingBar = findViewById(R.id.signinLoading);

        loadingBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signinBtn.setVisibility(View.INVISIBLE);
                loadingBar.setVisibility(View.VISIBLE);
                final String EMAIL = signinEmail.getText().toString();
                final String PASSWORD = signinPassword.getText().toString();
                if(EMAIL.isEmpty()||PASSWORD.isEmpty()){
                    // TODO: 11/28/2018  check fields
                    Toast.makeText(signinBtn.getContext(), "Check fields", Toast.LENGTH_SHORT).show();
                    signinBtn.setVisibility(View.VISIBLE);
                    loadingBar.setVisibility(View.INVISIBLE);
                }else{
                    signinUser(EMAIL, PASSWORD, currentUser);
                }
            }
        });
    }

    private void signinUser(final String email, String password, FirebaseUser user) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            Intent myIntent = new Intent(SigninActivity.this, MenuActivity.class);
                            SigninActivity.this.startActivity(myIntent);
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SigninActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            signinBtn.setVisibility(View.VISIBLE);
                            loadingBar.setVisibility(View.INVISIBLE);
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
