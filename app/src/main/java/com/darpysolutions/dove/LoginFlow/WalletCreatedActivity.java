package com.darpysolutions.dove.LoginFlow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.darpysolutions.dove.R;

public class WalletCreatedActivity extends AppCompatActivity {
    Button btnContinue, btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_walletcreated);

        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(WalletCreatedActivity.this, SetupPinActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
