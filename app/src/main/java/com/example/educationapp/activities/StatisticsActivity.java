package com.example.educationapp.activities;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.educationapp.R;
import com.example.educationapp.database.StatisticsDBHandler;
import com.example.educationapp.model.Statistics;

import java.util.List;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        setUpStats();


    }

    public void setUpStats(){

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(75, 75, 75, 75);

        LinearLayout layout = findViewById(R.id.layout_stats);

        CardView res = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res.setLayoutParams(lp);

        CardView res2 = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res2.setLayoutParams(lp);

        CardView res3 = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res3.setLayoutParams(lp);

        CardView res4 = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res4.setLayoutParams(lp);

        CardView res5 = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res5.setLayoutParams(lp);

        CardView res6 = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res6.setLayoutParams(lp);

        CardView res7 = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res7.setLayoutParams(lp);

        CardView res8 = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res8.setLayoutParams(lp);

        CardView progressBarCard = findViewById(R.id.progressBar_card);
        TextView progressValue = progressBarCard.findViewById(R.id.progress_value);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);
        progressValue.setText(getIntent().getExtras().getDouble("progress") + " %");

        ProgressBar progressBar = progressBarCard.findViewById(R.id.progressBar);
        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(getResources().getColor(R.color.secondary), PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress((int) getIntent().getExtras().getDouble("progress"), true);
        }


        StatisticsDBHandler statisticsDBHandler = new StatisticsDBHandler();

        statisticsDBHandler.selectStats(sharedPreferences.getInt("stats_id", 0), new StatisticsDBHandler.DataStatus() {
            @Override
            public void statsLoaded(Statistics stats) {

            TextView r = res.findViewById(R.id.result);
            r.setText("Správně");

            TextView v = res.findViewById(R.id.value);
            v.setText(String.valueOf(stats.getCorrect()));


            TextView r2 = res2.findViewById(R.id.result);
            r2.setText("Celkem");

            TextView v2 = res2.findViewById(R.id.value);
            v2.setText(String.valueOf(stats.getAnswered()));

            TextView r3 = res3.findViewById(R.id.result);
            r3.setText("Úspěšnost");

            TextView v3 = res3.findViewById(R.id.value);
            double p = 0;
            double s1 = stats.getCorrect();
            double s2 = stats.getAnswered();
            if (s2 != 0) {
                p = (s1 / s2) * 100;
            }
            v3.setText(String.format(Locale.GERMANY,"%.2f", p)+ " %");

            TextView r4 = res4.findViewById(R.id.result);
            r4.setText("Špatně");

            TextView v4 = res4.findViewById(R.id.value);
            v4.setText(String.valueOf(stats.getWrong()));

            if (!stats.getBest_lesson().equals("") && !stats.getWorst_lesson().equals("")) {
                TextView r5 = res5.findViewById(R.id.result);
                r5.setText("Nejlepší lekce ");

                TextView v5 = res5.findViewById(R.id.value);
                String[] splits = stats.getBest_lesson().split("\\+");
                v5.setText(splits[0] + "\n" + splits[1]);

                TextView r6 = res6.findViewById(R.id.result);
                r6.setText("Nejhorší lekce ");

                TextView v6 = res6.findViewById(R.id.value);
                String[] splits2 = stats.getWorst_lesson().split("\\+");
                v6.setText(splits2[0] + "\n" + splits2[1]);

                TextView r7 = res7.findViewById(R.id.result);
                r7.setText("Nejlepší scóre");

                TextView v7 = res7.findViewById(R.id.value);
                v7.setText(stats.getBest_score());

                TextView r8 = res8.findViewById(R.id.result);
                r8.setText("Nejhorší scóre");

                TextView v8 = res8.findViewById(R.id.value);
                v8.setText(stats.getWorst_score());

            } else {
                TextView r5 = res5.findViewById(R.id.result);
                r5.setText("Nejlepší lekce ");

                TextView v5 = res5.findViewById(R.id.value);
                v5.setText("");

                TextView r6 = res6.findViewById(R.id.result);
                r6.setText("Nejhorší lekce ");

                TextView v6 = res6.findViewById(R.id.value);
                v6.setText("");

                TextView r7 = res7.findViewById(R.id.result);
                r7.setText("Nejlepší scóre");

                TextView v7 = res7.findViewById(R.id.value);
                v7.setText("0");

                TextView r8 = res8.findViewById(R.id.result);
                r8.setText("Nejhorší scóre");

                TextView v8 = res8.findViewById(R.id.value);
                v8.setText("0");

            }

            if(res.getParent() != null || res4.getParent() !=null || res2.getParent() !=null || res3.getParent() != null || res5.getParent() !=null || res6.getParent() !=null){
                ((ViewGroup) res.getParent()).removeView(res);
                ((ViewGroup) res2.getParent()).removeView(res2);
                ((ViewGroup) res3.getParent()).removeView(res3);
                ((ViewGroup) res4.getParent()).removeView(res4);
                ((ViewGroup) res5.getParent()).removeView(res5);
                ((ViewGroup) res6.getParent()).removeView(res6);
                ((ViewGroup) res7.getParent()).removeView(res7);
                ((ViewGroup) res8.getParent()).removeView(res8);

            }
            layout.addView(res,1);
            layout.addView(res4,2);
            layout.addView(res2,3);
            layout.addView(res3,4);
            layout.addView(res5,5);
            layout.addView(res7,6);
            layout.addView(res6,7);
            layout.addView(res8,8); }

            @Override
            public void usernameLoaded(String username) {

            }

            @Override
            public void idLoaded(int id) {

            }

            @Override
            public void statusLoaded(List<String> lessonsList) {

            }

            @Override
            public void notificationLoaded(String notification) {

            }

        });

    }

}
