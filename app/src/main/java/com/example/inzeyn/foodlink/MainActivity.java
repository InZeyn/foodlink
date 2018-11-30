package com.example.inzeyn.foodlink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button signinAct, signupAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signinAct = findViewById(R.id.signinBtnActivity);
        signupAct = findViewById(R.id.signupBtnActivity);
    }

    @Override
    public void onClick(View v) {
        Intent signinIntent = new Intent(MainActivity.this, MenuActivity.class);
        Intent signupIntent = new Intent(MainActivity.this, SignupActivity.class);
        if (v.getId() == R.id.signinBtnActivity)
            MainActivity.this.startActivity(signinIntent);
        if (v.getId() == R.id.signupBtnActivity)
            MainActivity.this.startActivity(signupIntent);
    }
}


