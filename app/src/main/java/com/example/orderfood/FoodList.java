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
import com.example.orderfood.Model.Category;
import com.example.orderfood.Model.Food;
import com.example.orderfood.Model.User;
import com.example.orderfood.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
                categoryId = "0" + categoryId;
                Log.d("CategoryId", categoryId);
                loadFoodListFromSQLite(categoryId);
            } else {
                Toast.makeText(this, "CategoryId is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Khong co gi", Toast.LENGTH_SHORT).show();
        }

    }
//    @Override
//    public void onclick(View v, int position, boolean isLongClick) {
//        // Get the clicked category
//
//        Category clickedCategory = menuList.get(position);
//        // Pass the category to the food detail activity
//        Intent intent = new Intent(Home.this, FoodList.class);
//        intent.putExtra("CategoryId", String.valueOf(clickedCategory.getCategoryId()));
//        startActivity(intent);
//    }

    private void loadFoodListFromSQLite(String categoryId) {
        foodList = foodDatabase.getAllFoodsByCategoryId(categoryId);

        // Hiển thị danh sách món ăn trong RecyclerView
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

    private void loadFoodListFromFirebase(String categoryId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference foodListRef = database.getReference("Foods");

        // Lắng nghe sự thay đổi dữ liệu trên Firebase
        foodListRef.orderByChild("MenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    foodList.add(food);
                }

                // Hiển thị danh sách món ăn trong RecyclerView
                adapter = new FoodAdapter(FoodList.this, foodList);
                adapter.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        // Lấy Food từ danh sách foodList dựa trên vị trí (position)
                        Food food = foodList.get(position + 1);
                        Log.d("FoodList", "Clicked Food: " + food.getName() + ", Id: " + food.getId());

                        // Truyền foodId vào Intent và chuyển sang FoodDetail
                        Intent foodDetailIntent = new Intent(FoodList.this, FoodDetail.class);
                        foodDetailIntent.putExtra("FoodId", String.valueOf(food.getId()));
                        startActivity(foodDetailIntent);
                    }
                });
                recyclerView.setAdapter(adapter);

                // Insert danh sách món ăn vào SQLite
                for (Food food : foodList) {
                    foodDatabase.insertFood(food);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}