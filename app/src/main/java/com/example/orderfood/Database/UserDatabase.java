package com.example.orderfood.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.orderfood.Model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "FoodAppDB.db";
    private static final int DB_VER =1;
    private static final String TABLE_NAME = "Users";
    private static final String COL_Phone = "Phone";
    private static final String COL_Name = "Name";
    private static final String COL_PASSWORD = "Password";
    private static final String COL_EMAIL = "EMAIL";


    public UserDatabase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_Phone + " TEXT PRIMARY KEY," +
                COL_Name + " TEXT," +
                COL_PASSWORD + " TEXT," +
                COL_EMAIL + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public boolean checkUserExists(String phoneNumber) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COL_Phone};
        String selection = COL_Phone + "=?";
        String[] selectionArgs = {phoneNumber};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
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
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
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
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
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
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
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

}
