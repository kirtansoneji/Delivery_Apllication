package com.example.greatfresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String userId,email,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIn();
                finish();
            }
        },1000);
    }

    private void checkIn(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
      // SharedPreferences.Editor editor = sharedPreferences.edit();
        userId = sharedPreferences.getString(Constant.USER_ID,"");
        email = sharedPreferences.getString(Constant.EMAIL,"");
        mobile = sharedPreferences.getString(Constant.MOBILE,"");
        if (TextUtils.isEmpty(userId)){
            Intent i = new Intent(this, Login.class);
            startActivity(i);
        }else {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
    }
}
