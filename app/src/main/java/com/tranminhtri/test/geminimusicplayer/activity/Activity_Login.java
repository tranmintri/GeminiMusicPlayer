package com.tranminhtri.test.geminimusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.models.User;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Activity_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         Button btnSignIn_Free;
         Button btnSignup;
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        String username = sharedPreferences.getString("username", "");
        User user = new User(username);
        if(isLoggedIn){
            Intent intent = new Intent(Activity_Login.this, Activity_Home.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        btnSignIn_Free = findViewById(R.id.btnSignInFree);

        btnSignup = findViewById(R.id.btnSignUp);


        btnSignIn_Free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_SignIn_Free.class);
                startActivity(intent);

            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Activity_Login.this, Activity_Signup.class);
                startActivity(intent);

            }
        });

    }
}