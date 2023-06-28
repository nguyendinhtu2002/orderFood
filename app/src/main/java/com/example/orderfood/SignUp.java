package com.example.orderfood;

import android.app.AlertDialog;
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
import com.example.orderfood.Interface.MyAPIInterface;
import com.example.orderfood.Model.User;

import java.util.List;

import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;

import retrofit2.Retrofit;

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
//                    User user = new User(name, password, phone, email);
//                    myDatabase.addUser(user);
//                    Toast.makeText(SignUp.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();

                    // Call the API to get all users
                    getAllUsersFromAPI();

                    finish();
                }
            }
        });
    }
    private void getAllUsersFromAPI() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Đang tải danh sách người dùng...");
        builder.setCancelable(false);
//        AlertDialog progressDialog = builder.create();
//        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8000/test/")  // Thay thế URL của API thật
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();

        MyAPIInterface apiInterface = retrofit.create(MyAPIInterface.class);  // Thay thế YourAPIInterface bằng interface thật

        Call<List<User>> call = apiInterface.getUsers();
        call.enqueue(new Callback <List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response <List<User>> response) {
//                progressDialog.dismiss(); // Tắt ProgressDialog khi nhận được phản hồi từ API

                if (response.isSuccessful()) {
                    List<User> userList = response.body();

                    for (User user : userList) {
                        // Do something with each user object
                        Log.d("User", "Name: " + user.getName() + ", Phone: " + user.getPhone());
                    }
                } else {
                    Toast.makeText(SignUp.this, "Lỗi khi lấy danh sách người dùng từ API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
//                progressDialog.dismiss(); // Tắt ProgressDialog khi gọi API thất bại
                Log.d("Lỗi kết nối API: " ,t.getMessage());
                Toast.makeText(SignUp.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
