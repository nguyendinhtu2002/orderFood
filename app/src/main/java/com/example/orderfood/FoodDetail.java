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

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.orderfood.Database.Database;
import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Model.Food;
import com.example.orderfood.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

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

            new Database(getBaseContext()).addToCart(new Order(
                    foodId,
                    currentFood.getName(),
                    quantity,
                    currentFood.getPrice(),
                    currentFood.getDiscount()
            ));

            Toast.makeText(FoodDetail.this, "Thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
