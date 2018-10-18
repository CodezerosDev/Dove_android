package com.darpysolutions.dove.Dashboard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.Utils.NetworkStatsHelper;
import com.darpysolutions.Utils.TrafficStatsHelper;
import com.darpysolutions.dove.PurchaseData.Fragment.GraphFragment;
import com.darpysolutions.dove.PurchaseData.Fragment.PurchaseFragment;
import com.darpysolutions.dove.PurchaseData.ListWifiActivity;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.SalesData.Fragment.SalesFragment;
import com.darpysolutions.dove.Wallet.WalletHomeActivity;
import com.darpysolutions.dove.wifi.WifiUtils;

public class SalePurchaseActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvBuyData, tvSaleData, tvWallet;
    FrameLayout flParentLayout, flWallet;
    FragmentManager fragmentManager;
    ImageView ivSettings;
    private static final int READ_PHONE_STATE_REQUEST = 37;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salepurchasehome);

        tvBuyData = findViewById(R.id.tvBuyData);
        tvSaleData = findViewById(R.id.tvSaleData);
        tvBuyData.setOnClickListener(this);
        tvSaleData.setOnClickListener(this);

        ivSettings = findViewById(R.id.ivSettings);
        ivSettings.setOnClickListener(this);
        flParentLayout = findViewById(R.id.flParentLayout);
        tvWallet = findViewById(R.id.tvWallet);
        tvWallet.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();

        boolean Buyflag = getIntent().getBooleanExtra(Constants.BUY_FLAG, true);
        if (Buyflag) {
            tvBuyData.performClick();
        } else {
            tvSaleData.performClick();
        }
    }

    String currentFragment = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuyData:

                if (tvBuyData.getText().toString().equalsIgnoreCase("End Plan")) {
                    tvBuyData.setText("Buy Data");
                    tvBuyData.setTextColor(getResources().getColor(R.color.coolGrayColor));
                    tvSaleData.setTextColor(getResources().getColor(R.color.white));
                    tvBuyData.setBackgroundResource(R.drawable.white_btn_bg);
                    tvSaleData.setBackgroundResource(R.drawable.yellow_btn_bg);

                    replaceFragment(R.id.flParentLayout, new SalesFragment(), false);
                } else {
                    tvBuyData.setTextColor(getResources().getColor(R.color.white));
                    tvSaleData.setTextColor(getResources().getColor(R.color.coolGrayColor));
                    tvBuyData.setBackgroundResource(R.drawable.blue_btn_bg);
                    tvSaleData.setBackgroundResource(R.drawable.white_btn_bg);

                    replaceFragment(R.id.flParentLayout, new PurchaseFragment(), false);
                }
                break;
            case R.id.tvSaleData:
                Log.e("Click", "Saledata Clicked");
                if (tvSaleData.getText().toString().equalsIgnoreCase("End Plan")) {
                    tvSaleData.setText("Sale Data");
                    tvBuyData.setTextColor(getResources().getColor(R.color.white));
                    tvSaleData.setTextColor(getResources().getColor(R.color.coolGrayColor));
                    tvBuyData.setBackgroundResource(R.drawable.blue_btn_bg);
                    tvSaleData.setBackgroundResource(R.drawable.white_btn_bg);
                    replaceFragment(R.id.flParentLayout, new PurchaseFragment(), false);
                } else {
                    tvBuyData.setTextColor(getResources().getColor(R.color.coolGrayColor));
                    tvSaleData.setTextColor(getResources().getColor(R.color.white));
                    tvBuyData.setBackgroundResource(R.drawable.white_btn_bg);
                    tvSaleData.setBackgroundResource(R.drawable.yellow_btn_bg);

                    replaceFragment(R.id.flParentLayout, new SalesFragment(), false);
                }
                break;
            case R.id.tvWallet:
                Intent myIntent = new Intent(SalePurchaseActivity.this, WalletHomeActivity.class);
                startActivity(myIntent);
                break;
        }
    }

    public void replaceFragment(int contID, Fragment fragment, Boolean addBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(contID, fragment, fragment.getClass().getName());

        if (addBackStack) transaction.addToBackStack(fragment.getClass().getName());
        else fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        transaction.commitAllowingStateLoss();
        currentFragment = fragment.getClass().getName();
    }


    private void decypherString(String generated) {
        String[] decyp = generated.split("A");
        Log.e("Size", "" + decyp.length);
        for (int i = 0; i < decyp.length; i++) {
            Log.e("D", "" + decyp[i]);
        }

        if (decyp[0].equalsIgnoreCase("code") && decyp[5].equalsIgnoreCase("zero")) {

        }
    }

    public void showGraphFragment(String s, String generated) {

        GraphFragment graphFragment = new GraphFragment();

        Bundle args = new Bundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager);
            args.putLong("lastKnowData", getNetworkStatsAll(networkStatsHelper, SalePurchaseActivity.this));
        } else
            args.putLong("lastKnowData", getNetworkStatsAll());
        args.putString("vol", generated);

        graphFragment.setArguments(args);

        replaceFragment(R.id.flParentLayout, graphFragment, false);
        if (s.equalsIgnoreCase("purchase")) {
            tvSaleData.setText("End Plan");
            tvSaleData.setBackgroundResource(R.drawable.yellow_btn_bg);
            tvSaleData.setTextColor(getResources().getColor(R.color.white));
        } else {
            tvBuyData.setText("End Plan");
            tvBuyData.setBackgroundResource(R.drawable.blue_btn_bg);
            tvBuyData.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void ListAvailablePlans() {
        Intent myIntent = new Intent(SalePurchaseActivity.this, ListWifiActivity.class);
        startActivityForResult(myIntent, 1001);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            Log.e("Ho", "Gaya");
            showGraphFragment("purchase", data.getStringExtra("vol"));
        }
    }

    public void saleData(String generated, String vol) {
        WifiUtils wifiUtils = new WifiUtils(SalePurchaseActivity.this);
        wifiUtils.startHotSpot(generated, "AAAAAAAAAA");
        showGraphFragment("sale", vol);
    }


    private boolean hasPermissionToReadNetworkHistory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                getApplicationContext().getPackageName(),
                new AppOpsManager.OnOpChangedListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onOpChanged(String op, String packageName) {
                        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                android.os.Process.myUid(), getPackageName());
                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            return;
                        }
                        appOps.stopWatchingMode(this);
                    }
                });
        requestReadNetworkHistoryAccess();
        return false;
    }

    private boolean hasPermissionToReadPhoneStats() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void requestReadNetworkHistoryAccess() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    private void requestPermissions() {
        if (!hasPermissionToReadNetworkHistory()) {
            return;
        }
        if (!hasPermissionToReadPhoneStats()) {
            requestPhoneStateStats();
        }
    }

    private void requestPhoneStateStats() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_REQUEST);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissions();
    }


    @TargetApi(Build.VERSION_CODES.M)
    static public long getNetworkStatsAll(NetworkStatsHelper networkStatsHelper, Context mContext) {
        long mobileWifiRx = networkStatsHelper.getAllRxBytesMobile(mContext) + networkStatsHelper.getAllRxBytesWifi();
        long mobileWifiTx = networkStatsHelper.getAllTxBytesMobile(mContext) + networkStatsHelper.getAllTxBytesWifi();
        return mobileWifiRx + mobileWifiTx;
    }

    static public long getNetworkStatsAll() {
        return TrafficStatsHelper.getAllRxBytes() +
                TrafficStatsHelper.getAllTxBytes();
    }
}
