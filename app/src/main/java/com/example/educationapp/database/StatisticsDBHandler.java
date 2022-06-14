package com.example.educationapp.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.educationapp.model.Statistics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StatisticsDBHandler {

    private DatabaseReference databaseReference;


    /**
     * Creating new stats row for the user
     */
    public void createStatsForUser(int id){

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("statistics").child(String.valueOf(id)).child("answered").setValue(0);
        databaseReference.child("statistics").child(String.valueOf(id)).child("correct").setValue(0);
        databaseReference.child("statistics").child(String.valueOf(id)).child("wrong").setValue(0);
        databaseReference.child("statistics").child(String.valueOf(id)).child("best_score").setValue("0/0");
        databaseReference.child("statistics").child(String.valueOf(id)).child("best_lesson").setValue("");
        databaseReference.child("statistics").child(String.valueOf(id)).child("worst_score").setValue("0/0");
        databaseReference.child("statistics").child(String.valueOf(id)).child("worst_lesson").setValue("");
        databaseReference.child("statistics").child(String.valueOf(id)).child("done").setValue(0);

    }

    /**
     * Interface enabling ,,stoping" everything til the data are read
     */
    public interface DataStatus{
        void statsLoaded(Statistics stats);
        void usernameLoaded(String username);
        void idLoaded(int id);
        void statusLoaded(List<String> lessonsList);
        void notificationLoaded(String notification);
    }

    /**
     * Selecting statistics data for the user
     */
    public void selectStats(int stats_id, final DataStatus dataStatus){

        final Statistics[] stats = new Statistics[1];

        databaseReference = FirebaseDatabase.getInstance().getReference().child("statistics").child(String.valueOf(stats_id));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    stats[0] = snapshot.getValue(Statistics.class);

                }
                dataStatus.statsLoaded(stats[0]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("STATS",error.getMessage());
            }
        });
    }

    /**
     * Method reading and updating read data quickly in one call
     */
    private void transaction(DatabaseReference databaseRef, String child1, int newValue, String child, int total, String lessonName){

        databaseRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                if (currentData.getValue() == null) {
                    return Transaction.success(currentData);
                }

                if(child.equals("best_lesson")) {

                    String bestScorePrev = currentData.child(child1).getValue().toString();
                    String[] splits = bestScorePrev.split("/");
                    double per;
                    double val;

                    per = (Double.parseDouble(String.valueOf(splits[0]))/Double.parseDouble(splits[1])) * 100;

                    val = ((double) newValue/(double) total) * 100;

                    if(per <= val || splits[1].equals("0")){
                        currentData.child(child1).setValue(newValue + "/" + total);
                        currentData.child(child).setValue(lessonName);
                    }

                }else if(child.equals("worst_lesson")) {

                    String bestScorePrev = currentData.child(child1).getValue().toString();
                    String[] splits = bestScorePrev.split("/");
                    double per;
                    double val;

                    per = (Double.parseDouble(String.valueOf(splits[0]))/Double.parseDouble(splits[1])) * 100;

                    val = ((double) newValue/ (double) total) * 100;

                    if(per >= val || splits[1].equals("0")){
                        currentData.child(child1).setValue(newValue + "/" + total);
                        currentData.child(child).setValue(lessonName);
                    }

                }else{
                    int prevValue = Integer.parseInt(currentData.child(child1).getValue().toString()) + newValue;
                    currentData.child(child1).setValue(prevValue);
                }

                return Transaction.success(currentData);

            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });

    }

    /**
     * Method calling transaction method for reading and updating statistics
     * @param correct
     * @param total
     * @param user_id
     * @param lesson
     */
    public void saveResults(int correct, int total, int user_id, String lesson){

        //defined strings for cycle
        String[] children = {"answered","correct","wrong","best_score","worst_score"};
        String[] children2 = {"answered","correct","wrong","best_score","best_lesson","worst_lesson",""};

        int[] values = {total,correct,(total-correct),correct,correct};
        for(int i = 0; i < values.length; i++){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("statistics").child(String.valueOf(user_id));

            transaction(databaseReference,children[i],values[i],children2[i+1],total, lesson);

        }

    }

}
