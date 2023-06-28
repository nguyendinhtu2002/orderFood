package com.example.orderfood.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Interface.ItemClickListener;
import com.example.orderfood.Model.HistoryOrder;
import com.example.orderfood.Model.Order;
import com.example.orderfood.Model.User;
import com.example.orderfood.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<HistoryOrder> ordersList;
    private Context context;
    private ItemClickListener itemClickListener;

    public OrderAdapter(Context context, List<HistoryOrder> orderList) {
        this.context = context;
        this.ordersList = orderList;

    }

    public void setOrdersList(List<HistoryOrder> ordersList) {
        this.ordersList = ordersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        final HistoryOrder order = ordersList.get(position);
        MyDataBase myDataBase = new MyDataBase(context);
        User user = myDataBase.getUserByPhone(order.getUserPhone());
        Log.d("Name",user.getName());
        holder.txtOrderId.setText(order.getOrderId());
        holder.txtOrderName.setText(user.getName());
        holder.txtOrderAddress.setText(order.getDeliveryAddress());
        holder.txtOrderPhone.setText(order.getUserPhone());
        holder.txtOrderPrice.setText(order.getPrice());



    }


    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtOrderId, txtOrderName, txtOrderAddress, txtOrderPhone, txtOrderPrice, txtOrderStatus;
        public ImageButton cart_item_deleteButton;
        private ItemClickListener itemClickListener;

        public OrderViewHolder(View itemView) {
            super(itemView);

            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderName = itemView.findViewById(R.id.txtOrderName);
            txtOrderPhone = itemView.findViewById(R.id.txtOrderPhone);
            txtOrderAddress = itemView.findViewById(R.id.txtOrderAddress);
            txtOrderPrice = itemView.findViewById(R.id.txtOrderPrice);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            cart_item_deleteButton = itemView.findViewById(R.id.cart_item_deleteButton);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                itemClickListener.onclick(view, position, false);
            }
        }
    }

}
