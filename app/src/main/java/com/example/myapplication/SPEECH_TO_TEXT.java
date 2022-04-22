package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SPEECH_TO_TEXT extends AppCompatActivity {

    EditText speak;
    ImageView mic;
    int value;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    Context context = SPEECH_TO_TEXT.this;

    private SpeechRecognizer speechRecognizer;
    AlertDialog.Builder alertSpeechDialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        speak = findViewById(R.id.speak);
        mic = findViewById(R.id.mic);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mic_listen();
            }
        });
    }

    private void mic_listen(){
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Listening.....");

        try {
            startActivityForResult(speechIntent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case REQ_CODE_SPEECH_INPUT: {
                    if(resultCode == RESULT_OK && data != null) {
                        List<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String text = result.get(0);
                        speak.setText(text);
                        if(text.contentEquals("bật đèn phòng khách")==true)
                        {
                            value = 1;
                            controldevice();
                            break;
                        }
                        else if(text.contentEquals("Tắt đèn phòng khách")==true){
                            value = 0;
                            controldevice();
                            break;
                        }
                        else{
                            speak.setText("ban da ra lenh sai, moi ban ra lenh lai");
                            break;
                        }
                    }
                    break;
                }
            }
    }

    private void controldevice()
    {
        //Toast.makeText(SPEECH_TO_TEXT.this, "Bạn đa tat den", Toast.LENGTH_SHORT).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("den/phongkhach");
        myRef.setValue(value);
    }

}