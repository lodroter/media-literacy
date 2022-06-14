package com.example.educationapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.educationapp.R;
import com.example.educationapp.database.AuthenticationDBHandler;
import com.example.educationapp.database.StatisticsDBHandler;
import com.example.educationapp.model.Statistics;
import com.example.educationapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AuthenticationActivity extends AppCompatActivity {

    private final AuthenticationDBHandler authenticationDBHandler = new AuthenticationDBHandler(AuthenticationActivity.this,null,null,2);
    private final StatisticsDBHandler statisticsDBHandler = new StatisticsDBHandler();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final String TAG = "Authentication";

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setUpLayout();

    }

    /**
     * Method setting up UI of activity
     * Handling logic behind authentication
     */

    private void setUpLayout() {

        LinearLayout linearLayout = findViewById(R.id.login_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(75,35,75,35);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp2.setMargins(375,50,375,50);

        CardView mail = (CardView) View.inflate(linearLayout.getContext(), R.layout.edit_text, null);
        CardView password = (CardView) View.inflate(linearLayout.getContext(), R.layout.edit_text, null);
        CardView signUp = (CardView) View.inflate(linearLayout.getContext(),R.layout.button_login,null);
        CardView login = (CardView) View.inflate(linearLayout.getContext(), R.layout.button_login, null);

        mail.setLayoutParams(lp);
        password.setLayoutParams(lp);
        signUp.setLayoutParams(lp2);
        login.setLayoutParams(lp2);

        EditText mail_placeholder = mail.findViewById(R.id.placeholder);
        EditText password_placeholder = password.findViewById(R.id.placeholder);
        TextView signUpText = signUp.findViewById(R.id.auth_button);
        TextView loginText = login.findViewById(R.id.auth_button);

        mail_placeholder.setHint("Email");
        password_placeholder.setHint("Heslo");
        signUpText.setText("Registrovat");
        loginText.setText("Přihlásit");

        password_placeholder.setTransformationMethod(PasswordTransformationMethod.getInstance());

        TextView alert = new TextView(AuthenticationActivity.this);
        alert.setEnabled(false);
        alert.setTextColor(getResources().getColor(R.color.red_alert));
        alert.setGravity(Gravity.CENTER);

        signUp.setOnClickListener(v ->
        {
            if(!mail_placeholder.getText().toString().equals("") && !password_placeholder.getText().toString().equals("")){

                if(password_placeholder.getText().toString().length() >= 10){

                authenticationDBHandler.getAutoIncrementedId(new StatisticsDBHandler.DataStatus() {
                    @Override
                    public void statsLoaded(Statistics stats) {

                    }

                    @Override
                    public void usernameLoaded(String username) {

                    }

                    @Override
                    public void idLoaded(int id) {

                        firebaseAuth.createUserWithEmailAndPassword(mail_placeholder.getText().toString(),password_placeholder.getText().toString()).addOnCompleteListener(task -> {

                            if(task.isSuccessful()){


                                User user = new User("User#" + id ,password_placeholder.getText().toString(), mail_placeholder.getText().toString(), id,"8:00");
                                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        statisticsDBHandler.createStatsForUser(id);
                                        alert.setEnabled(true);
                                        alert.setText("Jsi zaregistrovan.");
                                        alert.setTextColor(Color.GREEN);
                                    }
                                });

                            }else{
                                try {
                                    if(password_placeholder.getText().toString().length() < 10){
                                        alert.setEnabled(true);
                                        alert.setText("Heslo musí mít alespoň 10 znaků");
                                        alert.setTextColor(getResources().getColor(R.color.red_alert));
                                        Log.e(TAG, "Password has not met the criteria.");
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void statusLoaded(List<String> lessonsList) {

                    }

                    @Override
                    public void notificationLoaded(String notification) {

                    }
                });
        }else if(Patterns.EMAIL_ADDRESS.matcher(mail_placeholder.getText().toString()).matches()){
                    alert.setEnabled(true);
                    alert.setText("Email nemá správný formát");
                    alert.setTextColor(getResources().getColor(R.color.red_alert));
                    Log.e(TAG, "Mail has not correct form.");
            }else{
                    alert.setEnabled(true);
                    alert.setTextColor(getResources().getColor(R.color.red_alert));
                    alert.setText("Heslo musí mít alespoň 10 znaků!");
                    alert.requestFocus();
                    Log.e(TAG, "Password has not met criteria.");
                }
        }else{
                alert.setEnabled(true);
                alert.setTextColor(getResources().getColor(R.color.red_alert));
                alert.setText("Musí být vyplněna obě pole!");
                alert.requestFocus();
                Log.e(TAG, "At least one field is empty.");
            }
        });

        login.setOnClickListener(v -> firebaseAuth.signInWithEmailAndPassword(mail_placeholder.getText().toString(),password_placeholder.getText().toString()).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Log.i(TAG,"User is logged in successfully");
                authenticationDBHandler.getUserId(FirebaseAuth.getInstance().getCurrentUser().getUid(), new StatisticsDBHandler.DataStatus() {
                    @Override
                    public void statsLoaded(Statistics stats) {

                    }

                    @Override
                    public void usernameLoaded(String username) {

                    }

                    @Override
                    public void idLoaded(int id) {
                        SharedPreferences sharedPreferences = getSharedPreferences("UserLogin", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mail", mail_placeholder.getText().toString());
                        editor.putString("password",password_placeholder.getText().toString());
                        editor.putInt("stats_id",id);
                        editor.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Log.i(TAG, "User is successfully added to shared preferences.");
                        editor.apply();
                        finish();
                        switchToMainActivity(true);
                    }

                    @Override
                    public void statusLoaded(List<String> lessonsList) {

                    }

                    @Override
                    public void notificationLoaded(String notification) {

                    }
                });
            }else{
                alert.setEnabled(true);
                alert.setTextColor(getResources().getColor(R.color.red_alert));
                alert.setText("Email nebo heslo není správně");
                Log.e(TAG,"Mail and password are not correct.");
            }
        }));

        signUp.setCardBackgroundColor(getResources().getColor(R.color.primary));
        signUpText.setTextColor(getResources().getColor(R.color.secondary));

        linearLayout.addView(alert);
        linearLayout.addView(mail);
        linearLayout.addView(password);
        linearLayout.addView(login);
        linearLayout.addView(signUp);

    }

    //method switching back to main activity after successful login
    private void switchToMainActivity(boolean exists){

        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        switchActivityIntent.putExtra("logged in", exists);
        startActivity(switchActivityIntent);
    }
}
