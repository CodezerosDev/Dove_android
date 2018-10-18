package com.darpysolutions.dove.Wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.darpysolutions.dove.R;

public class WalletDeleteActivity extends AppCompatActivity {
    Button btnDeleteWallet, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_delete);
        btnDeleteWallet = findViewById(R.id.btnDeleteWallet);
        btnCancel = findViewById(R.id.btnCancel);

        btnDeleteWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
