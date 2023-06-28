package com.example.orderfood;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.orderfood.Database.MyDataBase;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.orderfood.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    private EditText edtPhone, edtName, edtPassword, edtEmail;
    private Button btnSignUp;
    MyDataBase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);

        btnSignUp = findViewById(R.id.btnSignUp);
        myDatabase = new MyDataBase(this);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString();
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String email = edtEmail.getText().toString();

                if (phone.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUp.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!isValidPhone(phone)) {
                    Toast.makeText(SignUp.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(SignUp.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                }else if(password.length()!=8){
                    Toast.makeText(SignUp.this, "Password không hợp lệ", Toast.LENGTH_SHORT).show();
                }
                else  {
                    JSONObject userJson = new JSONObject();
                    try {
                        userJson.put("phone", phone);
                        userJson.put("name", name);
                        userJson.put("password", password);
                        userJson.put("email", email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = "http://10.0.2.2:8000/api/user/create";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, userJson,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    User user = new User(name, password, phone, email);
                                    myDatabase.addUser(user);
                                    Toast.makeText(SignUp.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();

                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(SignUp.this, "Đã xảy ra lỗi khi tạo tài khoản!", Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();
                                }
                            });

                    RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                    requestQueue.add(jsonObjectRequest);

                }
            }
        });
    }
    private boolean isValidPhone(String phone) {
        return phone.length() == 10;
    }
    private boolean isValidEmail(String email) {
        return email.endsWith("@gmail.com");
    }

}
