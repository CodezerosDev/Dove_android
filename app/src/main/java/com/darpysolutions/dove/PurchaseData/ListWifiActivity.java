package com.darpysolutions.dove.PurchaseData;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.dove.Manageplans_history.HistoryActivity;
import com.darpysolutions.dove.PurchaseData.Fragment.WifiListAdapter;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.wifi.ServerBean;
import com.darpysolutions.dove.wifi.WifiDataBean;
import com.darpysolutions.dove.wifi.WifiUtils;

import java.util.ArrayList;
import java.util.List;

public class ListWifiActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvWifis;
    ImageView ivBack;
    WifiManager wifiManager;
    ProgressDialog pd;
    ArrayList<WifiDataBean> listServer = new ArrayList<>();
    WifiListAdapter wifiListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listwifi);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        rvWifis = findViewById(R.id.rvWifis);
        listServer = new ArrayList<>();
        wifiListAdapter = new WifiListAdapter(ListWifiActivity.this, listServer);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListWifiActivity.this);
        rvWifis.setLayoutManager(mLayoutManager);
        rvWifis.setAdapter(wifiListAdapter);
        rvWifis.setItemAnimator(new DefaultItemAnimator());

        startWifiScanning();


    }

    private void startWifiScanning() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (mWifiScanReceiver != null)
            registerReceiver(mWifiScanReceiver,
                    new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        pd = new ProgressDialog(ListWifiActivity.this);
        pd.setMessage("Loading");
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWifiScanReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }

    public List<ScanResult> mScanResults = new ArrayList<>();
    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> tempResult;
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                tempResult = wifiManager.getScanResults();
                for (int i = 0; i < tempResult.size(); i++) {
                    Log.e("TAG", "" + tempResult.get(i).SSID + " " + tempResult.get(i).BSSID);
                    if (WifiUtils.CheckForAppWifi(tempResult.get(i).SSID)) {
//                        WifiDataBean sb = new WifiDataBean();
//                        sb.setServername(tempResult.get(i).SSID);
//                        sb.setType(Constants.HOTSPOT);
                        Boolean toAdd = true;

                        WifiDataBean sb = new WifiDataBean(tempResult.get(i).SSID);
                        for (int x = 0; x < listServer.size(); x++) {
                            if (listServer.get(x).getGeneratedString().equalsIgnoreCase(sb.getGeneratedString()))
                                toAdd = false;
                        }
                        if (toAdd) {
                          if(WifiUtils.CheckForAppWifi(sb.getGeneratedString()))
                            {
                                listServer.add(sb);
                                mScanResults.add(tempResult.get(i));
                            }
                        }
                        wifiListAdapter.notifyDataSetChanged();
                    }
                }
            }
            if (!isRefreshedAgain) {
                isRefreshedAgain = true;
                wifiManager.startScan();
            }
        }
    };
    Boolean isRefreshedAgain = false;

    public void ConnectTo(ScanResult scanResult,String vol) {
//        pd.show();
        Log.e("CONNECTING", "SSID:" + scanResult.SSID + " PASS:");
        String networkSSID = scanResult.SSID;
        String networkPass = "AAAAAAAAAA";// Constant.PASSWORD_VALUE + txtPassword;//getString(R.string.password);  ///Password

        final WifiConfiguration wifiConf = new WifiConfiguration();
        String mode = getSecurityMode(scanResult);
        //////////////////
        if (mode.equalsIgnoreCase("OPEN")) {
            wifiConf.SSID = "\"" + networkSSID + "\"";
            wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            int res = wifiManager.addNetwork(wifiConf);
            wifiManager.disconnect();
            wifiManager.enableNetwork(res, true);
            wifiManager.reconnect();
            wifiManager.setWifiEnabled(true);
            Log.e("COnnect", "TO:" + wifiConf.SSID);
            startConnected(vol);
//            startMainActivity();

        } else if (mode.equalsIgnoreCase("WEP")) {

            wifiConf.SSID = "\"" + networkSSID + "\"";
            wifiConf.wepKeys[0] = "\"" + networkPass + "\"";
            wifiConf.wepTxKeyIndex = 0;
            wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            int res = wifiManager.addNetwork(wifiConf);
            wifiManager.disconnect();
            wifiManager.enableNetwork(res, true);
            wifiManager.reconnect();
            wifiManager.setWifiEnabled(true);
            Log.e("COnnect", "TO:" + wifiConf.SSID);
//            startMainActivity();
            startConnected(vol);
        } else {
            wifiConf.SSID = "\"" + networkSSID + "\"";
            wifiConf.preSharedKey = "\"" + networkPass + "\"";
            wifiConf.hiddenSSID = false;
            wifiConf.status = WifiConfiguration.Status.ENABLED;
            wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            int res = wifiManager.addNetwork(wifiConf);
            wifiManager.disconnect();
            wifiManager.enableNetwork(res, true);
            wifiManager.reconnect();
            wifiManager.saveConfiguration();
            wifiManager.setWifiEnabled(true);
            Log.e("COnnect", "TO:" + wifiConf.SSID);
//            startMainActivity();
            startConnected(vol);
        }
    }

    private void startConnected(String vol) {
        Intent data = new Intent();
        data.putExtra("vol",vol);
        setResult(RESULT_OK, data);
        finish();
    }

    public String getSecurityMode(ScanResult scanResult) {
        final String cap = scanResult.capabilities;
        final String[] modes = {"WPA", "EAP", "WEP"};
        for (int i = modes.length - 1; i >= 0; i--) {
            if (cap.contains(modes[i])) {
                return modes[i];
            }
        }
        return "OPEN";
    }
}
