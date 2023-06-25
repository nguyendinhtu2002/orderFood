package com.example.orderfood.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.Model.Request;
import com.example.orderfood.R;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Request> ordersList;
    private Context context;

    public OrderAdapter(Context context) {
        this.context = context;
        this.ordersList = new ArrayList<>();
    }

    public void setOrdersList(List<Request> ordersList) {
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
        Request model = ordersList.get(position);

        holder.txtOrderName.setText(model.getName());
        holder.txtOrderAddress.setText(model.getAddress());
        holder.txtOrderPhone.setText(model.getPhone());
        holder.txtOrderPrice.setText(model.getTotal());
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOrderId, txtOrderName, txtOrderAddress, txtOrderPhone, txtOrderPrice;

        public OrderViewHolder(View itemView) {
            super(itemView);

            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderName = itemView.findViewById(R.id.txtOrderName);
            txtOrderAddress = itemView.findViewById(R.id.txtOrderAddress);
            txtOrderPhone = itemView.findViewById(R.id.txtOrderPhone);
            txtOrderPrice = itemView.findViewById(R.id.txtOrderPrice);
        }
    }
}
