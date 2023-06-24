package com.example.orderfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btnSignIn, btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSignIn.setOnClickListener((view) -> {
            Intent SignIn = new Intent(MainActivity.this, SignIn.class);
            startActivity(SignIn);

        });

        btnSignUp.setOnClickListener((view) -> {
            Intent SignUp = new Intent(MainActivity.this, SignUp.class);
            startActivity(SignUp);

        });

    }
}