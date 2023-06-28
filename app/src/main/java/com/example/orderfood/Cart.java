package com.example.orderfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.JsonObjectRequest;
import com.example.orderfood.Adapter.CartAdapter;

import com.example.orderfood.Common.Common;
import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyDataBase myDatabase;


    TextView txtTotalPrice;
    Button btnPlace, btnCartBack;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        txtTotalPrice = findViewById(R.id.total);
        btnCartBack = findViewById(R.id.btnCartBack);
        btnCartBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cart.this, Home.class);
                startActivity(i);
            }
        });
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        loadListFood();
    }


    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Bước cuối cùng!");
        alertDialog.setMessage("Nhập địa chỉ giao hàng:");

        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String address = edtAddress.getText().toString();
                String totalPrice = txtTotalPrice.getText().toString();

                // Create a JSON object to hold the order data
                JSONObject orderObject = new JSONObject();
                try {
                    orderObject.put("user_phone", Common.currentUser.getPhone());
                    orderObject.put("delivery_address", address);
                    orderObject.put("price", totalPrice);
                    orderObject.put("status", "Giao hàng thành công");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = "http://10.0.2.2:8000/api/history/create";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, orderObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                new MyDataBase(getBaseContext()).cleanCart();
                                Toast.makeText(Cart.this, "Đặt hàng thành công", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Cart.this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                // Add the request to the request queue
                Volley.newRequestQueue(Cart.this).add(jsonObjectRequest);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    private void loadListFood() {
        cart = new MyDataBase(this).getCarts(Common.currentUser.getPhone());
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);

        // Tạo đối tượng CartAdapter và gán giá trị cho txtTotalPrice
        CartAdapter adapter = new CartAdapter(cart, this);
        adapter.setTxtTotalPrice(txtTotalPrice);

        //calc total
        int total = 0;
        for(Order order:cart){
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }


}