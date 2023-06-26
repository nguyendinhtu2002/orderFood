package com.example.orderfood.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.Interface.ItemClickListener;
import com.example.orderfood.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId,txtOrderName,txtOrderPhone,txtOrderAddress,txtOrderPrice;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress = itemView.findViewById(R.id.txtOrderAddress);
        txtOrderId = itemView.findViewById(R.id.txtOrderId);
        txtOrderPhone = itemView.findViewById(R.id.txtOrderPhone);
        txtOrderPrice = itemView.findViewById(R.id.txtOrderPrice);
        txtOrderName = itemView.findViewById(R.id.txtOrderName);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onclick(v, getAdapterPosition(), false);
    }
}
