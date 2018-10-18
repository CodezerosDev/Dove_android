package com.darpysolutions.dove.LoginFlow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.darpysolutions.Utils.Apis;
import com.darpysolutions.Utils.Constants;
import com.darpysolutions.dove.NetUtils.OnTaskCompleted;
import com.darpysolutions.dove.NetUtils.PermissionUtils;
import com.darpysolutions.dove.NetUtils.PostAsynchTask;
import com.darpysolutions.dove.NetUtils.PrefUtilities;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.countrypicker.Country;
import com.darpysolutions.dove.countrypicker.CountryPickerCallbacks;
import com.darpysolutions.dove.countrypicker.CountryPickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PhoneRegisterActivity extends AppCompatActivity {
    ImageView ivCountry;
    EditText etPhoneNumber;
    TextView btnContinue;
    TextView tvCode;
    Country country;
    int flagResId;

    // permissions request code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneregister);

        checkPermissions();

        ivCountry = findViewById(R.id.ivCountry);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        tvCode = findViewById(R.id.tvCode);
        final CountryPickerDialog countryPicker =
                new CountryPickerDialog(this, new CountryPickerCallbacks() {
                    @Override
                    public void onCountrySelected(Country country, int flagResId) {
                        PhoneRegisterActivity.this.flagResId = flagResId;
                        PhoneRegisterActivity.this.country = country;

                        tvCode.setText(country.getDialingCode());
                        ivCountry.setImageResource(flagResId);
//                        etPhoneNumber.setSelection(etPhoneNumber.getText().length());
                    }
                }, "US", false);

        ivCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.show();
            }
        });

        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhoneNumber.getText().toString().length() <= 4) {
                    Toast.makeText(PhoneRegisterActivity.this, "Enter Phone Number", Toast.LENGTH_LONG).show();
                    return;
                }

                if (com.darpysolutions.Utils.Utils.isNetworkConnected(PhoneRegisterActivity.this)) {
                    CallRegisterApi();
                } else {
                    Toast.makeText(PhoneRegisterActivity.this, "Requires active internet connection to proceed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void CallRegisterApi() {
        final String phoneNumber = tvCode.getText() + "" + etPhoneNumber.getText().toString();
        String deviceToken = Constants.FCM_ID;

        if (com.darpysolutions.Utils.Utils.isNetworkConnected(PhoneRegisterActivity.this)) {
            final ProgressDialog progressDialog = new ProgressDialog(PhoneRegisterActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            HashMap<String, String> toSendMap = new HashMap<>();
            toSendMap.put(Constants.PHONE_NUMBER, phoneNumber);
            toSendMap.put(Constants.APP_DEVICE, "2");
            toSendMap.put(Constants.DEVICE_ID, deviceToken);
            new PostAsynchTask(toSendMap, Apis.REGISTER_MOBILE, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(String result) {

//                    {"status_code":1,"message":"User register successfully.","data":{"user_verification_status":"0","user_id":92,"verification_code":"6996","auth_token":"839977422"}}

                    JSONObject resultObject = null;
                    try {
                        progressDialog.dismiss();
                        resultObject = new JSONObject(result);
                        if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            JSONObject dataObject = resultObject.getJSONObject(Constants.DATA);

                            PrefUtilities.saveString(PhoneRegisterActivity.this, Constants.VERIFICATION_STATUS, dataObject.getString(Constants.VERIFICATION_STATUS));
                            PrefUtilities.saveInt(PhoneRegisterActivity.this, Constants.USER_ID, dataObject.getInt(Constants.USER_ID));
                            PrefUtilities.saveString(PhoneRegisterActivity.this, Constants.VERIFICATION_CODE, dataObject.getString(Constants.VERIFICATION_CODE));
                            PrefUtilities.saveString(PhoneRegisterActivity.this, Constants.AUTH_TOKEN, dataObject.getString(Constants.AUTH_TOKEN));
                            PrefUtilities.saveString(PhoneRegisterActivity.this, Constants.PHONE_NUMBER, phoneNumber);
                            Intent myIntent = new Intent(PhoneRegisterActivity.this, VerificationCodeActivity.class);
                            myIntent.putExtra(Constants.FLAG_RES_ID, PhoneRegisterActivity.this.flagResId);
                            myIntent.putExtra(Constants.PHONE_NUMBER, etPhoneNumber.getText().toString());
                            myIntent.putExtra(Constants.COUNTRY, PhoneRegisterActivity.this.country);
                            startActivity(myIntent);
                            finish();
                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("0")) {

                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(PhoneRegisterActivity.this, msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(PhoneRegisterActivity.this, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }).execute();
        } else {
            Toast.makeText(PhoneRegisterActivity.this, "Please Connect to Active Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    ///////////Permissions Logic
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
            PermissionUtils.requestPermissions(PhoneRegisterActivity.this, permissions, REQUEST_CODE_ASK_PERMISSIONS);

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
            Toast.makeText(PhoneRegisterActivity.this, "You need to enable permission of WRITE SYSTEM SETTINGS Manually", Toast.LENGTH_LONG).show();
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

}
