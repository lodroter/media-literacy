package com.example.educationapp.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.educationapp.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionDBHandler extends SQLiteOpenHelper {

    private static final int database_version = 1;
    private static final String database_name = "themes.db";
    private static final String table_questions = "test";
    private static final String column_id = "id";
    private static final String column_questionName = "question";
    private static final String answer = "answer";
    private static final String answer_type = "answer_type";
    private static final String visual = "visual";
    private static final String lesson_id = "lesson_id";


    public QuestionDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, database_name, factory, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + table_questions + " (" + column_id + " INTEGER PRIMARY KEY AUTOINCREMENT ," + column_questionName + " TEXT ," + answer + " INTEGER ," + answer_type + "TEXT," + visual+ "BLOB," + lesson_id + "INTEGER" +");" ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + table_questions );
        onCreate(db);

    }


    /**
     * Generating list of questions for corresponding test
     * @return list of questions
     */
    public List<Question> getListOfQuestions (int id){
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT * FROM " + table_questions + " WHERE " + lesson_id + " = '" + id + "' ;";

        //cursor point to a location in your results as ResultSet basically
        @SuppressLint("Recycle") Cursor c = db.rawQuery(query,null);
        int j = 0;

        if(c.moveToFirst()) {
            do{

                Question q = new Question();
                q.setQuestion(c.getString(1));
                q.setAnswer(c.getString(2));
                q.setPhoto(c.getBlob(3));
                q.setOption1(c.getString(5));
                q.setOption2(c.getString(6));
                q.setOption3(c.getString(7));
                q.setNote(c.getString(8));
                questions.add(q);
                j++;
            }
            while(c.moveToNext());
        }
        c.close();

        db.close();
        return questions;
    }

}
