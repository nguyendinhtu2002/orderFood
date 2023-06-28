package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.orderfood.Adapter.FoodAdapter;
import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Interface.ItemClickListener;
import com.example.orderfood.Model.Food;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;import com.android.volley.Request;

public class FoodList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    MyDataBase foodDatabase;
    List<Food> foodList;
    FoodAdapter adapter;

    String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        // Khởi tạo đối tượng FoodDatabase
        foodDatabase = new MyDataBase(this);

        // Ánh xạ các view
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Lấy categoryId từ Intent
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
            if (categoryId != null) {

                loadFoodListFromServer(categoryId);

            } else {
                Toast.makeText(this, "CategoryId is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Khong co gi", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadFoodListFromServer(String categoryId) {
        String url = "http://10.0.2.2:8000/api/food/getall";

        // Tạo request JSON
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Xử lý khi nhận được phản hồi từ server
                        handleFoodResponse(response,categoryId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý khi có lỗi xảy ra trong quá trình gửi request
                        Toast.makeText(FoodList.this, "Failed to get food list from server: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Thêm request vào hàng đợi yêu cầu
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void handleFoodResponse(JSONArray response,String categoryId) {
        List<Food> serverFoodList = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject foodObject = response.getJSONObject(i);
                int foodId = foodObject.getInt("id");
                String foodName = foodObject.getString("name");
                String foodImage = foodObject.getString("image");
                String foodDescription = foodObject.getString("description");
                String foodPrice = foodObject.getString("price");
                String foodDiscount = foodObject.getString("discount");
                String foodMenuId = foodObject.getString("menu_id");

                Food food = new Food(foodId, foodName, foodImage, foodDescription, foodPrice, foodDiscount, foodMenuId);
                serverFoodList.add(food);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (Food serverFood : serverFoodList) {
            if (!foodDatabase.isFoodExists(serverFood)) {
                foodDatabase.addFood(serverFood);
            }
        }

        foodList = foodDatabase.getAllFoodsByCategoryId(categoryId);
        Log.d("CO loi gi k",String.valueOf(foodList.size()));
        adapter = new FoodAdapter(FoodList.this, foodList);
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onclick(View view, int position, boolean isLongClick) {
                // Lấy Food từ danh sách foodList dựa trên vị trí (position)
                Food food = foodList.get(position);
                Log.d("FoodList", "Clicked Food: " + food.getName() + ", Id: " + food.getId());

                // Truyền foodId vào Intent và chuyển sang FoodDetail
                Intent foodDetailIntent = new Intent(FoodList.this, FoodDetail.class);
                foodDetailIntent.putExtra("FoodId", String.valueOf(food.getId()));
                startActivity(foodDetailIntent);
            }
        });
        recyclerView.setAdapter(adapter);
    }




}