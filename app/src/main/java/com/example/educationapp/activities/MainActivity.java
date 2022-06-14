package com.example.educationapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.educationapp.R;
import com.example.educationapp.database.LessonDBHandler;
import com.example.educationapp.database.ThemeDBHandler;
import com.example.educationapp.model.Level;
import com.example.educationapp.model.Theme;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    TextView text;
    ThemeDBHandler themeDBHandler;
    LessonDBHandler lessonDBHandler;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.progress_name);
        themeDBHandler = new ThemeDBHandler(MainActivity.this,null,null,2);
        lessonDBHandler = new LessonDBHandler(MainActivity.this,null,null,2);



        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin",MODE_PRIVATE);
        String mail = sharedPreferences.getString("mail",null);


        if(mail!= null){
            switchToLoggedInMain();
        }


        //this code below copies predefined database file from assets folder to database folder
        //so the new generated empty database file is overwritten with already filled up database
        File dbFilePath = new File(this.getApplicationInfo().dataDir + "/databases/themes.db");

        try {
            InputStream source = this.getAssets().open("themes.db");
            OutputStream destination = new FileOutputStream(dbFilePath);

            byte[] buffer = new byte[4096];
            int length;

            while ((length = source.read(buffer))>0)
            {
                destination.write(buffer, 0, length);
            }
            destination.flush();
            destination.close();
            source.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        setUpThemeScroll();
        setUpLevelScroll();

        setNavigationViewListener();
        setButtonClickedListener();


    }

    private void setUpThemeScroll(){


        LinearLayout linearLayout;
        CardView theme_card;
        Theme[] themes = themeDBHandler.selectThemesForList();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(75,75,75,75);


        for (Theme theme : themes) {

            linearLayout = findViewById(R.id.layout_horizontal_theme);
            theme_card = (CardView) View.inflate(linearLayout.getContext(), R.layout.theme_card, null);
            theme_card.setLayoutParams(lp);

            TextView t = theme_card.findViewById(R.id.theme_name);
            t.setText(theme.getThemename());

            ImageView logo = theme_card.findViewById(R.id.theme_card_logo);
            logo.setImageBitmap(theme.getVisual());

            theme_card.setOnClickListener(v -> switchToList(String.valueOf(t.getText()), "theme"));

            linearLayout.addView(theme_card);

        }


    }

    private void setUpLevelScroll(){


        LinearLayout linearLayout;
        CardView diff_card;
        Level[] levels = Level.values();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(75,75,75,75);


        for (Level level : levels) {

            linearLayout = findViewById(R.id.layout_horizontal_diff);
            diff_card = (CardView) View.inflate(linearLayout.getContext(), R.layout.difficulty_card, null);
            diff_card.setLayoutParams(lp);

            TextView t = diff_card.findViewById(R.id.diff_name);
            t.setText(level.toString());
            String l = level.toString();


            diff_card.setOnClickListener(v -> switchToList(l, "level"));

            linearLayout.addView(diff_card);

        }


    }

    private void setButtonClickedListener() {

        ImageView menuButton = findViewById(R.id.menu_button);
        DrawerLayout drawerLayout = findViewById(R.id.navDrawer_layout);

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

    }

    private void switchToList(String name, String type){

        Intent switchActivityIntent = new Intent(this,ListActivity.class);
        switchActivityIntent.putExtra("name",name);
        switchActivityIntent.putExtra("type",type);
        startActivity(switchActivityIntent);
    }


    private void switchToStatistics(){

        Intent switchActivityIntent = new Intent(this,StatisticsActivity.class);
        startActivity(switchActivityIntent);
    }

    private void switchToNotifications(){

        Intent switchActivityIntent = new Intent(this,NotificationsActivity.class);
        startActivity(switchActivityIntent);
    }

    private void switchToLogin(){

        Intent switchActivityIntent = new Intent(this, AuthenticationActivity.class);
        startActivity(switchActivityIntent);
    }

    private void switchToLoggedInMain(){

        Intent switchActivityIntent = new Intent(this,MainActivityLoggedIn.class);
        startActivity(switchActivityIntent);
        finish();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.miItem1: {
                switchToStatistics();
                break;
            }
            case R.id.miItem2: {
                switchToNotifications();
                break;
            }
            case R.id.miItem3: {
                break;
            }
            case R.id.user: {
                switchToLogin();
                break;
            }
        }
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.navDrawer);
        navigationView.setNavigationItemSelectedListener(this);
    }

}