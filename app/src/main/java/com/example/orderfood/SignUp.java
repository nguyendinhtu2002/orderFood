package com.example.orderfood;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    private EditText edtPhone, edtName, edtPassword,edtEmail;
    private Button btnSignUp;
    MyDataBase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        myDatabase = new MyDataBase(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);

        btnSignUp = findViewById(R.id.btnSignUp);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString();
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String email = edtEmail.getText().toString();

                if (phone.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUp.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {

                        // Thêm người dùng mới vào SQLite
                        User user = new User(name, password,phone,email);
                        myDatabase.addUser(user);
                        Toast.makeText(SignUp.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

            }
        });
    }
}
