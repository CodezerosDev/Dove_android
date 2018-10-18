package com.darpysolutions.dove.Dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.dove.Activity.BuyData;
import com.darpysolutions.dove.Activity.SellData;
import com.darpysolutions.dove.NetUtils.PermissionUtils;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.Wallet.WalletHomeActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    LinearLayout llBuyData, llSellData, llWallet;
    LocationManager mLocationManager;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);


        llBuyData = findViewById(R.id.llBuyData);
        llSellData = findViewById(R.id.llSellData);
        llWallet = findViewById(R.id.llWallet);

        llBuyData.setOnClickListener(this);
        llSellData.setOnClickListener(this);
        llWallet.setOnClickListener(this);

        CreateAndDisplayKeys();


    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void CreateAndDisplayKeys() {
        String vol = "200";
        String volUni = "M";  //M/G

        String duration = "30";
        String durationUni = "M"; //M/H/D

        String speed = "2.0";
        String speedUni = "K"; //K/M/G

        String price = "19";

        String generated = CreateKey(vol, volUni, duration, durationUni, speed, speedUni, price);


        Log.e("Generated :", "" + generated);

        decypherString(generated);
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

    private String CreateKey(String vol, String volUni, String duration, String durationUni, String speed, String speedUni, String price) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("code");
        stringBuilder.append('A');
        stringBuilder.append(vol);
        stringBuilder.append('P');
        stringBuilder.append(volUni);
        stringBuilder.append('A');
        stringBuilder.append(duration);
        stringBuilder.append('P');
        stringBuilder.append(durationUni);
        stringBuilder.append('A');
        stringBuilder.append(speed);
        stringBuilder.append('P');
        stringBuilder.append(speedUni);
        stringBuilder.append('A');
        stringBuilder.append(price);
        stringBuilder.append('A');
        stringBuilder.append("zero");

        return stringBuilder.toString();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000,
                50, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.e("Lat long", location.getLatitude() + " -- " + location.getLongitude());

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
        googleMap.setMyLocationEnabled(true);

//
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
//        if(PrefUtilities.getBoolean(this,Constants.SHOULD_SHOW_GRAPH)){
//            Intent myIntent1 = new Intent(DashboardActivity.this, SalePurchaseActivity.class);
//            startActivity(myIntent1);
//        }
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            Log.e("Perm:", "" + permission + " " + result);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            } else {
                Log.e("granted permissions :", permission);
            }
        }

        Log.e("missing permissions :", "" + missingPermissions.size());
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            Log.e("not empty", "permissions");
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            for (int i = 0; i < permissions.length; i++)
                Log.e("Asking", "" + permissions[i]);

//            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
            PermissionUtils.requestPermissions(DashboardActivity.this, permissions, REQUEST_CODE_ASK_PERMISSIONS);

        } else {
            Log.e("empty", "permissions");
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(getApplicationContext())) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));

                    startActivityForResult(intent, 200);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(DashboardActivity.this, "You need to enable permission of WRITE SYSTEM SETTINGS Manually", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
//                        finish();
                        return;
                    }
                }
                // all permissions were granted
                break;
        }
    }

    @Override
    public void onClick(View v) {
        //SalePurchaseActivity
        switch (v.getId()) {
            case R.id.llBuyData:
                startActivity(new Intent(DashboardActivity.this, BuyData.class));
                //startActivity(new Intent(DashboardActivity.this, SalePurchaseActivity.class).putExtra(Constants.BUY_FLAG, true));
                break;
            case R.id.llSellData:
                startActivity(new Intent(DashboardActivity.this, SellData.class));
                //startActivity(new Intent(DashboardActivity.this, SalePurchaseActivity.class).putExtra(Constants.BUY_FLAG, false));
                break;
            case R.id.llWallet:
                startActivity(new Intent(DashboardActivity.this, WalletHomeActivity.class));
                break;
        }
    }
}
