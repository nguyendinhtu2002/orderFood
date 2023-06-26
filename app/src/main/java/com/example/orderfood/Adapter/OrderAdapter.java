package com.example.orderfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.Interface.ItemClickListener;
import com.example.orderfood.Model.HistoryOrder;
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

        holder.txtOrderId.setText(order.getOrderId());
        holder.txtOrderName.setText(order.getUserPhone());
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

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOrderId, txtOrderName, txtOrderAddress, txtOrderPhone, txtOrderPrice, txtOrderStatus;

        public OrderViewHolder(View itemView) {
            super(itemView);

            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderName = itemView.findViewById(R.id.txtOrderName);
            txtOrderPhone = itemView.findViewById(R.id.txtOrderPhone);
            txtOrderAddress = itemView.findViewById(R.id.txtOrderAddress);
            txtOrderPrice = itemView.findViewById(R.id.txtOrderPrice);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
        }
    }
}
