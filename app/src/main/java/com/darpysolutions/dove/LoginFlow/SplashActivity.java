package com.darpysolutions.dove.LoginFlow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.darpysolutions.dove.R;

public class SplashActivity extends AppCompatActivity {

    TextView btnContinue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SplashActivity.this, WelcomeDoveActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
}
