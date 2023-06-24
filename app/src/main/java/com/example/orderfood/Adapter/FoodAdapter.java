package com.example.orderfood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.FoodDetail;
import com.example.orderfood.Interface.ItemClickListener;
import com.example.orderfood.Model.Food;
import com.example.orderfood.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> foodList;
    private Context context;
    private MyDataBase myDatabase;
    Food food = new Food();

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
        myDatabase = new MyDataBase(context);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        String foodName = food.getName();

        final Food food = foodList.get(position);
        Log.d("FoodAdapter position" , String.valueOf(position) );
        holder.food_name.setText(foodName);
        Picasso.get().load(food.getImage()).into(holder.food_image);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onclick(View v, int position, boolean isLongClick) {
                // Do something when item is clicked
                // For example, navigate to food detail activity
                Intent foodDetail = new Intent(context, FoodDetail.class);
                Log.d("FoodAdapter" , String.valueOf(food.getId()) );
                foodDetail.putExtra("FoodId", String.valueOf(food.getId()));
                context.startActivity(foodDetail);
            }
        });

        // Insert food into SQLite database
        myDatabase.insertFood(food);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView food_name;
        ImageView food_image;
        private ItemClickListener itemClickListener;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_name);
            food_image = itemView.findViewById(R.id.food_image);
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
}
