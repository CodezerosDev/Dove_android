package com.darpysolutions.dove.Activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.darpysolutions.Utils.NetworkStatsHelper;
import com.darpysolutions.Utils.TrafficStatsHelper;
import com.darpysolutions.dove.Fragments.GraphFragmentBuy;
import com.darpysolutions.dove.Fragments.GraphFragmentEnd;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.Wallet.WalletHomeActivity;

import static com.darpysolutions.Utils.Constants.BUY_FLAG;

public class PlanDashBoardOngoingPlan extends AppCompatActivity {

    FragmentManager fragmentManager;
    TextView tvWallet;
    TextView tvBuyData;
    TextView tvSaleData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_plandashboard);

        tvBuyData = (TextView) findViewById(R.id.tvBuyData);
        tvSaleData = (TextView) findViewById(R.id.tvSaleData);
        tvWallet = (TextView) findViewById(R.id.tvWallet);
        tvWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlanDashBoardOngoingPlan.this, WalletHomeActivity.class));
            }
        });

        fragmentManager = getSupportFragmentManager();

        tvBuyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvBuyData.setTextColor(Color.BLACK);
                tvBuyData.setBackgroundResource(R.drawable.rounded_primary);

                tvSaleData.setTextColor(Color.WHITE);
                tvSaleData.setBackgroundColor(Color.TRANSPARENT);

                GraphFragmentBuy graphFragment = new GraphFragmentBuy();

                Bundle args = new Bundle();
                args.putLong("lastKnowData", 50000);
                args.putString("vol", "10 M");
                graphFragment.setArguments(args);

                replaceFragment(R.id.flParentLayout, graphFragment, false);
            }
        });

        tvSaleData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvSaleData.setTextColor(Color.BLACK);
                tvSaleData.setBackgroundResource(R.drawable.rounded_primary);

                tvBuyData.setTextColor(Color.WHITE);
                tvBuyData.setBackgroundColor(Color.TRANSPARENT);

                GraphFragmentEnd graphFragment = new GraphFragmentEnd();

                Bundle args = new Bundle();
                args.putLong("lastKnowData", 50000);
                args.putString("vol", "10 M");
                graphFragment.setArguments(args);

                replaceFragment(R.id.flParentLayout, graphFragment, false);
            }
        });

        if(getIntent().getBooleanExtra(BUY_FLAG,false)){
            tvBuyData.callOnClick();
        }else {
            tvSaleData.callOnClick();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private long getNetworkStatsAll(NetworkStatsHelper networkStatsHelper, Context mContext) {
        long mobileWifiRx = networkStatsHelper.getAllRxBytesMobile(mContext) + networkStatsHelper.getAllRxBytesWifi();
        long mobileWifiTx = networkStatsHelper.getAllTxBytesMobile(mContext) + networkStatsHelper.getAllTxBytesWifi();
        return mobileWifiRx + mobileWifiTx;
    }

    public void replaceFragment(int contID, Fragment fragment, Boolean addBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(contID, fragment, fragment.getClass().getName());

        if (addBackStack) transaction.addToBackStack(fragment.getClass().getName());
        else fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        transaction.commitAllowingStateLoss();
    }

    static public long getNetworkStatsAll() {
        return TrafficStatsHelper.getAllRxBytes() +
                TrafficStatsHelper.getAllTxBytes();
    }
}
