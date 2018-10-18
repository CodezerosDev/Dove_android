package com.darpysolutions.dove.LoginFlow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.darpysolutions.Utils.Apis;
import com.darpysolutions.Utils.Constants;
import com.darpysolutions.Utils.Utils;
import com.darpysolutions.dove.NetUtils.ImageUploadTask;
import com.darpysolutions.dove.NetUtils.OnTaskCompleted;
import com.darpysolutions.dove.NetUtils.PostAsynchTask;
import com.darpysolutions.dove.NetUtils.PrefUtilities;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.Wallet.SacnQrActivity;
import com.darpysolutions.dove.Wallet.WalletModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;


public class WelcomeWalletActivity extends AppCompatActivity implements View.OnClickListener {

    private final int READ_REQUEST_CODE = 123;
    private final int READ_QR_CODE = 124;


    Context mContext;

    TextView  btnCreate;
    Button createButton;
    EditText et_password;
    Button cancelBtn;
    EditText et_paste;
    TextView tv_file_path;
    TextView btnImportExistingOne;
    TextView tv_password_title;

    BottomSheetBehavior sheetBehavior;
    LinearLayout relBottom;

    LinearLayout relative_import;
    BottomSheetBehavior sheetBehaviorImport;


    Button btnContinueImport, btnScan, btnSelectFile, btnCancelImport;

    BottomSheetBehavior sheetBehaviorImportName;
    LinearLayout relative_import_name;

    EditText et_wallet_name;
    TextView tv_ether_balance;
    TextView tv_dove_balance;
    Button btnImportConfirmation, btnCancelConfirmation;

    String walletJsonMain = "";
    WalletModel walletModelMain = null;
    float BalanceMain = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_welcomewallet);

        btnImportExistingOne = findViewById(R.id.btnImportExistingOne);
        btnCreate = findViewById(R.id.btnCreate);
        createButton = findViewById(R.id.btnCreatePassword);
        et_password = findViewById(R.id.et_password);
        et_paste = findViewById(R.id.et_paste);
        cancelBtn = findViewById(R.id.btnCancelPassword);
        tv_file_path = findViewById(R.id.tv_file_path);
        tv_password_title = findViewById(R.id.tv_password_title);

        btnContinueImport = findViewById(R.id.btnContinueImport);
        btnScan = findViewById(R.id.btnScan);
        btnSelectFile = findViewById(R.id.btnSelectFile);
        btnCancelImport = findViewById(R.id.btnCancelImport);

        et_wallet_name = findViewById(R.id.et_wallet_name);
        tv_dove_balance = findViewById(R.id.tv_dove_balance);
        tv_ether_balance = findViewById(R.id.tv_ether_balance);

        btnImportConfirmation = findViewById(R.id.btnImportConfirmation);
        btnCancelConfirmation = findViewById(R.id.btnCancelConfirmation);


        relBottom = findViewById(R.id.relative_bottom);
        sheetBehavior = BottomSheetBehavior.from(relBottom);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        relBottom.bringToFront();
//                        relBottom.setBackgroundColor(getResources().getColor(R.color.trans_white));
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        relBottom.bringToFront();
//                        relBottom.setBackgroundColor(getResources().getColor(R.color.trans_white));
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                relBottom.bringToFront();
            }
        });

        relative_import = findViewById(R.id.relative_import);
        sheetBehaviorImport = BottomSheetBehavior.from(relative_import);
        sheetBehaviorImport.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        relative_import.bringToFront();
//                        relBottom.setBackgroundColor(getResources().getColor(R.color.trans_white));
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        relative_import.bringToFront();
//                        relBottom.setBackgroundColor(getResources().getColor(R.color.trans_white));
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                relative_import.bringToFront();
            }
        });


        relative_import_name = findViewById(R.id.relative_import_name);
        sheetBehaviorImportName = BottomSheetBehavior.from(relative_import_name);
        sheetBehaviorImportName.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        relative_import_name.bringToFront();
                        relative_import_name.setBackgroundColor(getResources().getColor(R.color.trans_white));
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        relative_import_name.bringToFront();
                        relative_import_name.setBackgroundColor(getResources().getColor(R.color.trans_white));
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                relative_import_name.bringToFront();
            }
        });

        btnCreate.setOnClickListener(this);
        btnImportExistingOne.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        createButton.setOnClickListener(this);

        btnContinueImport.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        btnSelectFile.setOnClickListener(this);
        btnCancelImport.setOnClickListener(this);

        btnCancelConfirmation.setOnClickListener(this);
        btnImportConfirmation.setOnClickListener(this);

    }

    public void sendFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
//                String uriString = uri.getPath();
//                File file = new File(uriString);
                String path = null;
                try {
                    path = Utils.getFilePath(mContext, uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
//                String FilePath = data.getData().getPath();
                tv_password_title.setText("Import Wallet");
                createButton.setText("Import");
                tv_file_path.setVisibility(View.VISIBLE);
                et_password.setVisibility(View.VISIBLE);
                et_paste.setVisibility(View.GONE);
                tv_file_path.setText(path);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_COLLAPSED);

            } else {
                Toast.makeText(mContext, "File select cancelled", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == READ_QR_CODE) {
            if (resultCode == RESULT_OK) {

                tv_password_title.setText("Import Wallet");
                createButton.setText("Import");
                tv_file_path.setVisibility(View.GONE);
                et_password.setVisibility(View.GONE);
                et_paste.setVisibility(View.VISIBLE);
                et_paste.setText(data.getStringExtra(Constants.PRIVATE_KEY));
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnContinueImport:

                tv_password_title.setText("Import Wallet");
                createButton.setText("Import");
                tv_file_path.setVisibility(View.GONE);
                et_password.setVisibility(View.GONE);
                et_paste.setVisibility(View.VISIBLE);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_COLLAPSED);

                break;

            case R.id.btnScan:
                startActivityForResult(new Intent(mContext, SacnQrActivity.class), READ_QR_CODE);
                break;

            case R.id.btnSelectFile:
                sendFile();
                break;

            case R.id.btnCancelImport:
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.btnCreate:
//                showPasswordDialogue();
                tv_password_title.setText("Create Wallet");
                createButton.setText("Create");
                et_password.setVisibility(View.VISIBLE);
                tv_file_path.setVisibility(View.GONE);
                et_paste.setVisibility(View.GONE);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btnImportExistingOne:
//                startImportWalletActivity();
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btnCreatePassword:
                if (et_paste.getVisibility() == View.GONE) {
                    if (tv_file_path.getVisibility() == View.GONE) {
                        if (TextUtils.isEmpty(et_password.getText().toString())) {

                            Utils.hideKeyboardView(WelcomeWalletActivity.this, v);

                        } else {
                            Utils.hideKeyboardView(WelcomeWalletActivity.this, v);
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            startWalletCreateActivity(et_password.getText().toString());
                        }
                    } else {
                        if (!TextUtils.isEmpty(et_password.getText().toString())) {
                            CallWalletImportFileApi(et_password.getText().toString(), tv_file_path.getText().toString());
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            et_password.setText("");
                        } else et_password.setError("Required Field");
                    }
                } else {

                    if (!TextUtils.isEmpty(et_paste.getText().toString())) {

//                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                clipboard.setPrimaryClip(ClipData.newPlainText("","0x834cd08ed21993b759a8235ad47278a768c8b2390bc8b9f8b0117956f523e80b"));
//                        if (clipboard != null && clipboard.hasPrimaryClip()) {
//                            Log.e("clip data", clipboard.getPrimaryClip().getItemAt(0).getText().toString());
//                            CallWalletImportType2Api(clipboard.getPrimaryClip().getItemAt(0).getText().toString());
                        Log.e("clip data", et_paste.getText().toString());
                        CallWalletImportType2Api(et_paste.getText().toString());
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        et_paste.setText("");
//                        }


                    } else et_paste.setError("Required Field");

                }

                break;
            case R.id.btnCancelPassword:
                Utils.hideKeyboardView(WelcomeWalletActivity.this, v);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.btnCancelConfirmation:
                sheetBehaviorImportName.setState(BottomSheetBehavior.STATE_COLLAPSED);
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btnImportConfirmation:
                if (!TextUtils.isEmpty(et_wallet_name.getText().toString())) {
                    PrefUtilities.saveString(mContext, Constants.PRIVATE_KEY, walletModelMain.getPrivateKey());
                    PrefUtilities.saveString(mContext, Constants.PUBLIC_KEY, walletModelMain.getPublicKey());
                    PrefUtilities.saveInt(mContext, Constants.WALLET_TYPE, walletModelMain.getType());
                    PrefUtilities.saveString(mContext, Constants.WALLET_NAME, et_wallet_name.getText().toString());
                    if (walletModelMain.getType() == 1) {
                        PrefUtilities.saveString(mContext, Constants.SALF_KEY, walletModelMain.getSalfKey());
                        PrefUtilities.saveString(mContext, Constants.IV_KEY, walletModelMain.getIvKey());
                    }

                    try {
                        JSONObject walletObject = new JSONObject(walletJsonMain);

                        ArrayList<WalletModel> walletModels = new Gson().fromJson(walletObject.getString(Constants.WALLETS)
                                , new TypeToken<ArrayList<WalletModel>>() {
                                }.getType());

                        for (WalletModel model : walletModels)
                            if (model.isActive())
                                model.setWalletName(et_wallet_name.getText().toString());

                        walletObject.remove(Constants.WALLETS);
                        walletObject.put(Constants.WALLETS, new Gson().toJson(walletModels));

                        PrefUtilities.saveFloat(mContext, Constants.BALANCE, BalanceMain);

                        PrefUtilities.saveString(mContext, Constants.WALLETS, walletObject.toString());
                        sheetBehaviorImportName.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Toast.makeText(mContext, "Wallet imported successfully", Toast.LENGTH_LONG).show();

                        Toast.makeText(WelcomeWalletActivity.this, "Wallet Created Successfully, Balance :" + BalanceMain, Toast.LENGTH_LONG).show();
                        PrefUtilities.saveInt(WelcomeWalletActivity.this, Constants.LOGGED_IN_AFTER_STAGE, Constants.LOGGED_IN_PIN);
                        Intent myIntent = new Intent(WelcomeWalletActivity.this, WalletCreatedActivity.class);
                        startActivity(myIntent);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else et_wallet_name.setError("Name is required");
                break;
        }
    }


    private void CallWalletImportFileApi(String password, String filePath) {
        if (com.darpysolutions.Utils.Utils.isNetworkConnected(mContext)) {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            HashMap<String, String> toSendMap = new HashMap<>();
            HashMap<String, String> toSendFileMap = new HashMap<>();
            toSendMap.put(Constants.PASSWORD, password);
            toSendFileMap.put(Constants.KEYFILE, filePath);

            new ImageUploadTask(Apis.IMPORT_WALLET, toSendMap, toSendFileMap, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(String result) {
                    progressDialog.dismiss();
                    Log.e("Result", "found :" + result);
                    JSONObject resultObject = null;
                    try {
                        resultObject = new JSONObject(result);
                        if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            JSONObject dataObject = resultObject.getJSONObject(Constants.DATA);

                            /*
                            *
                           {
                                "status_code": 1,
                                "message": "Export wallet successfully.",
                                "data": {
                                    "address": "13c5cd6953a54ee29f8000a23edc412d0b63ddfe",
                                    "crypto": {
                                        "cipher": "aes-128-ctr",
                                        "ciphertext": "d83f4de93ce5e1a4c7bca3206cb4df81e9b394cd5e432c8ae1c25be8a897f774",
                                        "cipherparams": {
                                            "iv": "70e51a29f27b886d7083b3977dadfd61"
                                        },
                                        "mac": "17f6787a0fb76f733443d0e03cbd77b7f3d5f3a612774e5e5cfbe45ae848e825",
                                        "kdf": "pbkdf2",
                                        "kdfparams": {
                                            "c": 262144,
                                            "dklen": 32,
                                            "prf": "hmac-sha256",
                                            "salt": "62f950a826c623ebb7b3b900f4c45e27331cd464a1ffed3c2ce8cf98a6dbf093"
                                        }
                                    },
                                    "id": "e54b3319-0db0-44ca-bfc7-34d7b6fb8416",
                                    "version": 3
                                }
                            }


                            * */
//                            WalletModel walletModels = new Gson().fromJson(getIntent().getStringExtra("roomData"), WalletModel.class);

//                            ArrayList<WalletModel> walletModels  = new Gson().fromJson(PrefUtilities
//                                    .getString(mContext,Constants.WALLETS), new TypeToken<ArrayList<WalletModel>>() {
//                            }.getType());

                            ArrayList<WalletModel> walletModels = new ArrayList<>();

                            WalletModel walletModel = new WalletModel();
                            walletModel.setPublicKey(dataObject.getString(Constants.PUBLIC_KEY));
                            walletModel.setPrivateKey(dataObject.getString(Constants.PRIVATE_KEY));
                            walletModel.setIvKey(dataObject.getString(Constants.IV_KEY));
                            walletModel.setSalfKey(dataObject.getString(Constants.SALF_KEY));
                            walletModel.setType(1);
                            walletModel.setActive(true);
                            walletModel.setWalletSequence(1);

                            walletModels.add(walletModel);
                            JsonObject walletJson = new JsonObject();
                            walletJson.addProperty(Constants.WALLETS, new Gson().toJson(walletModels));
                            walletJson.addProperty(Constants.DOVE_WALLETS_COUNT, 1);
                            walletJson.addProperty(Constants.OTHER_WALLETS_COUNT, 0);

//                            PrefUtilities.saveString(mContext, Constants.WALLETS, walletJson.toString());
//
//                            PrefUtilities.saveString(mContext, Constants.PRIVATE_KEY, dataObject.getString(Constants.PRIVATE_KEY));
//                            PrefUtilities.saveString(mContext, Constants.PUBLIC_KEY, dataObject.getString(Constants.PUBLIC_KEY));
//                            PrefUtilities.saveString(mContext, Constants.IV_KEY, dataObject.getString(Constants.IV_KEY));
//                            PrefUtilities.saveString(mContext, Constants.SALF_KEY, dataObject.getString(Constants.SALF_KEY));
//
//                            PrefUtilities.saveInt(mContext, Constants.WALLET_TYPE, 1);
//                            CallForWalletBalanceService();

                            WalletModel currentWallet = new WalletModel();
                            currentWallet.setPublicKey(dataObject.getString(Constants.PUBLIC_KEY));
                            currentWallet.setPrivateKey(dataObject.getString(Constants.PRIVATE_KEY));
                            currentWallet.setIvKey(dataObject.getString(Constants.IV_KEY));
                            currentWallet.setSalfKey(dataObject.getString(Constants.SALF_KEY));
                            currentWallet.setType(1);

                            CallForWalletBalanceService(walletJson.toString(), currentWallet, dataObject.getString(Constants.PUBLIC_KEY));

                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(mContext, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        } else {
            Toast.makeText(mContext, "Please Connect to Active Internet Connection", Toast.LENGTH_LONG).show();
        }

    }


    private void CallWalletImportType2Api(String privateKey) {
        if (com.darpysolutions.Utils.Utils.isNetworkConnected(mContext)) {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            HashMap<String, String> toSendMap = new HashMap<>();
            toSendMap.put(Constants.PRIVATE_KEY, privateKey);

            new PostAsynchTask(toSendMap, Apis.IMPORT_WALLET_TYPE2, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(String result) {
                    progressDialog.dismiss();
                    Log.e("Result", "found :" + result);
                    JSONObject resultObject = null;
                    try {
                        resultObject = new JSONObject(result);
                        if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            JSONObject dataObject = resultObject.getJSONObject(Constants.DATA);

                            ArrayList<WalletModel> walletModels = new ArrayList<>();

                            WalletModel walletModel = new WalletModel();
                            walletModel.setPublicKey(dataObject.getString(Constants.PUBLIC_KEY));
                            walletModel.setPrivateKey(dataObject.getString(Constants.PRIVATE_KEY));
                            walletModel.setType(2);
                            walletModel.setActive(true);
                            walletModel.setWalletSequence(1);

                            walletModels.add(walletModel);
                            JsonObject walletJson = new JsonObject();
                            walletJson.addProperty(Constants.WALLETS, new Gson().toJson(walletModels));
                            walletJson.addProperty(Constants.DOVE_WALLETS_COUNT, 0);
                            walletJson.addProperty(Constants.OTHER_WALLETS_COUNT, 1);

//                            PrefUtilities.saveString(mContext, Constants.WALLETS, walletJson.toString());
//
//                            PrefUtilities.saveString(mContext, Constants.PRIVATE_KEY, dataObject.getString(Constants.PRIVATE_KEY));
//                            PrefUtilities.saveString(mContext, Constants.PUBLIC_KEY, dataObject.getString(Constants.PUBLIC_KEY));
//                            PrefUtilities.saveInt(mContext, Constants.WALLET_TYPE, 2);
//                            CallForWalletBalanceService();
                            WalletModel currentWallet = new WalletModel();
                            currentWallet.setPublicKey(dataObject.getString(Constants.PUBLIC_KEY));
                            currentWallet.setPrivateKey(dataObject.getString(Constants.PRIVATE_KEY));
                            currentWallet.setType(2);

                            CallForWalletBalanceService(walletJson.toString(), currentWallet, dataObject.getString(Constants.PUBLIC_KEY));

                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(mContext, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        } else {
            Toast.makeText(mContext, "Please Connect to Active Internet Connection", Toast.LENGTH_LONG).show();
        }

    }


    private void startImportWalletActivity() {
        Intent myIntent = new Intent(WelcomeWalletActivity.this, ImportWalletActivity.class);
        startActivity(myIntent);
    }

    private void startWalletCreateActivity(String password) {

        CallWalletCreateApi(password);

    }

    private void CallWalletCreateApi(String password) {
        if (com.darpysolutions.Utils.Utils.isNetworkConnected(WelcomeWalletActivity.this)) {
            final ProgressDialog progressDialog = new ProgressDialog(WelcomeWalletActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            HashMap<String, String> toSendMap = new HashMap<>();
            toSendMap.put("password", password);
            new PostAsynchTask(toSendMap, Apis.CREATE_WALLET, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(String result) {
                    progressDialog.dismiss();
                    Log.e("Result", "found :" + result);
                    JSONObject resultObject = null;
                    try {
                        resultObject = new JSONObject(result);
                        if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            JSONObject dataObject = resultObject.getJSONObject(Constants.DATA);

                            /*
                            *
                            {
                                "status_code": 1,
                                "message": "Wallet created successfully.",
                                "data": {
                                    "privateKey": "e15cdf7d7673fd1d06142114f84ad3577c5001c2dbdff19f85e178f2889b10e0",
                                    "publicKey": "13c5cd6953a54ee29f8000a23edc412d0b63ddfe",
                                    "iv": "70e51a29f27b886d7083b3977dadfd61",
                                    "salf": "62f950a826c623ebb7b3b900f4c45e27331cd464a1ffed3c2ce8cf98a6dbf093"
                                }
                            }

                            * */

                            ArrayList<WalletModel> walletModels = new ArrayList<>();

                            WalletModel walletModel = new WalletModel();
                            walletModel.setPublicKey(dataObject.getString(Constants.PUBLIC_KEY));
                            walletModel.setPrivateKey(dataObject.getString(Constants.PRIVATE_KEY));
                            walletModel.setIvKey(dataObject.getString(Constants.IV_KEY));
                            walletModel.setSalfKey(dataObject.getString(Constants.SALF_KEY));
                            walletModel.setType(1);

                            walletModel.setActive(true);
                            walletModel.setWalletSequence(1);

                            walletModels.add(walletModel);
                            JsonObject walletJson = new JsonObject();
                            walletJson.addProperty(Constants.WALLETS, new Gson().toJson(walletModels));
                            walletJson.addProperty(Constants.DOVE_WALLETS_COUNT, 1);
                            walletJson.addProperty(Constants.OTHER_WALLETS_COUNT, 0);

//                            PrefUtilities.saveString(mContext, Constants.WALLETS, walletJson.toString());
//
//                            PrefUtilities.saveString(WelcomeWalletActivity.this, Constants.PRIVATE_KEY, dataObject.getString(Constants.PRIVATE_KEY));
//                            PrefUtilities.saveString(WelcomeWalletActivity.this, Constants.PUBLIC_KEY, dataObject.getString(Constants.PUBLIC_KEY));
//                            PrefUtilities.saveString(WelcomeWalletActivity.this, Constants.IV_KEY, dataObject.getString(Constants.IV_KEY));
//                            PrefUtilities.saveString(WelcomeWalletActivity.this, Constants.SALF_KEY, dataObject.getString(Constants.SALF_KEY));
//                            PrefUtilities.saveInt(WelcomeWalletActivity.this, Constants.WALLET_TYPE, 1);

//                            CallForWalletBalanceService();

                            WalletModel currentWallet = new WalletModel();
                            currentWallet.setPublicKey(dataObject.getString(Constants.PUBLIC_KEY));
                            currentWallet.setPrivateKey(dataObject.getString(Constants.PRIVATE_KEY));
                            currentWallet.setIvKey(dataObject.getString(Constants.IV_KEY));
                            currentWallet.setSalfKey(dataObject.getString(Constants.SALF_KEY));
                            currentWallet.setType(1);

                            CallForWalletBalanceService(walletJson.toString(), currentWallet, dataObject.getString(Constants.PUBLIC_KEY));

                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(WelcomeWalletActivity.this, msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(WelcomeWalletActivity.this, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        } else {
            Toast.makeText(WelcomeWalletActivity.this, "Please Connect to Active Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void CallForWalletBalanceService(final String walletJson, final WalletModel walletModel, String publicKey) {
        if (com.darpysolutions.Utils.Utils.isNetworkConnected(WelcomeWalletActivity.this)) {
            final ProgressDialog progressDialog = new ProgressDialog(WelcomeWalletActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            HashMap<String, String> toSendMap = new HashMap<>();
            toSendMap.put(Constants.ADDRESS, (Constants.IS_DEBUG ? "0X" : "") + publicKey);

            new PostAsynchTask(toSendMap, Apis.BALANCE_CHECK, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(String result) {
                    progressDialog.dismiss();
                    Log.e("Result", "found :" + result);
                    JSONObject resultObject = null;
                    try {
                        resultObject = new JSONObject(result);
                        if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {
                            JSONObject dataObject = resultObject.getJSONObject(Constants.DATA);
                            String balance = dataObject.getString(Constants.BALANCE);
                            BalanceMain = Float.parseFloat(dataObject.getString(Constants.BALANCE));

                            walletModelMain = walletModel;
                            walletJsonMain = walletJson;
                            tv_dove_balance.setText(PrefUtilities.getFloat(mContext, Constants.BALANCE) + " Dove");


                            if (walletModel.getType() == 1) {
                                btnImportConfirmation.setText("Save this wallet");
                                btnCancelConfirmation.setText("Cancel");

                            } else {
                                btnImportConfirmation.setText("Import this wallet");
                                btnCancelConfirmation.setText("Import Again");
                            }

                            sheetBehaviorImportName.setState(BottomSheetBehavior.STATE_EXPANDED);


                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("0")) {
                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(WelcomeWalletActivity.this, msg, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(WelcomeWalletActivity.this, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        } else {
            Toast.makeText(WelcomeWalletActivity.this, "Please Connect to Active Internet Connection", Toast.LENGTH_LONG).show();
        }

    }
}
