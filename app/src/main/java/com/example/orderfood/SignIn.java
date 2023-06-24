package com.example.orderfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orderfood.Common.Common;
import com.example.orderfood.Database.UserDatabase;
import com.example.orderfood.Model.User;

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPass;
    Button btnSignIn;
    UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userDatabase = new UserDatabase(this);

        edtPass = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString().trim();
                String password = edtPass.getText().toString().trim();

                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignIn.this, "Vui lòng nhập số điện thoại và mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }


                    if (userDatabase.checkUserCredentials(phone, password)) {
                        User user = new User(phone, "", password); // You can modify this line to retrieve user information from the database if needed
                        Intent homeIntent = new Intent(SignIn.this, Home.class);
                        Common.currentUser = user;
                        startActivity(homeIntent);
                    } else {
                        Toast.makeText(SignIn.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                }

        });
    }
}
