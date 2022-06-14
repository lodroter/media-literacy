package com.example.educationapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.educationapp.R;
import com.example.educationapp.database.LessonDBHandler;
import com.example.educationapp.database.StatisticsDBHandler;
import com.example.educationapp.database.ThemeDBHandler;
import com.example.educationapp.model.Statistics;

import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity{

    private ThemeDBHandler themeDBHandler;
    private LessonDBHandler lessonDBHandler;
    private String lesson;
    private String level;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        themeDBHandler = new ThemeDBHandler(ListActivity.this,null,null,1);
        lessonDBHandler = new LessonDBHandler(ListActivity.this,null,null,1);

        if(getIntent().getExtras().getString("type").equals("level")){
            setUpScroll();
        }else{
            setUpScrollForTheme();
        }

        setUpProgressBar();


    }


    private void setUpScroll(){

        LinearLayout linearLayout;
        CardView list_card;
        String[] themes = themeDBHandler.getThemesByLevel(getIntent().getExtras().getString("name"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(75,75,75,75);
        linearLayout = findViewById(R.id.scrollList);


        for (String theme : themes) {

            list_card = (CardView) View.inflate(linearLayout.getContext(), R.layout.list_item, null);
            list_card.setLayoutParams(lp);

            TextView t = list_card.findViewById(R.id.item_text);
            t.setText(theme);

            list_card.findViewById(R.id.imageView).setOnClickListener(v -> {
                lesson = String.valueOf(t.getText());
                level = getIntent().getExtras().getString("name");
                id = lessonDBHandler.getIdOfLesson(lesson);
                switchToLesson();
            });
            list_card.findViewById(R.id.imageView2).setOnClickListener(v -> {
                lesson = String.valueOf(t.getText());
                level = getIntent().getExtras().getString("name");
                id = lessonDBHandler.getIdOfLesson(lesson);
                switchToTest();
            });

            linearLayout.addView(list_card);

        }

        RelativeLayout relativeLayout = findViewById(R.id.progress_list_layout);
        CardView progress = relativeLayout.findViewById(R.id.progress_include);
        TextView name = progress.findViewById(R.id.progress_name);
        name.setText("Progress");

    }

    private void setUpScrollForTheme(){

        LinearLayout linearLayout;
        CardView list_card;
        String[] levels = themeDBHandler.getLevelsByTheme(getIntent().getExtras().getString("name"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(75,75,75,75);


        int i = 1;
        for(String l : levels) {

            linearLayout = findViewById(R.id.scrollList);
            list_card = (CardView) View.inflate(linearLayout.getContext(), R.layout.list_item, null);
            list_card.setLayoutParams(lp);

            TextView t = list_card.findViewById(R.id.item_text);
            t.setText("Level " + i);

            list_card.findViewById(R.id.imageView).setOnClickListener(v -> {
                level = String.valueOf(t.getText());
                lesson = getIntent().getExtras().getString("name");
                id = lessonDBHandler.getIdOfLessonLevel(level,lesson);
                switchToLesson();
            });
            list_card.findViewById(R.id.imageView2).setOnClickListener(v -> {
                level = String.valueOf(t.getText());
                lesson = getIntent().getExtras().getString("name");
                id = lessonDBHandler.getIdOfLessonLevel(level,lesson);
                System.out.println(lesson + " " + level + " " + id + " from list to test switch");
                switchToTest();
            });

            linearLayout.addView(list_card);
            i++;

        }

        RelativeLayout relativeLayout = findViewById(R.id.progress_list_layout);
        CardView progress = relativeLayout.findViewById(R.id.progress_include);
        TextView name = progress.findViewById(R.id.progress_name);
        name.setText("Progress");

    }


    public void switchToLesson(){

        Intent switchActivityIntent = new Intent(this,LessonActivity.class);
        switchActivityIntent.putExtra("lesson", lesson);
        switchActivityIntent.putExtra("level",level);
        startActivity(switchActivityIntent);
    }

    public void switchToTest(){

        Intent switchActivityIntent = new Intent(this,QuizActivity.class);
        switchActivityIntent.putExtra("level", level);
        switchActivityIntent.putExtra("quiz", lesson);
        switchActivityIntent.putExtra("id",id);
        startActivity(switchActivityIntent);
    }

    public void switchBackToMain(){

        Intent switchActivityIntent = new Intent(this,MainActivity.class);
        startActivity(switchActivityIntent);
    }

    private void setUpProgressBar(){

        RelativeLayout relativeLayout = findViewById(R.id.progress_list_layout);
        CardView progress = relativeLayout.findViewById(R.id.progress_include);
        TextView value = progress.findViewById(R.id.progress_value);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);
        lessonDBHandler.getLessonsStatus(sharedPreferences.getInt("stats_id",0), new StatisticsDBHandler.DataStatus() {
            @Override
            public void statsLoaded(Statistics stats) {

            }

            @Override
            public void usernameLoaded(String username) {

            }

            @Override
            public void idLoaded(int id) {

            }

            @Override
            public void statusLoaded(List<String> lessonsList) {

                double p,v;

                if(sharedPreferences.getString("mail",null) != null){

                    p = Double.parseDouble(lessonsList.get(0));

                    v = Double.parseDouble(String.format(Locale.ENGLISH,"%.2f", ((p / 21) * 100)));
                }else{

                    v = 0;
                }

                value.setText(v + " % ");

                ProgressBar progressBar = progress.findViewById(R.id.progressBar);
                Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(getResources().getColor(R.color.secondary), PorterDuff.Mode.SRC_IN);
                progressBar.setProgressDrawable(progressDrawable);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress((int) v,true);
                }
            }

            @Override
            public void notificationLoaded(String notification) {

            }
        });
    }

}
