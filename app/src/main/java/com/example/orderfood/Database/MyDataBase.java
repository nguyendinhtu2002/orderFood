package com.example.orderfood.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.orderfood.Model.Category;
import com.example.orderfood.Model.Food;
import com.example.orderfood.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MyDataBase extends SQLiteOpenHelper {
    private static final String DB_NAME = "FoodAppDB.db";
    private static final int DB_VER = 1;
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_FOODS = "Foods";
    private static final String TABLE_CATEGORY = "Categories";


    // Columns for the Users table
    private static final String COL_Phone = "Phone";
    private static final String COL_Name = "Name";
    private static final String COL_PASSWORD = "Password";
    private static final String COL_EMAIL = "EMAIL";

    // Columns for the Foods table
    private static final String COL_ID = "Id";
    private static final String COL_FOOD_NAME = "Name";
    private static final String COL_IMAGE = "Image";
    private static final String COL_DESCRIPTION = "Description";
    private static final String COL_PRICE = "Price";
    private static final String COL_DISCOUNT = "Discount";
    private static final String COL_MENU_ID = "MenuId";

    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_IMAGE = "image";



    public MyDataBase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the Users table
        String createUsersTableQuery = "CREATE TABLE " + TABLE_USERS + "(" +
                COL_Phone + " TEXT PRIMARY KEY," +
                COL_Name + " TEXT," +
                COL_PASSWORD + " TEXT," +
                COL_EMAIL + " TEXT)";
        db.execSQL(createUsersTableQuery);

        // Create the Foods table
        String createFoodsTableQuery = "CREATE TABLE " + TABLE_FOODS + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_FOOD_NAME + " TEXT," +
                COL_IMAGE + " TEXT," +
                COL_DESCRIPTION + " TEXT," +
                COL_PRICE + " TEXT," +
                COL_DISCOUNT + " TEXT," +
                COL_MENU_ID + " TEXT)";
        db.execSQL(createFoodsTableQuery);

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY +
                "(" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CATEGORY_NAME + " TEXT," +
                COLUMN_CATEGORY_IMAGE + " TEXT" +
                ")";
        db.execSQL(CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);

        onCreate(db);
    }
    public void addUser(User user) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_Phone, user.getPhone());
        values.put(COL_Name, user.getName());
        values.put(COL_PASSWORD, user.getPassword());
        values.put(COL_EMAIL, user.getEmail());

        // Add other user information as necessary
        db.insert(TABLE_USERS, null, values);
        db.close();
    }
    public void addCategory(Category category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_IMAGE, category.getImage());
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                String image = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_IMAGE));
                Category category = new Category(name, image,id);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryList;
    }
    public Category getCategoryById(String categoryId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME, COLUMN_CATEGORY_IMAGE};
        String selection = COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = {categoryId};
        Cursor cursor = db.query(TABLE_CATEGORY, projection, selection, selectionArgs, null, null, null);
        Category category = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
            String image = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_IMAGE));
            category = new Category(name, image,id);
        }
        cursor.close();
        db.close();
        return category;
    }
    public boolean checkUserExists(String phoneNumber) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COL_Phone};
        String selection = COL_Phone + "=?";
        String[] selectionArgs = {phoneNumber};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean userExists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return userExists;
    }

    public boolean checkUserCredentials(String phoneNumber, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COL_Phone};
        String selection = COL_Phone + "=? AND " + COL_PASSWORD + "=?";
        String[] selectionArgs = {phoneNumber, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean validCredentials = cursor.moveToFirst();
        cursor.close();
        db.close();
        return validCredentials;
    }
    public String getPasswordFromDatabase(String email) {
        String password = null;

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COL_PASSWORD};
        String selection = COL_EMAIL + "=?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex(COL_PASSWORD));
        }
        cursor.close();
        db.close();

        return password;
    }
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        if (cursor.moveToFirst()) {
            do {
                String phone = cursor.getString(cursor.getColumnIndex(COL_Phone));
                String name = cursor.getString(cursor.getColumnIndex(COL_Name));
                String password = cursor.getString(cursor.getColumnIndex(COL_PASSWORD));
                String email = cursor.getString(cursor.getColumnIndex(COL_EMAIL));
                User user = new User(phone, name, password, email);
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }
    public void insertFood(Food food) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FOOD_NAME, food.getName());
        values.put(COL_IMAGE, food.getImage());
        values.put(COL_DESCRIPTION, food.getDescription());
        values.put(COL_PRICE, food.getPrice());
        values.put(COL_DISCOUNT, food.getDiscount());
        values.put(COL_MENU_ID, food.getMenuId());
        db.insert(TABLE_FOODS, null, values);
        db.close();
    }

    public List<Food> getAllFoods() {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOODS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(COL_FOOD_NAME));
                String image = cursor.getString(cursor.getColumnIndex(COL_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndex(COL_PRICE));
                String discount = cursor.getString(cursor.getColumnIndex(COL_DISCOUNT));
                String menuId = cursor.getString(cursor.getColumnIndex(COL_MENU_ID));
                Food food = new Food(id,name, image, description, price, discount, menuId);
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodList;
    }
    public Food getFoodById(String foodId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COL_ID, COL_FOOD_NAME, COL_IMAGE, COL_DESCRIPTION, COL_PRICE, COL_DISCOUNT, COL_MENU_ID};
        String selection = COL_ID + " = ?";
        String[] selectionArgs = {foodId};
        Cursor cursor = db.query(TABLE_FOODS, projection, selection, selectionArgs, null, null, null);
        Food food = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));

            String name = cursor.getString(cursor.getColumnIndex(COL_FOOD_NAME));
            String image = cursor.getString(cursor.getColumnIndex(COL_IMAGE));
            String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
            String price = cursor.getString(cursor.getColumnIndex(COL_PRICE));
            String discount = cursor.getString(cursor.getColumnIndex(COL_DISCOUNT));
            String menuId = cursor.getString(cursor.getColumnIndex(COL_MENU_ID));
            Log.d("FoodId", String.valueOf(id));

            food = new Food(id,name, image, description, price, discount, menuId);
        }
        cursor.close();
        db.close();
        return food;
    }
    public List<Food> getAllFoodsByCategoryId(String categoryId) {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COL_ID, COL_FOOD_NAME, COL_IMAGE, COL_DESCRIPTION, COL_PRICE, COL_DISCOUNT, COL_MENU_ID};
        String selection = COL_MENU_ID + " = ?";
        String[] selectionArgs = {categoryId};
        Cursor cursor = db.query(TABLE_FOODS, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(COL_FOOD_NAME));
                String image = cursor.getString(cursor.getColumnIndex(COL_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndex(COL_PRICE));
                String discount = cursor.getString(cursor.getColumnIndex(COL_DISCOUNT));
                String menuId = cursor.getString(cursor.getColumnIndex(COL_MENU_ID));
                Food food = new Food(id,name, image, description, price, discount, menuId);
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodList;
    }
}
