package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    private TextView register, login, forgot;
    private FirebaseAuth mAuth;
    private EditText edt_wlemail,edt_wlpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        register = findViewById(R.id.wlregister);
        register.setOnClickListener(this);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        forgot = findViewById(R.id.forgot);
        forgot.setOnClickListener(this);

        edt_wlemail = findViewById(R.id.edt_wlemail);
        edt_wlpass = findViewById(R.id.edt_wlpass);

        mAuth = FirebaseAuth.getInstance();

        //getUserData();
    }
//
//    private void getUserData() {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User")
//                .child(mAuth.getUid());
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.exists()) {
//
//                    User userModel = snapshot.getValue(User.class);
//                    edt_wlemail.setText(userModel.getEmail());
//                    edt_wlpass.setText(userModel.getPassword());
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("TAG", "Failed to read value.", error.toException());
//            }
//        });
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wlregister:
                startActivity(new Intent(this, Register.class));
                break; 
                
            case R.id.login:
                Login();
                break;
            case R.id.forgot:
                startActivity(new Intent(this, Forgot_password.class));
                break;
        }

    }


    private void Login() {
        String mail = edt_wlemail.getText().toString().trim();
        String mk = edt_wlpass.getText().toString().trim();

        if (mail.isEmpty())
        {
            edt_wlemail.setError("Email is required!");
            edt_wlemail.requestFocus();
            return;
        }
        if (mk.isEmpty())
        {
            edt_wlpass.setError("Password is required!");
            edt_wlpass.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            edt_wlemail.setError("Please provide vail email!");
            edt_wlemail.requestFocus();
            return;
        }
        if (mk.length() < 8)
        {
            edt_wlpass.setError("Min pass length should be 8 characters!");
            edt_wlpass.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(mail,mk).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()) {
                        startActivity(new Intent(Welcome.this, MainActivity.class));
                        Toast.makeText(Welcome.this,"Login successfully",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(Welcome.this,"Please let verify user!",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(Welcome.this,"Failed to login! Please check your credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}