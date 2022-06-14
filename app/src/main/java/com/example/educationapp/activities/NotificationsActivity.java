package com.example.educationapp.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.educationapp.R;
import com.example.educationapp.database.AuthenticationDBHandler;
import com.example.educationapp.database.StatisticsDBHandler;
import com.example.educationapp.model.Statistics;

import java.util.Calendar;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private boolean checked = false;
    AuthenticationDBHandler authenticationDBHandler = new AuthenticationDBHandler(this,null,null,2);


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        NumberPicker hour = findViewById(R.id.hour_picker);
        NumberPicker minute = findViewById(R.id.minutes_picker);

        hour.setMinValue(0);
        hour.setMaxValue(23);

        minute.setMinValue(0);
        minute.setMaxValue(59);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin",MODE_PRIVATE);
        String mail = sharedPreferences.getString("mail",null);

        Button confirm = findViewById(R.id.confirm_notifications);
        confirm.setOnClickListener(v -> {
            onTurnOnNotifications(hour.getValue(),minute.getValue());
            switchBackToMain(mail);
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchOnOff = findViewById(R.id.switch_notifications);
        switchOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                checked = true;
            }
        });

        authenticationDBHandler.getNotifications(sharedPreferences.getString("uid", null), new StatisticsDBHandler.DataStatus() {
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

            }

            @Override
            public void notificationLoaded(String notification) {

                String[] times = notification.split(":");
                hour.setValue(Integer.parseInt(times[0]));
                minute.setValue(Integer.parseInt(times[1]));
                switchOnOff.setChecked(true);
            }
        });

    }

    private void switchBackToMain(String mail){

        Intent switchActivityIntent;
        switchActivityIntent = new Intent(this,MainActivity.class);
        switchActivityIntent.putExtra("mail", mail);

        startActivity(switchActivityIntent);
    }

    public void onTurnOnNotifications(int hour, int minute) {

        notificationChannel();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);

        if(Calendar.getInstance().after(calendar)){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }


        Intent intent = new Intent(this,MemoBroadcast.class);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }


        String time = hour + ":" + minute;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin",MODE_PRIVATE);
        authenticationDBHandler.setNotifications(time, sharedPreferences.getString("uid",null));

    }

    /**
     * Calling method for saving and creating
     * in system new notification channel
     */
    private void notificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String id = "001";
            String name = "Channel_001";
            String description = "Channel for daily notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(id,name,importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }


}
