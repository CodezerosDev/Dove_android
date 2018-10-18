package com.darpysolutions.dove.Wallet;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class WalletHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private final int READ_REQUEST_CODE = 123;
    private final int READ_QR_CODE = 124;

    public final static int QRcodeWidth = 500;

    TextView tvImportWallet, tvExportWallet;
    ImageView ivBack;
    TextView tv_switch;
    TextView tv_balance;
    TextView tv_wallet_name;

    Button btnPasteCopy;
    Button btnScan;
    Button btnCancelImport;
    Button btnSelectFile;

    Button createButtonExport;
    Button cancelBtnExport;

    TextView tv_password_title;
    TextView tv_file_path;
    EditText et_paste;
    EditText et_password;

    Button btnexport_type2;
    Button btnCancelType2;
    ImageView iv_qr;
    TextView tv_public;

    BottomSheetBehavior sheetBehaviorImport;
    LinearLayout relative_import;

    BottomSheetBehavior sheetBehaviorExport;
    LinearLayout relative_export;

    BottomSheetBehavior sheetBehaviorExportType2;
    LinearLayout relative_export_type2;

    BottomSheetBehavior sheetBehaviorImportName;
    LinearLayout relative_import_name;

    Context mContext;

    String walletJsonMain = "";
    WalletModel walletModelMain = null;
    float BalanceMain = 0;

    EditText et_wallet_name;
    TextView tv_ether_balance;
    TextView tv_dove_balance;

    Button btnImportConfirmation, btnCancelConfirmation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallethome);
        mContext = this;
        tvImportWallet = findViewById(R.id.tvImportWallet);
        tvExportWallet = findViewById(R.id.tvExportWallet);
        ivBack = findViewById(R.id.ivBack);
        tv_switch = findViewById(R.id.tv_switch);
        tv_balance = findViewById(R.id.tv_balance);
        tv_file_path = findViewById(R.id.tv_file_path);
        tv_password_title = findViewById(R.id.tv_password_title);
        tv_wallet_name = findViewById(R.id.tv_wallet_name);
        et_paste = findViewById(R.id.et_paste);
        tv_balance.setText(PrefUtilities.getFloat(mContext, Constants.BALANCE) + "");
        tv_wallet_name.setText(PrefUtilities.getString(mContext, Constants.WALLET_NAME));

        tv_public = findViewById(R.id.tv_public);
        iv_qr = findViewById(R.id.iv_qr);
        btnexport_type2 = findViewById(R.id.btnexport_type2);
        btnCancelType2 = findViewById(R.id.btnCancelType2);


        btnPasteCopy = findViewById(R.id.btnPasteCopy);
        btnScan = findViewById(R.id.btnScan);
        btnCancelImport = findViewById(R.id.btnCancelImport);
        btnSelectFile = findViewById(R.id.btnSelectFile);

        createButtonExport = findViewById(R.id.btnCreatePassword);
        et_password = findViewById(R.id.et_password);
        cancelBtnExport = findViewById(R.id.btnCancelPassword);

        et_wallet_name = findViewById(R.id.et_wallet_name);
        tv_dove_balance = findViewById(R.id.tv_dove_balance);
        tv_ether_balance = findViewById(R.id.tv_ether_balance);

        btnImportConfirmation = findViewById(R.id.btnImportConfirmation);
        btnCancelConfirmation = findViewById(R.id.btnCancelConfirmation);


        relative_import = findViewById(R.id.relative_import);
        sheetBehaviorImport = BottomSheetBehavior.from(relative_import);
        sheetBehaviorImport.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {

                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                relative_import.bringToFront();
            }
        });

        relative_export_type2 = findViewById(R.id.relative_export_type2);
        sheetBehaviorExportType2 = BottomSheetBehavior.from(relative_export_type2);
        sheetBehaviorExportType2.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                relative_export_type2.bringToFront();
            }
        });

        relative_export = findViewById(R.id.relative_export);
        sheetBehaviorExport = BottomSheetBehavior.from(relative_export);
        sheetBehaviorExport.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                relative_export.bringToFront();
            }
        });

        relative_import_name = findViewById(R.id.relative_import_name);
        sheetBehaviorImportName = BottomSheetBehavior.from(relative_import_name);
        sheetBehaviorImportName.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                relative_import_name.bringToFront();
            }
        });

        btnCancelConfirmation.setOnClickListener(this);
        btnImportConfirmation.setOnClickListener(this);

        createButtonExport.setOnClickListener(this);
        cancelBtnExport.setOnClickListener(this);

        btnPasteCopy.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        btnCancelImport.setOnClickListener(this);
        btnSelectFile.setOnClickListener(this);

        btnCancelType2.setOnClickListener(this);
        btnexport_type2.setOnClickListener(this);

        ivBack.setOnClickListener(this);
        tv_switch.setOnClickListener(this);
        tvImportWallet.setOnClickListener(this);
        tvExportWallet.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tv_balance != null) {
            tv_balance.setText(PrefUtilities.getFloat(mContext, Constants.BALANCE) + "");
            tv_wallet_name.setText(PrefUtilities.getString(mContext, Constants.WALLET_NAME));
        }
    }

    @Override
    public void onClick(View v) {
        ClipboardManager clipboard;
        switch (v.getId()) {

            case R.id.btnexport_type2:
                clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("", tv_public.getText().toString()));
                Toast.makeText(mContext, "Copied successfully", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnCancelType2:
                sheetBehaviorExportType2.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.tvImportWallet:
//                startWalletImportActivity();
                Utils.hideKeyboardView(mContext, v);
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.tvExportWallet:
                Utils.hideKeyboardView(mContext, v);
                if (PrefUtilities.getInt(mContext, Constants.WALLET_TYPE) == 1) {
                    tv_password_title.setText("Export Wallet");
                    createButtonExport.setText("Export");
                    tv_file_path.setVisibility(View.GONE);
                    et_paste.setVisibility(View.GONE);
                    et_password.setVisibility(View.VISIBLE);
                    sheetBehaviorExport.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {

                    Utils.hideKeyboardView(mContext, v);
                    sheetBehaviorExportType2.setState(BottomSheetBehavior.STATE_EXPANDED);

                    tv_public.setText(PrefUtilities.getString(mContext, Constants.PUBLIC_KEY));

                    new showBarcode().execute();
//                    new Handler().post();


                }
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.tv_switch:
                startWalletListActivity();
                break;

            case R.id.btnPasteCopy:

//                tv_password_title.setText("Import Wallet");
//                createButtonExport.setText("Import");
//                tv_file_path.setVisibility(View.GONE);
//                et_password.setVisibility(View.GONE);
//                et_paste.setVisibility(View.VISIBLE);
//                    et_paste.setText(clipboard.getPrimaryClip().getItemAt(0).getText().toString());
//                sheetBehaviorExport.setState(BottomSheetBehavior.STATE_EXPANDED);
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_COLLAPSED);
                clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if (clipboard != null && clipboard.hasPrimaryClip())
                    CallWalletImportType2Api(clipboard.getPrimaryClip().getItemAt(0).getText().toString());

                break;

            case R.id.btnScan:
                startActivityForResult(new Intent(mContext, SacnQrActivity.class), READ_QR_CODE);
                break;


            case R.id.btnCancelImport:
                Utils.hideKeyboardView(mContext, v);
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.btnSelectFile:
                sendFile();
                break;

            case R.id.btnCancelPassword:
                Utils.hideKeyboardView(mContext, v);
                sheetBehaviorExport.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.btnCreatePassword:
                if (et_paste.getVisibility() == View.GONE) {
                    if (tv_file_path.getVisibility() == View.GONE) {
                        if (!TextUtils.isEmpty(et_password.getText().toString())) {
                            Utils.hideKeyboardView(mContext, v);
                            sheetBehaviorExport.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            CallWalletExportApi(et_password.getText().toString());
                            et_password.setText("");
                        } else et_password.setError("Required Field");
                    } else {
                        if (!TextUtils.isEmpty(et_password.getText().toString())) {
                            CallWalletImportFileApi(et_password.getText().toString(), tv_file_path.getText().toString());
                            sheetBehaviorExport.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            et_password.setText("");
                        } else et_password.setError("Required Field");
                    }
                } else {

                    if (!TextUtils.isEmpty(et_paste.getText().toString())) {

//                        ClipboardManager clipboard2 = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                        clipboard2.setPrimaryClip(ClipData.newPlainText("", "0x834cd08ed21993b759a8235ad47278a768c8b2390bc8b9f8b0117956f523e80b"));
//                        if (clipboard != null && clipboard.hasPrimaryClip()) {
//                            Log.e("clip data", clipboard.getPrimaryClip().getItemAt(0).getText().toString());
//                            CallWalletImportType2Api(clipboard.getPrimaryClip().getItemAt(0).getText().toString());
                        Log.e("clip data", et_paste.getText().toString());
                        CallWalletImportType2Api(et_paste.getText().toString());
                        sheetBehaviorExport.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        et_paste.setText("");
//                        }


                    } else et_paste.setError("Required Field");

                }
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
                        if (tv_balance != null) {
                            tv_balance.setText(PrefUtilities.getFloat(mContext, Constants.BALANCE) + "");
                            tv_wallet_name.setText(PrefUtilities.getString(mContext, Constants.WALLET_NAME));
                        }
                        Toast.makeText(mContext, "Wallet imported successfully", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else et_wallet_name.setError("Name is required");
                break;
        }

    }


    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor) : getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }


    class showBarcode extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap = null;

        @Override
        protected void onPostExecute(Void aVoid) {
            if (bitmap != null)
                iv_qr.setImageBitmap(bitmap);

        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                bitmap = TextToImageEncode(PrefUtilities.getString(mContext, Constants.PRIVATE_KEY));
            } catch (WriterException e) {
                e.printStackTrace();
            }


            return null;
        }
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
                createButtonExport.setText("Import");
                tv_file_path.setVisibility(View.VISIBLE);
                et_password.setVisibility(View.VISIBLE);
                et_paste.setVisibility(View.GONE);
                tv_file_path.setText(path);
                sheetBehaviorExport.setState(BottomSheetBehavior.STATE_EXPANDED);
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_COLLAPSED);

            } else {
                Toast.makeText(mContext, "File select cancelled", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == READ_QR_CODE) {
            if (resultCode == RESULT_OK) {

                tv_password_title.setText("Import Wallet");
                createButtonExport.setText("Import");
                tv_file_path.setVisibility(View.GONE);
                et_password.setVisibility(View.GONE);
                et_paste.setVisibility(View.VISIBLE);
                et_paste.setText(data.getStringExtra(Constants.PRIVATE_KEY));
                sheetBehaviorExport.setState(BottomSheetBehavior.STATE_EXPANDED);
                sheetBehaviorImport.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        }

    }

    private void startWalletListActivity() {
        startActivity(new Intent(this, WalletListActivity.class));
    }

    private void startWalletImportActivity() {
        startActivity(new Intent(this, WalletImportActivity.class));
    }


    private void CallWalletExportApi(String password) {
        if (com.darpysolutions.Utils.Utils.isNetworkConnected(mContext)) {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            HashMap<String, String> toSendMap = new HashMap<>();
            toSendMap.put(Constants.PASSWORD, password);
            toSendMap.put(Constants.PRIVATE_KEY, PrefUtilities.getString(mContext, Constants.PRIVATE_KEY));
            toSendMap.put(Constants.PUBLIC_KEY, (Constants.IS_DEBUG ? "0X" : "") + PrefUtilities.getString(mContext, Constants.PUBLIC_KEY));
            toSendMap.put(Constants.SALF_KEY, PrefUtilities.getString(mContext, Constants.SALF_KEY));
            toSendMap.put(Constants.IV_KEY, PrefUtilities.getString(mContext, Constants.IV_KEY));
            new PostAsynchTask(toSendMap, Apis.EXPORT_WALLET, new OnTaskCompleted() {
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

                            String fileName = "UTC--";
                            fileName = fileName.concat(Utils.GetUTCDateTimeAsString()).concat("--").concat(dataObject.getString(Constants.ADDRESS));
                            Utils.generateNoteOnSD(mContext, fileName, dataObject.toString(), "Wallet exported successfully");

                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(WalletHomeActivity.this, msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(WalletHomeActivity.this, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        } else {
            Toast.makeText(WalletHomeActivity.this, "Please Connect to Active Internet Connection", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onBackPressed() {
        if (sheetBehaviorImport.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehaviorImport.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else if (sheetBehaviorExport.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehaviorExport.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else if (sheetBehaviorExportType2.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehaviorExportType2.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();

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

                            JSONObject walletObject = new JSONObject(PrefUtilities
                                    .getString(mContext, Constants.WALLETS));

                            ArrayList<WalletModel> walletModels = new Gson().fromJson(walletObject.getString(Constants.WALLETS)
                                    , new TypeToken<ArrayList<WalletModel>>() {
                                    }.getType());


                            WalletModel walletModel = new WalletModel();
                            walletModel.setPublicKey(dataObject.getString(Constants.PUBLIC_KEY));
                            walletModel.setPrivateKey(dataObject.getString(Constants.PRIVATE_KEY));
                            walletModel.setIvKey(dataObject.getString(Constants.IV_KEY));
                            walletModel.setSalfKey(dataObject.getString(Constants.SALF_KEY));
                            walletModel.setType(1);
                            walletModel.setActive(true);
                            walletModel.setWalletSequence(walletObject.getInt(Constants.DOVE_WALLETS_COUNT) + 1);

                            for (WalletModel walletModel1 : walletModels)
                                walletModel1.setActive(false);

                            walletModels.add(walletModel);

                            JsonObject walletJson = new JsonObject();
                            walletJson.addProperty(Constants.WALLETS, new Gson().toJson(walletModels));
                            walletJson.addProperty(Constants.DOVE_WALLETS_COUNT, walletModel.getWalletSequence());
                            walletJson.addProperty(Constants.OTHER_WALLETS_COUNT, walletObject.getInt(Constants.OTHER_WALLETS_COUNT));
//                            PrefUtilities.saveString(mContext, Constants.WALLETS, walletJson.toString());

//                            PrefUtilities.saveString(mContext, Constants.WALLETS, new Gson().toJson(walletJson));

                            WalletModel currentWallet = new WalletModel();
                            currentWallet.setPublicKey(dataObject.getString(Constants.PUBLIC_KEY));
                            currentWallet.setPrivateKey(dataObject.getString(Constants.PRIVATE_KEY));
                            currentWallet.setIvKey(dataObject.getString(Constants.IV_KEY));
                            currentWallet.setSalfKey(dataObject.getString(Constants.SALF_KEY));
                            currentWallet.setType(1);

                            CallForWalletBalanceService(walletJson.toString(), currentWallet);

//                            PrefUtilities.saveString(mContext, Constants.PRIVATE_KEY, dataObject.getString(Constants.PRIVATE_KEY));
//                            PrefUtilities.saveString(mContext, Constants.PUBLIC_KEY, dataObject.getString(Constants.PUBLIC_KEY));
//                            PrefUtilities.saveString(mContext, Constants.IV_KEY, dataObject.getString(Constants.IV_KEY));
//                            PrefUtilities.saveString(mContext, Constants.SALF_KEY, dataObject.getString(Constants.SALF_KEY));
//                            PrefUtilities.saveInt(mContext, Constants.WALLET_TYPE, 1);

                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(WalletHomeActivity.this, msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(WalletHomeActivity.this, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        } else {
            Toast.makeText(WalletHomeActivity.this, "Please Connect to Active Internet Connection", Toast.LENGTH_LONG).show();
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

                            JSONObject walletObject = new JSONObject(PrefUtilities
                                    .getString(mContext, Constants.WALLETS));

                            ArrayList<WalletModel> walletModels = new Gson().fromJson(walletObject.getString(Constants.WALLETS)
                                    , new TypeToken<ArrayList<WalletModel>>() {
                                    }.getType());


                            WalletModel walletModel = new WalletModel();
                            walletModel.setPublicKey(dataObject.getString(Constants.PUBLIC_KEY));
                            walletModel.setPrivateKey(dataObject.getString(Constants.PRIVATE_KEY));
                            walletModel.setType(2);
                            walletModel.setActive(true);
                            walletModel.setWalletSequence(walletObject.getInt(Constants.OTHER_WALLETS_COUNT) + 1);
                            for (WalletModel walletModel1 : walletModels)
                                walletModel1.setActive(false);

                            walletModels.add(walletModel);

                            JsonObject walletJson = new JsonObject();
                            walletJson.addProperty(Constants.WALLETS, new Gson().toJson(walletModels));
                            walletJson.addProperty(Constants.DOVE_WALLETS_COUNT, walletObject.getInt(Constants.OTHER_WALLETS_COUNT));
                            walletJson.addProperty(Constants.OTHER_WALLETS_COUNT, walletModel.getWalletSequence());
//                            PrefUtilities.saveString(mContext, Constants.WALLETS, walletJson.toString());

//                            PrefUtilities.saveString(mContext, Constants.WALLETS, new Gson().toJson(walletJson));
//                            PrefUtilities.saveString(mContext, Constants.WALLETS, walletJson.toString());

                            WalletModel currentWallet = new WalletModel();
                            currentWallet.setPublicKey(dataObject.getString(Constants.PUBLIC_KEY));
                            currentWallet.setPrivateKey(dataObject.getString(Constants.PRIVATE_KEY));
                            currentWallet.setType(2);

//                            PrefUtilities.saveString(mContext, Constants.PRIVATE_KEY, dataObject.getString(Constants.PRIVATE_KEY));
//                            PrefUtilities.saveString(mContext, Constants.PUBLIC_KEY, dataObject.getString(Constants.PUBLIC_KEY));
//                            PrefUtilities.saveInt(mContext, Constants.WALLET_TYPE, 2);

                            CallForWalletBalanceService(walletJson.toString(), currentWallet);

                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(WalletHomeActivity.this, msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(WalletHomeActivity.this, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        } else {
            Toast.makeText(WalletHomeActivity.this, "Please Connect to Active Internet Connection", Toast.LENGTH_LONG).show();
        }

    }


    private void CallForWalletBalanceService(final String walletJson, final WalletModel walletModel) {
        if (com.darpysolutions.Utils.Utils.isNetworkConnected(mContext)) {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            HashMap<String, String> toSendMap = new HashMap<>();
            toSendMap.put(Constants.ADDRESS, (Constants.IS_DEBUG ? "0X" : "") + walletModel.getPublicKey());

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
                            tv_balance.setText(PrefUtilities.getFloat(mContext, Constants.BALANCE) + "");
                            sheetBehaviorExport.setState(BottomSheetBehavior.STATE_COLLAPSED);


                            walletModelMain = walletModel;
                            walletJsonMain = walletJson;
                            tv_dove_balance.setText(PrefUtilities.getFloat(mContext, Constants.BALANCE) + " Dove");

                            sheetBehaviorImportName.setState(BottomSheetBehavior.STATE_EXPANDED);

//                            PrefUtilities.saveString(mContext, Constants.PRIVATE_KEY, walletModel.getPrivateKey());
//                            PrefUtilities.saveString(mContext, Constants.PUBLIC_KEY, walletModel.getPublicKey());
//                            PrefUtilities.saveInt(mContext, Constants.WALLET_TYPE, walletModel.getType());
//                            if (walletModel.getType() == 1) {
//                                PrefUtilities.saveString(mContext, Constants.SALF_KEY, walletModel.getSalfKey());
//                                PrefUtilities.saveString(mContext, Constants.IV_KEY, walletModel.getIvKey());
//                            }
//                            PrefUtilities.saveString(mContext, Constants.WALLETS, walletJson);


                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("0")) {
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
}
