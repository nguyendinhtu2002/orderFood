package com.example.orderfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.orderfood.Adapter.FoodAdapter;
import com.example.orderfood.Adapter.OrderAdapter;
import com.example.orderfood.Common.Common;
import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Interface.ItemClickListener;
import com.example.orderfood.Model.Food;
import com.example.orderfood.Model.HistoryOrder;
import com.example.orderfood.Model.Order;
import com.example.orderfood.Model.Request;
import com.example.orderfood.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class OrderStatus extends AppCompatActivity {


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    Button btnHomeBack;

    MyDataBase orderDatabase=new MyDataBase(this);
    List<Order> orderList = new ArrayList<>();
    OrderAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);


        recyclerView = findViewById(R.id.ListOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();

        btnHomeBack = findViewById(R.id.btnHomeBack);
        btnHomeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodBack = new Intent(OrderStatus.this, Home.class);
                startActivity(foodBack);
            }
        });
    }

//    private void loadOrders(String phone) {
//        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class,R.layout.order_layout,
//                OrderViewHolder.class,
//                requests.orderByChild("phone").equalTo(phone)) {
//            @Override
//            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
//
//                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
//                viewHolder.txtOrderName.setText(model.getName());
//                viewHolder.txtOrderAddress.setText(model.getAddress());
//                viewHolder.txtOrderPhone.setText(model.getPhone());
//                viewHolder.txtOrderPrice.setText(model.getTotal());
//            }
//        };
//        recyclerView.setAdapter(adapter);
//    }

    private void loadOrders() {
        List<HistoryOrder> orders = orderDatabase.getAllHistoryOrdersByPhone("123456");
        adapter = new OrderAdapter(this, orders);  // Pass the Context as the first argument
        recyclerView.setAdapter(adapter);
    }
}