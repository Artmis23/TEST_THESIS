package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_password extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText email;
    TextView reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        reset = findViewById(R.id.reset);
        reset.setOnClickListener(this);

        email = findViewById(R.id.edt_email);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reset:
                Forgotpassword();
                break;
        }
    }

    private void Forgotpassword()
    {
        String mail = email.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            email.setError("Please provide vail email!");
            email.requestFocus();
            return;
        }
        if (mail.isEmpty())
        {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Forgot_password.this,"Check your email to reset password",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(Forgot_password.this,"Try again! Something wrong happened",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}