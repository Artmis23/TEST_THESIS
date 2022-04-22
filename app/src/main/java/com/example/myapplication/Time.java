package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class Time extends AppCompatActivity {
    TimePicker timePicker;
    TextView textViewTime;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    TextView button_time;

    // Change this value and run the application again.
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        textViewTime = (TextView) this.findViewById(R.id.textView_time);
        timePicker = (TimePicker) this.findViewById(R.id.timePicker);
        calendar = Calendar.getInstance();
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(Time.this, AlarmReceiver.class);
        button_time = findViewById(R.id.button_time);
        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());

                int gio = timePicker.getCurrentHour();
                int phut = timePicker.getCurrentMinute();

                String hours = String.valueOf(gio);
                String minute = String.valueOf(phut);

                if(gio > 12){
                    hours = String.valueOf(gio-12);
                }
                if(phut<10)
                {
                    minute = "0"+String.valueOf(phut);
                }

                pendingIntent = PendingIntent.getBroadcast(
                        Time.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT
                );
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

                textViewTime.setText(hours +":"+minute);
            }
        });
    }

}