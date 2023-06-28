package com.example.orderfood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Home;
import com.example.orderfood.Interface.ItemClickListener;
import com.example.orderfood.Model.Order;
import com.example.orderfood.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_cart_name, txt_price;
    public ImageView img_cart_count;

    private ItemClickListener itemClickListener;
    private TextView txtTotalPrice;

    public void setTxt_cart_name(TextView txt_cart_name){
        this.txt_cart_name = txt_cart_name;
    }

//    public void setTxtTotalPrice(TextView txtTotalPrice) {
//        this.txtTotalPrice = txtTotalPrice;
//    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_price = itemView.findViewById(R.id.cart_item_price);
        img_cart_count = itemView.findViewById(R.id.cart_item_count);

        ImageButton btnDelete = itemView.findViewById(R.id.cart_item_deleteButton);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.onclick(v, getAdapterPosition(), false);
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;
    private ItemClickListener itemClickListener;
    private TextView txtTotalPrice;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);

        // Ánh xạ TextView txtTotalPrice từ View cha vào biến trong CartAdapter
        txtTotalPrice = parent.findViewById(R.id.txtOrderPrice);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("" +listData.get(position).getQuantity(), Color.rgb(173,216,230));
        holder.img_cart_count.setImageDrawable(drawable);
        Order currentOrder = listData.get(position);
        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));

        holder.txt_price.setText(fmt.format(price));

        holder.txt_cart_name.setText(listData.get(position).getProductName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onclick(View v, int position, boolean isLongClick) {

                if (itemClickListener != null) {
                    itemClickListener.onclick(v, position, false);
                }
                // Handle the delete button click here
                // Remove the item from the list and notify the adapter

                listData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listData.size());

//
//                int total = 0;
//                for (Order order : listData) {
//                    total += Integer.parseInt(order.getPrice()) * Integer.parseInt(order.getQuantity());
//                }
//
//                // Update total price in the UI
//                Locale locale = new Locale("vi", "VN");
//                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//                txtTotalPrice.setText(fmt.format(total));

                MyDataBase dataBase = new MyDataBase(context);
                dataBase.deleteItemOnOrder(currentOrder.getId());

                Context context = v.getContext();
                Intent intent = new Intent(context, Home.class);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setTxtTotalPrice(TextView txtTotalPrice) {
        this.txtTotalPrice = txtTotalPrice;

    }
}
