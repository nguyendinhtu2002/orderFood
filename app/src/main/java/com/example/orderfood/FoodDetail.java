package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.orderfood.Common.Common;
import com.example.orderfood.Database.Database;
import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Model.Food;
import com.example.orderfood.Model.Order;
import com.example.orderfood.Model.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button btnCart, btnFoodBack;
    ElegantNumberButton numberButton;
    Food currentFood;
    String foodId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        numberButton = findViewById(R.id.number_button);
        btnFoodBack = findViewById(R.id.btnFoodBack);
        btnFoodBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodBack = new Intent(FoodDetail.this, Home.class);
                startActivity(foodBack);
            }
        });
        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
                Log.d("FoodDetail, UserPhone: ", Common.currentUser.getPhone());
            }
        });

        food_name = findViewById(R.id.food_name);
        food_description = findViewById(R.id.food_description);
        food_price = findViewById(R.id.food_price);
        food_image = findViewById(R.id.food_image);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");

            if (foodId != null && !foodId.isEmpty()) {
                getDetailFood(foodId);
            } else {
                Toast.makeText(this,"Khong co gi",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getDetailFood(String foodId) {
        MyDataBase foodDatabase = new MyDataBase(this);

        currentFood = foodDatabase.getFoodById(foodId);


        if (currentFood != null) {
            Picasso.get().load(currentFood.getImage()).into(food_image);
            collapsingToolbarLayout.setTitle(currentFood.getName());

            food_name.setText(currentFood.getName());
            food_price.setText(currentFood.getPrice());
            food_description.setText(currentFood.getDescription());
        }
    }

    private void addToCart() {

        if (currentFood != null) {

                String quantity = numberButton.getNumber();


                new MyDataBase(getBaseContext()).addToCart(new Order(
                        Common.currentUser.getPhone(),
                        foodId,
                        currentFood.getName(),
                        quantity,
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
            createOrderOnServer(quantity);
            Toast.makeText(FoodDetail.this, "Thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void createOrderOnServer(String quantity) {
        String url = "http://10.0.2.2:8000/api/order/create";

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("user_phone", Common.currentUser.getPhone());
            requestData.put("food_id", foodId);
            requestData.put("food_name", currentFood.getName());
            requestData.put("quantity", quantity);
            requestData.put("discount", currentFood.getDiscount());
            requestData.put("Price", currentFood.getPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response
                        // Order created successfully on the server
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle API error
                        Toast.makeText(FoodDetail.this, "Failed to push order to  server: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
