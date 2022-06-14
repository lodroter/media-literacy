package com.example.educationapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.educationapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class AuthenticationDBHandler extends SQLiteOpenHelper {

    private static final int database_version = 1;
    private static final String database_name = "themes.db";
    private static final String table_user = "user";
    private static final String column_id = "id";
    private static final String column_username = "username";
    private static final String column_mail = "mail";
    private static final String column_password = "password";
    private static final String picture = "picture";
    private static final String notifications = "notifications";
    private static final String statistics_id = "statistics_id";
    private DatabaseReference databaseReference;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private int id = 0;


    public AuthenticationDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, database_name, factory, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + table_user + " (" + column_id + " INTEGER PRIMARY KEY AUTOINCREMENT ," + column_username + " TEXT ," + column_mail + " TEXT ," + column_password + "TEXT," + picture + "BLOB," + notifications + "TEXT," + statistics_id + "INTEGER" + ");" ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + table_user );
        onCreate(db);

    }

    /**
     * Getting user's id from database to create stats for user
     * @return uid
     */
    public int getAutoIncrementedId(StatisticsDBHandler.DataStatus dataStatus) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    id = (int) snapshot.getChildrenCount();
                    dataStatus.idLoaded(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return id;
    }

    /**
     * Updating time of daily notifications
     */
    public void setNotifications(String time, String uid){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("notifications");

        databaseReference.setValue(time);

    }

    /**
     * Selecting current time of daily notifications to set up UI
     * @return time
     */
    public String getNotifications(String uid, StatisticsDBHandler.DataStatus dataStatus){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("notifications");

        final String[] notification = new String[1];

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("username");
                notification[0] = snapshot.getValue().toString();
                System.out.println(notification[0]);
                dataStatus.notificationLoaded(notification[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return notification[0];
    }


    /**
     * Updates user's name according to user's input
     */
    public void updateUsername(String username, String uid) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("username");
        databaseReference.setValue(username);
    }


    /**
     * Logging out an user in database
     */
    public void signOut(){
        auth.signOut();
        Log.i("Authentication DB", "User is logged out.");
    }


    /**
     * Selecting user's name for rendering menu with user's name as item
     * @return username
     */
    public String getUsername(String uid, StatisticsDBHandler.DataStatus dataStatus){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("username");

        final String[] username = new String[1];

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username[0] = snapshot.getValue().toString();
                dataStatus.usernameLoaded(username[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return username[0];
    }


    /**
     * Getting user's stats id
     * @return stats_id
     */
    public int getUserId(String uid, StatisticsDBHandler.DataStatus dataStatus){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("statistics_id");

        final int[] id = new int[1];

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id[0] = Integer.parseInt(snapshot.getValue().toString());
                dataStatus.idLoaded(id[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return id[0];

    }
}
