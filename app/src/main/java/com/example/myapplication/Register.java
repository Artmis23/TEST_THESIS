package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    TextView register;
    EditText name, phone , email, pass1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.edt_name);
        email = findViewById(R.id.edt_email);
        phone = findViewById(R.id.edt_phone);
        pass1 = findViewById(R.id.edt_pass1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String ten = name.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String sdt = phone.getText().toString().trim();
        String mk = pass1.getText().toString().trim();

        if (ten.isEmpty())
        {
            name.setError("Name is required!");
            name.requestFocus();
            return;
        }
        if (sdt.isEmpty())
        {
            phone.setError("Name is required!");
            phone.requestFocus();
            return;
        }
        if (mail.isEmpty())
        {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if (mk.isEmpty())
        {
            pass1.setError("Pass is required!");
            pass1.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            email.setError("Please provide vail email!");
            email.requestFocus();
            return;
        }
        if (mk.length() < 8)
        {
            pass1.setError("Min pass length should be 8 characters!");
            pass1.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(mail,mk)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            User user = new User(ten,sdt,mail,mk);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(Register.this,"User has been registered successfully, please verify your email!",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(Register.this,"Fail to register! Try again!",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(Register.this,"Fail to register! Try again!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}