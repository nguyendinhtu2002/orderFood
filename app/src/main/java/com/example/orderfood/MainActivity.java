package com.example.orderfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.orderfood.Common.Common;
import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Model.Category;
import com.example.orderfood.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnSignIn, btnSignUp;
    MyDataBase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        myDatabase = new MyDataBase(this);
        getAllCategory();
        if (!hasUsersInSQLite()) {
            callApi();
        }
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
    private boolean hasUsersInSQLite() {
        List<User> userList = myDatabase.getAllUsers();
        return !userList.isEmpty();
    }
    private void callApi() {
        String url = "http://10.0.2.2:8000/api/user/getall";

        // Khởi tạo RequestQueue bằng Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Tạo request JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String email = jsonObject.getString("email");
                                String name = jsonObject.getString("name");
                                String password = jsonObject.getString("password");
                                String phone = jsonObject.getString("phone");

                                User user = new User(name, password, phone, email);
                                // Thêm người dùng vào SQLite
                                myDatabase.addUser(user);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi nếu có
                        error.printStackTrace();
                    }
                });

        // Thêm request vào hàng đợi yêu cầu
        requestQueue.add(jsonArrayRequest);
    }
    private void getAllCategory() {
        String url = "http://10.0.2.2:8000/api/category/getall";

        // Tạo request JSON
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Succse","Thanh cong");
                        // Xử lý khi nhận được phản hồi từ server
                        handleCategoryResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi xảy ra trong quá trình gửi request
//                        Toast.makeText(this, "Failed to get categories from server: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Thêm request vào hàng đợi yêu cầu
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private void handleCategoryResponse(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject categoryObject = response.getJSONObject(i);
                String categoryName = categoryObject.getString("name");
                String categoryImage = categoryObject.getString("image");
                int categoryiD = categoryObject.getInt("id");

                Category category = new Category(categoryName,categoryImage,categoryiD);

                if(!myDatabase.isCategoryExists(category)){
                    myDatabase.addCategory(category);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}