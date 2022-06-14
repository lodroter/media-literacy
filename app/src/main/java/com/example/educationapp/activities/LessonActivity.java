package com.example.educationapp.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.educationapp.R;
import com.example.educationapp.Utils;
import com.example.educationapp.database.LessonDBHandler;
import com.example.educationapp.model.Lesson;

public class LessonActivity extends AppCompatActivity {

    private final LessonDBHandler lessonDBHandler = new LessonDBHandler(this,null,null,2);
    private Lesson l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson);

        l = lessonDBHandler.getLessonContent(getIntent().getExtras().getString("lesson"),getIntent().getExtras().getString("level"));

        setUpText();

    }

    //method filling up UI with data
    private void setUpText(){

        LinearLayout linearLayout;
        TextView name;
        TextView content;
        CardView def;
        CardView tip;
        TextView definition;
        TextView tipText;

        linearLayout = findViewById(R.id.lesson_layout);
        name = linearLayout.findViewById(R.id.lesson_name);
        content = linearLayout.findViewById(R.id.content);
        def = (CardView) View.inflate(LessonActivity.this, R.layout.definition,null);
        definition = def.findViewById(R.id.definition);
        tip = (CardView) View.inflate(LessonActivity.this,R.layout.note,null);
        tipText = tip.findViewById(R.id.definition);

        if(l.getDefinition() != null){
            definition.setText(l.getDefinition());
            linearLayout.addView(def,1);
        }

        name.setText(l.getName());
        content.setText(Html.fromHtml(l.getContent()));


        if(l.getTip() != null){
            tipText.setText(l.getTip());
            linearLayout.addView(tip);
        }

        if(l.getVisual() != null){
            CardView photo1 = (CardView) View.inflate(this,R.layout.photo,null);
            ImageView visual = photo1.findViewById(R.id.photo);
            visual.setImageBitmap(Utils.getImage(l.getVisual()));
            linearLayout.addView(photo1);
        }

        if(l.getPhoto() != null){
            CardView photo2 = (CardView) View.inflate(this,R.layout.photo,null);
            ImageView photo = photo2.findViewById(R.id.photo);
            photo.setImageBitmap(Utils.getImage(l.getPhoto()));
            linearLayout.addView(photo2);
        }

        if(l.getExample() != null){
            CardView photo3 = (CardView) View.inflate(this,R.layout.photo,null);
            ImageView example = photo3.findViewById(R.id.photo);
            example.setImageBitmap(Utils.getImage(l.getExample()));
            linearLayout.addView(photo3);
        }


    }

}
