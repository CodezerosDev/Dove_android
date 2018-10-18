package com.darpysolutions.dove;

import android.app.Service;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.Utils.NetworkStatsHelper;
import com.darpysolutions.Utils.Utils;
import com.darpysolutions.dove.Dashboard.SalePurchaseActivity;
import com.darpysolutions.dove.NetUtils.PrefUtilities;
import com.darpysolutions.dove.wifi.WifiUtils;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class LoggingService extends Service {

    Handler handler;
    Runnable runnable;
    Context mContext;

    long lastKnownBytes = 0;
    long totalAllowedBytes = 0;
    long dataChanged = 0;

    String vol = "";

    public LoggingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        lastKnownBytes = intent.getLongExtra("lastKnowData", 0);
        vol = intent.getStringExtra("vol");

        if (vol.contains("M"))
            totalAllowedBytes = 1048576 * Integer.parseInt(vol.replace(" M", ""));
        else
            totalAllowedBytes = 1073741824 * Integer.parseInt(vol.replace(" G", ""));

        setData();
        return super.onStartCommand(intent, flags, startId);
    }

    private void setData() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) mContext.getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager);
            dataChanged = SalePurchaseActivity.getNetworkStatsAll(networkStatsHelper, mContext) - lastKnownBytes;
        } else
            dataChanged = SalePurchaseActivity.getNetworkStatsAll() - lastKnownBytes;


        sendBroadcast(new Intent(Constants.BROADCAST_SEND_DATA_UPDATES).putExtra("dataChanged", Utils.getFileSizeInDouble(dataChanged, vol.contains("M"))));
        if (handler == null)
            handler = new Handler();
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    setData();
                }
            };
        }
        handler.postDelayed(runnable, 1000);
        if (dataChanged > totalAllowedBytes) {
            handler.removeCallbacks(runnable);
            new WifiUtils(mContext).stopHotSpot();
            stopSelf();
        }
    }


    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
