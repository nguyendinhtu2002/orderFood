package com.example.orderfood.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.orderfood.Model.Category;
import com.example.orderfood.Model.Food;
import com.example.orderfood.Model.HistoryOrder;
import com.example.orderfood.Model.Order;
import com.example.orderfood.Model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyDataBase extends SQLiteOpenHelper {
    private static final String DB_NAME = "FoodAppDB.db";
    private static final int DB_VER = 1;
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_FOODS = "Foods";
    private static final String TABLE_CATEGORY = "Categories";
    private static final String TABLE_ORDER = "OrderDetail";
    private static final String TABLE_HISTORY_ORDER = "history_order";

    private static final String COLUMN_HISTORY_ID = "orderId";
    private static final String COLUMN_HISTORY_PRICE = "Price";
    private static final String COLUMN_HISTORY_PHONE = "userPhone";
    private static final String COLUMN_HISTORY_DATE = "creationDate";
    private static final String COLUMN_HISTORY_ADDRESS = "deliveryAddress";
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


    // Columns for the Category table
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_IMAGE = "image";

    //Column for the Order table
    private static final String COLUMN_ORDER_ID = "Id";
    private static final String COLUMN_ORDER_USER_PHONE = "UserPhone";
    private static final String COLUMN_ORDER_PRODUCT_ID = "ProductId";
    private static final String COLUMN_ORDER_PRODUCT_NAME = "ProductName";
    private static final String COLUMN_ORDER_QUANTITY = "Quantity";
    private static final String COLUMN_ORDER_PRICE = "Price";
    private static final String COLUMN_ORDER_DISCOUNT = "Discount";


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

        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER +
                "(" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ORDER_USER_PHONE + " TEXT," +
                COLUMN_ORDER_PRODUCT_ID + " TEXT," +
                COLUMN_ORDER_PRODUCT_NAME + " TEXT," +
                COLUMN_ORDER_QUANTITY + " TEXT," +
                COLUMN_ORDER_PRICE + " TEXT," +
                COLUMN_ORDER_DISCOUNT + " TEXT" +
                ")";
        db.execSQL(CREATE_ORDER_TABLE);
        String CREATE_TABLE_HISTORY_ORDER = "CREATE TABLE " + TABLE_HISTORY_ORDER + "("
                + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_HISTORY_PRICE + " INTEGER,"
                + COLUMN_HISTORY_PHONE + " TEXT,"
                + COLUMN_HISTORY_DATE + " DATE,"
                + COLUMN_HISTORY_ADDRESS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_HISTORY_ORDER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_ORDER);

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
    public void addFood(Food food) {
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
    public void addToCart(Order order) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_PHONE, order.getUserPhone());
        values.put(COLUMN_ORDER_PRODUCT_ID, order.getProductId());
        values.put(COLUMN_ORDER_PRODUCT_NAME, order.getProductName());
        values.put(COLUMN_ORDER_PRICE, order.getPrice());
        values.put(COLUMN_ORDER_DISCOUNT, order.getDiscount());

        // Kiểm tra xem sản phẩm đã có trong đơn hàng hay chưa
        String selection = COLUMN_ORDER_PRODUCT_ID + " = ?";
        String[] selectionArgs = {order.getProductId()};
        Cursor cursor = db.query(TABLE_ORDER, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            // Sản phẩm đã tồn tại trong đơn hàng, tăng số lượng lên
            int currentQuantity = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_QUANTITY)));
            int newQuantity = currentQuantity + Integer.parseInt(order.getQuantity());
            values.put(COLUMN_ORDER_QUANTITY, String.valueOf(newQuantity));
            db.update(TABLE_ORDER, values, selection, selectionArgs);
        } else {
            // Sản phẩm chưa tồn tại trong đơn hàng, thêm mới
            values.put(COLUMN_ORDER_QUANTITY, order.getQuantity());
            db.insert(TABLE_ORDER, null, values);
        }

        cursor.close();
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

    public boolean isCategoryExists(Category category) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_CATEGORY_NAME + " = ?";
        String[] selectionArgs = {category.getName()};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }


    public List<Order> getCarts(String phone) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDER +
                " WHERE " + COLUMN_ORDER_USER_PHONE + " = ?", new String[]{phone});
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_ID));
                String userPhone= cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_USER_PHONE));
                String productId = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PRODUCT_ID));
                String productName = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PRODUCT_NAME));
                String quantity = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_QUANTITY));
                String price = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PRICE));
                String discount = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DISCOUNT));
                Order order = new Order(id,userPhone, productId, productName, quantity, price, discount);
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        return orderList;
    }
    public boolean isFoodExists(Food food) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_FOODS +
                    " WHERE " + COL_ID + " = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(food.getId())});

            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
    public boolean checkFoodExist(String phoneNumber, String food_id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COLUMN_ORDER_USER_PHONE, COLUMN_ORDER_PRODUCT_ID};
        String selection = COLUMN_ORDER_USER_PHONE + " = ? AND " + COLUMN_ORDER_PRODUCT_ID + " = ?";
        String[] selectionArgs = {phoneNumber, food_id};
        Cursor cursor = db.query(TABLE_ORDER, columns, selection, selectionArgs, null, null, null);
        boolean orderExists = cursor.moveToFirst();
        if (orderExists) {
            String existingQuantity = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_QUANTITY));

            int newQuantity = Integer.parseInt(existingQuantity) + 1;

            ContentValues values = new ContentValues();
            values.put(COLUMN_ORDER_QUANTITY, String.valueOf(newQuantity));
            db.update(TABLE_ORDER, values, selection, selectionArgs);
        }
        cursor.close();
        db.close();
        return orderExists;
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
    public List<Order> getAllOrdersByUserPhone(String phone) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_ORDER_ID,COLUMN_ORDER_USER_PHONE ,COLUMN_ORDER_PRODUCT_ID, COLUMN_ORDER_PRODUCT_NAME, COLUMN_ORDER_QUANTITY, COLUMN_ORDER_PRICE, COLUMN_ORDER_DISCOUNT};
        String selection = COLUMN_ORDER_USER_PHONE + " = ?";
        String[] selectionArgs = {String.valueOf(phone)};
        Cursor cursor = db.query(TABLE_ORDER, projection, selection, selectionArgs, null, null, null);
        Order order = null;
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_ID));
                String userPhone = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_USER_PHONE));
                String productId = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PRODUCT_ID));
                String productName = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PRODUCT_NAME));
                String quantity = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_QUANTITY));
                String price = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PRICE));
                String discount = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DISCOUNT));
                order = new Order(id,userPhone, productId, productName, quantity, price, discount);
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderList;
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
    public User getUserByPhone(String phone) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_Phone + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{phone});

        User user = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(COL_Name));
            String password = cursor.getString(cursor.getColumnIndex(COL_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndex(COL_EMAIL));
            user = new User(name, password, phone, email);
        }

        cursor.close();
        db.close();

        return user;
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

    public void cleanCart()
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("DELETE FROM " + TABLE_ORDER);
        db.execSQL(query);
    }
    public void deleteItemOnOrder(String orderId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COLUMN_ORDER_ID + " = ?";
        String[] whereArgs = {orderId};
        db.delete(TABLE_ORDER, whereClause, whereArgs);
        db.close();
    }


    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }
    public void addHistory(HistoryOrder historyOrder) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        orderValues.put(COLUMN_HISTORY_PHONE, historyOrder.getUserPhone());
        orderValues.put(COLUMN_HISTORY_DATE, formatDate(historyOrder.getCreationDate()));
        orderValues.put(COLUMN_HISTORY_ADDRESS, historyOrder.getDeliveryAddress());
        orderValues.put(COLUMN_HISTORY_PRICE, historyOrder.getPrice());
        db.insert(TABLE_HISTORY_ORDER, null, orderValues);
        db.close();
    }

    public List<HistoryOrder> getAllHistoryOrdersByPhone(String phone) {
        List<HistoryOrder> historyOrders = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_HISTORY_ORDER + " WHERE " + COLUMN_HISTORY_PHONE + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{phone});

        if (cursor.moveToFirst()) {
            do {
                // Retrieve the history order details from the cursor
                String userPhone = cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_PHONE));
                String creationDateStr = cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_DATE));
                String deliveryAddress = cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_ADDRESS));
                String price = cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_PRICE));

                // Convert the creation date string to a Date object
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date creationDate;
                try {
                    creationDate = sdf.parse(creationDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                    continue; // Skip this iteration if the creation date is invalid
                }

                // Create a new HistoryOrder object
                HistoryOrder historyOrder = new HistoryOrder(userPhone, deliveryAddress, creationDate,price,"Giao hàng thành công");

                // Add the HistoryOrder to the list
                historyOrders.add(historyOrder);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return historyOrders;
    }
    public boolean checkOrderExists(String orderId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HISTORY_ORDER + " WHERE " + COLUMN_HISTORY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{orderId});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}