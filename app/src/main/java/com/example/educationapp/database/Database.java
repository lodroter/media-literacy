package com.example.educationapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database {
    private final Context context;
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public Database(Context context){
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public Database open() throws SQLException
    {
        dbHelper.openDataBase();
        dbHelper.close();
        database = dbHelper.getReadableDatabase();
        return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    public void test(){
        try{
            String query ="SELECT * FROM themes ";
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()){
                do{
                    String value = cursor.getString(0);
                    Log.d("db", value);
                }while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            //handle
        }
    }
}
