package com.darpysolutions.dove.LoginFlow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.dove.Dashboard.DashboardActivity;
import com.darpysolutions.dove.NetUtils.PrefUtilities;
import com.darpysolutions.dove.R;


public class WelcomeDoveActivity extends AppCompatActivity {


    TextView btnContinue;
    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_welcomedove);

        if (PrefUtilities.getBoolean(mContext, Constants.IS_LOGGED_IN)) {
            Intent myIntent = null;
            switch (PrefUtilities.getInt(mContext, Constants.LOGGED_IN_AFTER_STAGE)) {
                case Constants.LOGGED_IN_WALLET:
                    myIntent = new Intent(mContext, WelcomeWalletActivity.class);
                    break;
                case Constants.LOGGED_IN_PIN:
                    myIntent = new Intent(mContext, SetupPinActivity.class);
                    break;
                case Constants.LOGGED_IN_DASHBOARD:
                    myIntent = new Intent(mContext, DashboardActivity.class);
                    break;

            }
            if (myIntent != null)
                startActivity(myIntent);
            finish();
        }

        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, PhoneRegisterActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
}
