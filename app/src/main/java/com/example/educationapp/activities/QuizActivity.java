package com.example.educationapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.educationapp.R;
import com.example.educationapp.Utils;
import com.example.educationapp.database.LessonDBHandler;
import com.example.educationapp.database.QuestionDBHandler;
import com.example.educationapp.database.StatisticsDBHandler;
import com.example.educationapp.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {


    private final QuestionDBHandler questionDBHandler = new QuestionDBHandler(QuizActivity.this, null,null,1);
    private final StatisticsDBHandler statisticsDBHandler = new StatisticsDBHandler();

    List<Question> questions;
    private int j = 1;
    private Boolean correct = false;
    private CardView answer;
    private CardView option1;
    private CardView option2;
    private CardView option3;
    private int corr = 0;
    private int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);

        questions = questionDBHandler.getListOfQuestions(getIntent().getExtras().getInt("id"));
        total = questions.size();

        setUpAnswers(0);

    }

    private void setUpAnswers(int i) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(75, 75, 75, 75);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp2.setMargins(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        correct = false;
        LinearLayout answers = findViewById(R.id.quiz_layout);

        answer = (CardView) View.inflate(answers.getContext(), R.layout.button_answer, null);
        answer.setLayoutParams(lp);
        option1 = (CardView) View.inflate(answers.getContext(), R.layout.button_answer, null);
        option1.setLayoutParams(lp);
        option2 = (CardView) View.inflate(answers.getContext(), R.layout.button_answer, null);
        option2.setLayoutParams(lp);
        option3 = (CardView) View.inflate(answers.getContext(), R.layout.button_answer, null);
        option3.setLayoutParams(lp);
        TextView question = findViewById(R.id.question);
        ImageView photo = findViewById(R.id.quiz_photo);


        TextView a = answer.findViewById(R.id.answer_text);
        a.setText(questions.get(i).getAnswer());

        TextView o1 = option1.findViewById(R.id.answer_text);
        o1.setText(questions.get(i).getOption1());

        TextView o2 = option2.findViewById(R.id.answer_text);
        o2.setText(questions.get(i).getOption2());

        TextView o3 = option3.findViewById(R.id.answer_text);
        o3.setText(questions.get(i).getOption3());

        question.setText(questions.get(i).getQuestion());

        if(questions.get(i).getPhoto() != null) {
            photo.setImageBitmap(Utils.getImage(questions.get(i).getPhoto()));
        }

        photo.setOnClickListener(v -> setUpPopUpZoom(i));

        a.setOnClickListener(v -> {
            correct = true;
            corr++;
            setUpPopUpWindow(i);
        });

        o1.setOnClickListener(v -> setUpPopUpWindow(i));

        o2.setOnClickListener(v -> setUpPopUpWindow(i));

        o3.setOnClickListener(v -> setUpPopUpWindow(i));


        List<CardView> rand = new ArrayList<CardView>() {{
            add(answer);
            add(option1);
            add(option2);
            add(option3);
        }};

        Collections.shuffle(rand);

        answers.addView(rand.get(0), 0);
        answers.addView(rand.get(1), 1);
        answers.addView(rand.get(2), 2);
        answers.addView(rand.get(3), 3);



    }

    public void switchToResults(){
        saveResults();

        Intent switchActivityIntent = new Intent(this, ResultsActivity.class);
        switchActivityIntent.putExtra("correct",corr);
        switchActivityIntent.putExtra("total",total);
        startActivity(switchActivityIntent);
    }

    /**
     * Pop up window rendering after every answer
     * with little note and if answer is correct or not
     */
    public void setUpPopUpWindow(int i) {

        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_window);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView result = popup.findViewById(R.id.correct);
        TextView note = popup.findViewById(R.id.note);

        if(correct){
            result.setText("Správně");
            result.setTextColor(getResources().getColor(R.color.correct_green));
        }else{
            result.setText("Špatně");
            result.setTextColor(getResources().getColor(R.color.red_alert));
        }

        note.setText(questions.get(i).getNote());

        popup.setOnDismissListener(dialog -> {
            ((ViewManager) answer.getParent()).removeView(answer);
            ((ViewManager) option1.getParent()).removeView(option1);
            ((ViewManager) option2.getParent()).removeView(option2);
            ((ViewManager) option3.getParent()).removeView(option3);
            if (j  != questions.size()) {
                QuizActivity.this.setUpAnswers(j++);
            } else {
                QuizActivity.this.switchToResults();
            }
        });

        popup.show();
    }

    /**
     * Method creating on click pop up window with the picture
     * so the picture is bigger
     */
    public void setUpPopUpZoom(int i) {

        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_picturezoom);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - 100;

        ImageView photo = popup.findViewById(R.id.quiz_photo_zoom);

        photo.setImageBitmap(Utils.getImage(questions.get(i).getPhoto()));
        photo.getLayoutParams().width = width;

        popup.show();
    }

    /**
     * Method saving results of test to database
     * If the ratio of correct and total questions
     * is over 50 %, lesson is marked as done
     */
    private void saveResults(){

        SharedPreferences sharedPreferences = getSharedPreferences("UserLogin",MODE_PRIVATE);

        String name_lesson = getIntent().getExtras().getString("quiz",null) + " + " + getIntent().getExtras().getString("level",null);
        statisticsDBHandler.saveResults(corr, total, sharedPreferences.getInt("stats_id",0), name_lesson);

        int sol = corr/total * 100;
        System.out.println(sol + " save ratio 50 ");
        LessonDBHandler lessonDBHandler = new LessonDBHandler(this,null,null,2);

        if(sol > 50) {
             lessonDBHandler.changeLessonStatus(sharedPreferences.getInt("stats_id",0));
        }

    }
}
