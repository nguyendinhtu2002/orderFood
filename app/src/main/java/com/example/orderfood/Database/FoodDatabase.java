package com.example.orderfood.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.orderfood.Model.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "FoodAppDB1.db";
    private static final int DB_VER = 1;
    private static final String TABLE_NAME = "Foods";
    private static final String COL_ID = "Id";
    private static final String COL_NAME = "Name";
    private static final String COL_IMAGE = "Image";
    private static final String COL_DESCRIPTION = "Description";
    private static final String COL_PRICE = "Price";
    private static final String COL_DISCOUNT = "Discount";
    private static final String COL_MENU_ID = "MenuId";

    public FoodDatabase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_IMAGE + " TEXT," +
                COL_DESCRIPTION + " TEXT," +
                COL_PRICE + " TEXT," +
                COL_DISCOUNT + " TEXT," +
                COL_MENU_ID + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertFood(Food food) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, food.getName());
        values.put(COL_IMAGE, food.getImage());
        values.put(COL_DESCRIPTION, food.getDescription());
        values.put(COL_PRICE, food.getPrice());
        values.put(COL_DISCOUNT, food.getDiscount());
        values.put(COL_MENU_ID, food.getMenuId());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Food> getAllFoods() {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
                String image = cursor.getString(cursor.getColumnIndex(COL_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndex(COL_PRICE));
                String discount = cursor.getString(cursor.getColumnIndex(COL_DISCOUNT));
                String menuId = cursor.getString(cursor.getColumnIndex(COL_MENU_ID));
                Food food = new Food(name, image, description, price, discount, menuId);
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodList;
    }
    public Food getFoodById(String foodId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COL_ID, COL_NAME, COL_IMAGE, COL_DESCRIPTION, COL_PRICE, COL_DISCOUNT, COL_MENU_ID};
        String selection = COL_ID + " = ?";
        String[] selectionArgs = {foodId};
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        Food food = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            String image = cursor.getString(cursor.getColumnIndex(COL_IMAGE));
            String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
            String price = cursor.getString(cursor.getColumnIndex(COL_PRICE));
            String discount = cursor.getString(cursor.getColumnIndex(COL_DISCOUNT));
            String menuId = cursor.getString(cursor.getColumnIndex(COL_MENU_ID));
            food = new Food(name, image, description, price, discount, menuId);
        }
        cursor.close();
        db.close();
        return food;
    }
    public List<Food> getAllFoodsByCategoryId(String categoryId) {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COL_ID, COL_NAME, COL_IMAGE, COL_DESCRIPTION, COL_PRICE, COL_DISCOUNT, COL_MENU_ID};
        String selection = COL_MENU_ID + " = ?";
        String[] selectionArgs = {categoryId};
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
                String image = cursor.getString(cursor.getColumnIndex(COL_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndex(COL_PRICE));
                String discount = cursor.getString(cursor.getColumnIndex(COL_DISCOUNT));
                String menuId = cursor.getString(cursor.getColumnIndex(COL_MENU_ID));
                Food food = new Food(name, image, description, price, discount, menuId);
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodList;
    }

}
