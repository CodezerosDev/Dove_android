package com.darpysolutions.dove.Wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.darpysolutions.dove.R;

public class WalletImportActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCancel, btnScan, btnContinue;
    EditText etwalletName;
    Button btnImportWallet, btnImportAgain;
    Button btnCancelAgain, btnScanAgain, btnPasteAgain;
    LinearLayout llWalletNotFound, llConfirmationBar, llBottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletimport);
        btnCancel = findViewById(R.id.btnCancel);
        btnScan = findViewById(R.id.btnScan);
        btnContinue = findViewById(R.id.btnContinue);

        etwalletName = findViewById(R.id.etwalletName);

        btnImportWallet = findViewById(R.id.btnImportWallet);
        btnImportAgain = findViewById(R.id.btnImportAgain);

        btnCancelAgain = findViewById(R.id.btnCancelAgain);
        btnScanAgain = findViewById(R.id.btnScanAgain);
        btnPasteAgain = findViewById(R.id.btnPasteAgain);

        llWalletNotFound = findViewById(R.id.llWalletNotFound);
        llConfirmationBar = findViewById(R.id.llConfirmationBar);
        llBottomBar = findViewById(R.id.llBottomBar);

        btnCancel.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        btnContinue.setOnClickListener(this);

        btnImportAgain.setOnClickListener(this);
        btnImportWallet.setOnClickListener(this);

        btnCancelAgain.setOnClickListener(this);
        btnScanAgain.setOnClickListener(this);
        btnPasteAgain.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (llBottomBar.getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else if (llConfirmationBar.getVisibility() == View.VISIBLE) {
            llConfirmationBar.setVisibility(View.GONE);
            llBottomBar.setVisibility(View.VISIBLE);
        } else if (llWalletNotFound.getVisibility() == View.VISIBLE) {
            llWalletNotFound.setVisibility(View.GONE);
            llConfirmationBar.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnScan:
                llConfirmationBar.setVisibility(View.VISIBLE);
                llBottomBar.setVisibility(View.GONE);
                break;
            case R.id.btnContinue:
                llConfirmationBar.setVisibility(View.VISIBLE);
                llBottomBar.setVisibility(View.GONE);
                break;
            case R.id.btnImportWallet:
                llConfirmationBar.setVisibility(View.GONE);
                llWalletNotFound.setVisibility(View.VISIBLE);
                break;
            case R.id.btnImportAgain:
                llConfirmationBar.setVisibility(View.GONE);
                llWalletNotFound.setVisibility(View.VISIBLE);
                break;
            case R.id.btnCancelAgain:
                finish();
                break;
            case R.id.btnScanAgain:
                llWalletNotFound.setVisibility(View.GONE);
                llConfirmationBar.setVisibility(View.VISIBLE);
                break;
            case R.id.btnPasteAgain:
                llWalletNotFound.setVisibility(View.GONE);
                llConfirmationBar.setVisibility(View.VISIBLE);
                break;
        }
    }
}
