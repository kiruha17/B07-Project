package com.example.b07group57;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView, conPasswordTextView, nameTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page_fragment);

        mAuth = FirebaseAuth.getInstance();

        //Initialize fields
        emailTextView = findViewById(R.id.email_input);
        passwordTextView = findViewById(R.id.password_input);
        conPasswordTextView = findViewById(R.id.conpassword);
        nameTextView = findViewById(R.id.user_name); //Send to user class to store data later
        Button regBtn = findViewById(R.id.register);

        regBtn.onClickListener(new View.onClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

    }

    private void registerNewUser(){
        String email, password, conPassword, name;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        conPassword = conPasswordTextView.getText().toString();
        name = nameTextView.getText().toString();

        //Validations for input email and password
        if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),
                    "Please enter a email!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),
                    "Please enter a password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(conPassword)){
            Toast.makeText(getApplicationContext(),
                    "Please confirm your password",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(name)){
            Toast.makeText(getApplicationContext(),
                    "Please enter your name",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(conPassword)){
            Toast.makeText(getApplicationContext(),
                    "Passwords do not match",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Registration is Successful
                    Toast.makeText(getApplicationContext(),
                            "Registration successful", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SignUpActivity.this, HomeFragment.this); //Home
                    startActivity(intent);

                }
                else {
                    //Registration Failed
                    Toast.makeText(getApplicationContext(),
                            "Registration failed" + "Please try again later",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
