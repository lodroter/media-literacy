package com.example.educationapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.educationapp.R;
import com.example.educationapp.database.AuthenticationDBHandler;
import com.example.educationapp.database.LessonDBHandler;
import com.example.educationapp.database.StatisticsDBHandler;
import com.example.educationapp.database.ThemeDBHandler;
import com.example.educationapp.model.Level;
import com.example.educationapp.model.Statistics;
import com.example.educationapp.model.Theme;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Locale;

public class MainActivityLoggedIn extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView text;
    LessonDBHandler lessonDBHandler;
    ThemeDBHandler themeDBHandler;
    AuthenticationDBHandler authenticationDBHandler;
    String mail;
    NavigationView navigationView;
    private double progress = 0;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logged);

        text = findViewById(R.id.progress_name);
        lessonDBHandler = new LessonDBHandler(MainActivityLoggedIn.this, null, null, 2);
        themeDBHandler = new ThemeDBHandler(MainActivityLoggedIn.this, null, null, 2);
        authenticationDBHandler = new AuthenticationDBHandler(MainActivityLoggedIn.this, null, null, 2);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin",MODE_PRIVATE);
        mail = sharedPreferences.getString("mail",null);

        setUpUsernameInDrawer();

        setUpThemeScroll();
        setUpLevelScroll();

        setNavigationViewListener();
        setButtonClickedListener();

        setUpProgressBar();

    }

    /**
     * Method setting up the button in upper left corner
     * to open navigation menu
     */
    private void setButtonClickedListener() {

        ImageView menuButton = findViewById(R.id.menu_button);
        DrawerLayout drawerLayout = findViewById(R.id.navDrawer_layout);

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));


    }

    private void setUpThemeScroll() {


        LinearLayout linearLayout;
        CardView theme_card;
        Theme[] themes = themeDBHandler.selectThemesForList();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(75, 75, 90, 75);


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

    private void setUpLevelScroll() {


        LinearLayout linearLayout;
        CardView diff_card;
        Level[] levels = Level.values();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(75, 75, 90, 75);


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

    /**
     * Method getting user's name from database
     * to rewrite item in menu with user's name
     */
    private void setUpUsernameInDrawer() {
        NavigationView navigationView = findViewById(R.id.navDrawerViewLogged);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.user);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin",MODE_PRIVATE);
        authenticationDBHandler.getUsername(sharedPreferences.getString("uid", null), new StatisticsDBHandler.DataStatus() {
            @Override
            public void statsLoaded(Statistics stats) {

            }

            @Override
            public void usernameLoaded(String username) {
                menuItem.setTitle(username);
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

    private void switchToList(String name, String type) {

        Intent switchActivityIntent = new Intent(this, ListActivity.class);
        switchActivityIntent.putExtra("name", name);
        switchActivityIntent.putExtra("type", type);
        startActivity(switchActivityIntent);
    }


    private void switchToStatistics() {

        Intent switchActivityIntent = new Intent(this, StatisticsActivity.class);
        switchActivityIntent.putExtra("progress",progress);
        startActivity(switchActivityIntent);
    }

    private void switchToNotifications() {

        Intent switchActivityIntent = new Intent(this, NotificationsActivity.class);
        startActivity(switchActivityIntent);
    }

    private void switchToMain() {

        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }


    /**
     * Method setting up methods which are called
     * after on click listener is true
     */
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
            case R.id.log_out: {
                setUpPopUpLogOut();
                break;
            }
            case R.id.user: {
                setUpNewUsername();
                break;
            }
        }
        return true;
    }

    /**
     * Sets up on item click listener
     */
    private void setNavigationViewListener() {
        navigationView = findViewById(R.id.navDrawerViewLogged);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void setUpProgressBar() {

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

                double p = Double.parseDouble(lessonsList.get(0));


                progress = Double.parseDouble(String.format(Locale.ENGLISH,"%.2f", ((p / 21) * 100)));

                System.out.println(progress + " progress");

                CardView progressBarCard = findViewById(R.id.progressBar_card);
                TextView progressValue = progressBarCard.findViewById(R.id.progress_value);
                progressValue.setText(progress + " % ");


                ProgressBar progressBar = progressBarCard.findViewById(R.id.progressBar);
                Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(getResources().getColor(R.color.secondary), PorterDuff.Mode.SRC_IN);
                progressBar.setProgressDrawable(progressDrawable);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress((int) progress, true);
                }
            }

            @Override
            public void notificationLoaded(String notification) {

            }
        });
    }

    /**
     * Rendering pop up window with decision about logging up
     * and logic behind it
     */
    public void setUpPopUpLogOut() {

        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_logout);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button confirm = popup.findViewById(R.id.yes_button);
        Button cancel = popup.findViewById(R.id.no_button);

        popup.show();


        confirm.setOnClickListener(v -> {
            //deleting data from shared preferences, so the user is not longer held as logged in
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("mail");
            editor.remove("password");
            editor.remove("stats_id");
            editor.remove("uid");
            editor.apply();
            finish();
            authenticationDBHandler.signOut();
            switchToMain();
        });

        cancel.setOnClickListener(v -> popup.dismiss());

    }

    //pop up window for changing username
    public void setUpNewUsername() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);

        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_username);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button change = popup.findViewById(R.id.change);
        EditText username_placeholder = popup.findViewById(R.id.placeholder);
        authenticationDBHandler.getUserId(sharedPreferences.getString("uid", null), new StatisticsDBHandler.DataStatus() {
            @Override
            public void statsLoaded(Statistics stats) {

            }

            @Override
            public void usernameLoaded(String username) {
                username_placeholder.setHint(username);
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

        popup.show();

        change.setOnClickListener(v -> {
            authenticationDBHandler.updateUsername(username_placeholder.getText().toString(),sharedPreferences.getString("uid",null));
            setUpUsernameInDrawer();
            popup.dismiss();
        });

    }





}