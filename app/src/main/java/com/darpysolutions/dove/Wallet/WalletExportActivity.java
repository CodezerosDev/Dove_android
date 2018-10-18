package com.darpysolutions.dove.Wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.darpysolutions.dove.R;

public class WalletExportActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCopyAddress, btnClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletexport);
        btnCopyAddress = findViewById(R.id.btnCopyAddress);
        btnClose = findViewById(R.id.btnClose);

        btnClose.setOnClickListener(this);
        btnCopyAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCopyAddress:
                finish();
                break;
            case R.id.btnClose:
                finish();
                break;
        }
    }
}
