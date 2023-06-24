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

public class UserDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "FoodAppDB.db";
    private static final int DB_VER = 2;
    private static final String TABLE_NAME = "Users";
    private static final String COL_Phone = "Phone";
    private static final String COL_Name = "Name";
    private static final String COL_PASSWORD = "Password";

    public UserDatabase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_Phone + " TEXT PRIMARY KEY," +
                COL_Name + " TEXT," +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUser(User user) {
        Log.d("UserDatabase", "Added user: Phone = " + user.getPhone() + ", Name = " + user.getName() + ", Password = " + user.getPassword());

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_Phone, user.getPhone());
        values.put(COL_Name, user.getName());
        values.put(COL_PASSWORD, user.getPassword());
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
        Log.d("test", phoneNumber+" " + password);
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
}
