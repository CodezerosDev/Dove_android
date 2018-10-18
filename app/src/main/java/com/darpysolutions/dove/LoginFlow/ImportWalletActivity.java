package com.darpysolutions.dove.LoginFlow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.darpysolutions.dove.R;

public class ImportWalletActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnCancel, btnScan, btnContinue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importwallet);
        btnCancel = findViewById(R.id.btnCancel);
        btnScan = findViewById(R.id.btnScan);
        btnContinue = findViewById(R.id.btnContinue);

        btnCancel.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnScan:
                Log.e("Clicked", "Scan");
                startWalletCreateActivity();
                break;
            case R.id.btnContinue:
                Log.e("Clicked", "Continue");
                startWalletCreateActivity();
                break;
            case R.id.btnCancel:
                Log.e("Clicked", "Cancel");
                finish();
                break;

        }
    }

    private void startWalletCreateActivity() {
        Intent myIntent = new Intent(ImportWalletActivity.this, WalletCreatedActivity.class);
        startActivity(myIntent);
    }
}
