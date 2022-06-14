package com.example.educationapp.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.educationapp.model.Lesson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LessonDBHandler extends SQLiteOpenHelper {

    private static final int database_version = 1;
    private static final String database_name = "themes.db";
    private static final String table_lessons = "lesson";
    private static final String column_id = "id";
    private static final String column_lessonName = "name";
    private static final String column_level = "level";
    private static final String content = "content";
    private static final String photo = "photo";
    private static final String tip = "tip";
    private static final String definition = "definition";
    private static final String example = "example";
    private static final String visual = "visual";

    public LessonDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, database_name, factory, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + table_lessons + " (" + column_id + " INTEGER PRIMARY KEY AUTOINCREMENT ," + column_lessonName + " TEXT ," + column_level + "TEXT," + content + "TEXT," +  tip + "TEXT," + definition + "TEXT," + example + "BLOB," + visual + "BLOB" + photo + "BLOB " + ");" ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + table_lessons );
        onCreate(db);

    }

    /**
     * Selecting lesson chosen columns from database
     * @return Lesson
     */
    public Lesson getLessonContent (String theme, String level){

        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT " + content + "," + tip + "," + definition + "," + example + "," + visual + "," + photo  + " FROM " + table_lessons + " WHERE " + column_lessonName + " = '" + theme + "' AND " + column_level + " ='" + level + "' ;";

        //cursor point to a location in your results as ResultSet basically
        @SuppressLint("Recycle") Cursor c = db.rawQuery(query,null);

        Lesson l = new Lesson();

        if(c.moveToFirst()) {
            do{
                l.setContent(c.getString(0));
                l.setTip(c.getString(1));
                l.setDefinition(c.getString(2));
                l.setExample(c.getBlob(3));
                l.setVisual(c.getBlob(4));
                l.setPhoto(c.getBlob(5));
                l.setName(theme);
            }
            while(c.moveToNext());
        }
        c.close();

        db.close();
        return l;
    }


    /**
     * Selecting theme's id for rendering lesson or test corresponding to chosen theme and level
     * @return id
     */
    public int getIdOfLesson(String name){

        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT " + column_id +" FROM " + table_lessons + " WHERE " + column_lessonName + " = '" + name + "' ;";


        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(query,null);

        c.moveToFirst();
        db.close();

        return c.getInt(0);

    }

    /**
     * Getting id of lesson or test corresponding to chosen one in list
     * @return id
     */
    public int getIdOfLessonLevel (String l, String n) {
        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT " + column_id +" FROM " + table_lessons + " WHERE " + column_level + " = '" + l + "' AND " + column_lessonName + " ='" + n + "';";

        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(query,null);

        c.moveToFirst();
        db.close();

        return c.getInt(0);
    }

    /**
     * Selecting status of each theme and level for rendering progress
     * @return list of strings
     */
    public List<String> getLessonsStatus(int id,StatisticsDBHandler.DataStatus dataStatus){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("statistics").child(String.valueOf(id)).child("done");
        List<String> done = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    done.add(0, snapshot.getValue().toString());
                }
                dataStatus.statusLoaded(done);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return done;
    }

    /**
     * Method enabling read and update in one moment
     */
    private void transaction(DatabaseReference databaseRef){


        databaseRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                if (currentData.getValue() == null) {
                    return Transaction.success(currentData);
                }

                int currDone = Integer.parseInt(currentData.getValue().toString()) + 1;

                currentData.setValue(currDone);

                return Transaction.success(currentData);

            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });

    }

    //calling transaction method for correct column
    public void changeLessonStatus(int id){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("statistics").child(String.valueOf(id)).child("done");

        transaction(databaseReference);

    }

}
