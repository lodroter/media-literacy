package com.example.educationapp.testingDB;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DAOUser {

   private DatabaseReference databaseReference;

   public DAOUser() {

      FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
      databaseReference = firebaseDatabase.getReference("message");
   }

   public Task<Void> add(User2 user){

      return databaseReference.child("users").child("1").child("username").setValue("username");
   }

   public void read(){

      databaseReference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value = dataSnapshot.getValue(String.class);
            Log.d("TAG", "Value is: " + value);
            System.out.println(value + " value read");
         }

         @Override
         public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w("TAG", "Failed to read value.", error.toException());
         }
      });
   }
}