package com.darpysolutions.dove.wifi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static android.content.Context.WIFI_SERVICE;

public class WifiUtils {

    Context mContext;

    public WifiUtils(Context c) {
        mContext = c;
    }

    public void startHotSpot(String n, String p) {
        new HotspotStart(mContext, n, p).execute();
    }

    public void stopHotSpot() {
        new HotspotStop(mContext).execute();
    }

    public static boolean CheckForAppWifi(String ssid) {
        if (ssid.contains("code")) {
            String[] decyp = ssid.split("A");
            if (decyp.length == 4)
                return true;
        }
        return false;
    }


    public class HotspotStart extends AsyncTask<Void, Void, Void> {
        String hsptName, hsptPassword;
        ProgressDialog pd;
        Context mContext;

        private HotspotStart(Context ctx, String name, String pass) {
            mContext = ctx;
            pd = new ProgressDialog(mContext);
            pd.setTitle("Loading...");
            pd.show();

            hsptName = name;
            hsptPassword = pass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("Start", "Do in background");
            startHotspot(hsptName, hsptPassword, mContext);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            Toast.makeText(mContext, "Hotspot will be activated in few moments", Toast.LENGTH_LONG).show();
        }
    }


    public class HotspotStop extends AsyncTask<Void, Void, Void> {
        Context mContext;

        private HotspotStop(Context ctx) {
            mContext = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("Start", "Do in background");
            stopHotspot(mContext);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(mContext, "Hotspot will be deactivated in few moments", Toast.LENGTH_LONG).show();
        }
    }

    private void startHotspot(String Name, String Password, Context mContext) {
        Log.e("FUNCTION", "StartHotspot: " + Name + " Pass:" + Password);
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();
        boolean methodFound = false;
        for (Method method : wmMethods) {
            Log.e("Master", "Method " + method.getName());
            if (method.getName().equals("setWifiApEnabled")) {
                methodFound = true;
                WifiConfiguration netConfig = new WifiConfiguration();
                netConfig.SSID = Name;
                netConfig.preSharedKey = Password;
                netConfig.hiddenSSID = false;
//                netConfig.
                netConfig.status = WifiConfiguration.Status.ENABLED;
                netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                netConfig.allowedKeyManagement.set(4);
                netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//                netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//                netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

//                netConfig.
//                netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
//                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//                netConfig.status = WifiConfiguration.Status.ENABLED;
//                netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//                netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//                netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);


                try {
                    boolean apstatus = (Boolean) method.invoke(wifiManager, netConfig, true);

                    if (apstatus) {
                        Log.d("HotSpot", "Access Point created");
                    } else {
                        Log.d("HotSpot", "Access Point creation failed");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!methodFound) {
            Log.d("Server Creation", "cannot configure an access point");
        }
    }

    private void stopHotspot(Context mContext) {
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();
        boolean methodFound = false;
        for (Method method : wmMethods) {
            Log.e("Master", "Method " + method.getName());
            if (method.getName().equals("setWifiApEnabled")) {
                methodFound = true;
                WifiConfiguration netConfig = new WifiConfiguration();
                try {
                    boolean apstatus = (Boolean) method.invoke(wifiManager, netConfig, false);
                    if (apstatus) {
                        Log.d("HotSpot", "Access Point created");
                    } else {
                        Log.d("HotSpot", "Access Point creation failed");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!methodFound) {
            Log.d("Server Creation", "cannot configure an access point");
        }
    }


}
