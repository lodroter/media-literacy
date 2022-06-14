package com.example.educationapp.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Spannable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.educationapp.Utils;
import com.example.educationapp.model.Theme;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ThemeDBHandler extends SQLiteOpenHelper {

    private static final int database_version = 1;
    private static final String database_name = "themes.db";
    private static final String table_themes = "theme";
    private static final String table_lessons = "lesson";
    private static final String column_id = "id";
    private static final String column_themeName = "themename";
    private static final String column_name = "name";
    private static final String level = "level";
    private static final String theme_visual = "visual";

    private static final String DB_PATH= "data/data/com.example.educationapp/databases/";
    private final Context context;
    private final String TAG = "THEME";

    public ThemeDBHandler(@Nullable Context context, SQLiteDatabase db, Spannable.Factory factory, int version) {
        super(context, database_name, null, database_version);
        this.context = context;
    }

    public void copyDB() throws IOException{
        try {

            InputStream ip =  context.getAssets().open(database_name+".db");
            String op=  DB_PATH  +  database_name ;
            OutputStream output = new FileOutputStream( op);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ip.read(buffer))>0){
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            ip.close();
        }
        catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + table_themes + " (" + column_id + " INTEGER PRIMARY KEY AUTOINCREMENT ," + column_themeName  + " TEXT ," + theme_visual + " TEXT );" ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion > oldVersion){
            try {
                copyDB();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Selecting all of the themes in database for generating list in main activity
     * @return list of Themes
     */
    public Theme[] selectThemesForList(){
        Theme[] themes = new Theme[getThemeListSize()];
        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT " + column_themeName + ", " + theme_visual + " FROM " + table_themes  + " ;";


        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(query,null);

        int i = 0;
        if(c.moveToFirst()) {
            do{
                Theme theme = new Theme();
                theme.setThemename(c.getString(0));
                theme.setVisual(Utils.getImage(c.getBlob(1)));
                themes[i] = theme;
                i++;
            }
            while(c.moveToNext());
        }

        db.close();
        return themes;
    }

    /**
     * Selecting those themes which has level passed in argument
     * @return Array of strings
     */
    public String[] getThemesByLevel(String name){

        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT " + column_name + " FROM " + table_lessons + " WHERE " + level + " = '" + name + "';";

        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(query, null);

        String[] themes = new String[c.getCount()];

        int i = 0;
        if(c.moveToFirst()){
            do{
                themes[i] = c.getString(0);
                i++;
            }
            while (c.moveToNext());
        }

        db.close();
        return themes;
    }

    //returning size of themes list
    public int getThemeListSize() {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + table_themes;

        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(query,null);

        c.moveToFirst();
        db.close();

        return c.getInt(0);
    }

    /**
     * Selecting all the levels for theme passed in argument
     * @return Array of strings
     */
    public String[] getLevelsByTheme(String theme){

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + level + " FROM " + table_lessons + " WHERE " + column_name + " = '" + theme + "';";

        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(query, null);
        String[] levels = new String[c.getCount()];

        int i = 0;
        if(c.moveToFirst()){
            do{
                levels[i] = c.getString(0);
                i++;
            }
            while (c.moveToNext());
        }

        db.close();
        return levels;
    }


}
