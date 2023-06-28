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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.orderfood.Adapter.OrderAdapter;
import com.example.orderfood.Common.Common;
import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Model.HistoryOrder;
import com.example.orderfood.Model.Order;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderStatus extends AppCompatActivity {


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    Button btnHomeBack;
    MyDataBase myDatabase;

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
        myDatabase = new MyDataBase(this);
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



    private void loadOrders() {
        List<HistoryOrder> orders = orderDatabase.getAllHistoryOrdersByPhone(Common.currentUser.getPhone());
        adapter = new OrderAdapter(this, orders);
        recyclerView.setAdapter(adapter);
    }
}