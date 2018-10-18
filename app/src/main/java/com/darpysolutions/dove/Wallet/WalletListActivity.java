package com.darpysolutions.dove.Wallet;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.darpysolutions.dove.NetUtils.OnTaskCompleted;
import com.darpysolutions.dove.NetUtils.PostAsynchTask;
import com.darpysolutions.dove.NetUtils.PrefUtilities;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.Wallet.Adapters.WalletListAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import static com.darpysolutions.dove.Wallet.WalletHomeActivity.QRcodeWidth;

public class WalletListActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvWalletList;
    ImageView ivBack;

    BottomSheetBehavior sheetBehaviorExport;
    LinearLayout relative_export;

    BottomSheetBehavior sheetBehaviorExportType2;
    LinearLayout relative_export_type2;

    BottomSheetBehavior sheetBehaviorDelete;
    LinearLayout ll_delete_wallet;

    TextView tv_password_title;
    EditText et_password;
    Button createButtonExport;
    Button cancelBtnExport;

    Button btnexport_type2;
    Button btnCancelType2;
    ImageView iv_qr;
    TextView tv_public;

    Button btnDeleteWallet;
    Button btnDeleteCancel;

    Context mContext;

    WalletListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_walletlist);

        rvWalletList = findViewById(R.id.rvWalletList);
        ivBack = findViewById(R.id.ivBack);

        tv_public = findViewById(R.id.tv_public);
        iv_qr = findViewById(R.id.iv_qr);
        btnexport_type2 = findViewById(R.id.btnexport_type2);
        btnCancelType2 = findViewById(R.id.btnCancelType2);

        createButtonExport = findViewById(R.id.btnCreatePassword);
        et_password = findViewById(R.id.et_password);
        cancelBtnExport = findViewById(R.id.btnCancelPassword);
        tv_password_title = findViewById(R.id.tv_password_title);

        btnDeleteWallet = findViewById(R.id.btnDeleteWallet);
        btnDeleteCancel = findViewById(R.id.btnDeleteCancel);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletListActivity.super.onBackPressed();
            }
        });

        btnDeleteCancel.setOnClickListener(this);
        btnDeleteWallet.setOnClickListener(this);
        createButtonExport.setOnClickListener(this);
        cancelBtnExport.setOnClickListener(this);
        btnexport_type2.setOnClickListener(this);
        btnCancelType2.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(WalletListActivity.this);
        rvWalletList.setLayoutManager(mLayoutManager);
        adapter = new WalletListAdapter(WalletListActivity.this);
        rvWalletList.setAdapter(adapter);
        rvWalletList.setItemAnimator(new DefaultItemAnimator());

        relative_export_type2 = findViewById(R.id.relative_export_type2);
        sheetBehaviorExportType2 = BottomSheetBehavior.from(relative_export_type2);
        sheetBehaviorExportType2.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        relative_export_type2.setBackgroundColor(getResources().getColor(R.color.trans_white));
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        relative_export_type2.setBackgroundColor(getResources().getColor(R.color.trans_white));
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        ll_delete_wallet = findViewById(R.id.ll_delete_wallet);
        sheetBehaviorDelete = BottomSheetBehavior.from(ll_delete_wallet);
        sheetBehaviorDelete.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        ll_delete_wallet.bringToFront();
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        ll_delete_wallet.bringToFront();
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                ll_delete_wallet.bringToFront();
            }
        });

        relative_export = findViewById(R.id.relative_export);
        sheetBehaviorExport = BottomSheetBehavior.from(relative_export);
        sheetBehaviorExport.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        relative_export.bringToFront();
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        relative_export.bringToFront();
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                relative_export.bringToFront();
            }
        });
    }

    public void exportWallet(WalletModel walletModel) {

        if (walletModel.getType() == 1) {
            tv_password_title.setText("Export Wallet");
            createButtonExport.setText("Export");
            createButtonExport.setTag(walletModel);
            et_password.setVisibility(View.VISIBLE);
            sheetBehaviorExport.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {

//            Utils.hideKeyboardView(mContext, v);
            sheetBehaviorExportType2.setState(BottomSheetBehavior.STATE_EXPANDED);
            tv_public.setText(PrefUtilities.getString(mContext, Constants.PRIVATE_KEY));
            new showBarcode().execute();


        }
    }


//        Intent myIntent = new Intent(WalletListActivity.this, WalletExportActivity.class);
//        startActivity(myIntent);


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

    @Override
    public void onBackPressed() {
        if (sheetBehaviorDelete.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehaviorDelete.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else if (sheetBehaviorExport.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehaviorExport.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else if (sheetBehaviorExportType2.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehaviorExportType2.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnexport_type2:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("", tv_public.getText().toString()));
                Toast.makeText(mContext, "Copied successfully", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnCancelType2:
                sheetBehaviorExportType2.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.tvExportWallet:
                Utils.hideKeyboardView(mContext, v);
                if (PrefUtilities.getInt(mContext, Constants.WALLET_TYPE) == 1) {
                    tv_password_title.setText("Export Wallet");
                    createButtonExport.setText("Export");
                    et_password.setVisibility(View.VISIBLE);
                    sheetBehaviorExport.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    Utils.hideKeyboardView(mContext, v);
                    sheetBehaviorExportType2.setState(BottomSheetBehavior.STATE_EXPANDED);
                    tv_public.setText(PrefUtilities.getString(mContext, Constants.PUBLIC_KEY));
                    new showBarcode().execute();
                }
                break;

            case R.id.btnCancelPassword:
                Utils.hideKeyboardView(mContext, v);
                sheetBehaviorExport.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.btnCreatePassword:
                if (!TextUtils.isEmpty(et_password.getText().toString())) {
                    Utils.hideKeyboardView(mContext, v);
                    sheetBehaviorExport.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    WalletModel walletModel = (WalletModel) v.getTag();
                    CallWalletExportApi(et_password.getText().toString(), walletModel.getPrivateKey(),
                            walletModel.getPublicKey(), walletModel.getSalfKey(), walletModel.getIvKey());
                    et_password.setText("");
                } else et_password.setError("Required Field");
                break;

            case R.id.btnDeleteCancel:
                sheetBehaviorDelete.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btnDeleteWallet:

                try {
                    JSONObject walletObject = new JSONObject(PrefUtilities
                            .getString(mContext, Constants.WALLETS));
                    ArrayList<WalletModel> drBeanArrayList = new Gson().fromJson(walletObject.getString(Constants.WALLETS)
                            , new TypeToken<ArrayList<WalletModel>>() {
                            }.getType());
                    Log.e("Size", drBeanArrayList.size() + "");
                    boolean isActive = ((WalletModel) v.getTag()).isActive();
                    for (WalletModel walletModel : drBeanArrayList) {
                        if (walletModel.getPublicKey().equals(((WalletModel) v.getTag()).getPublicKey())) {
                            drBeanArrayList.remove(walletModel);
                            break;
                        }
                    }

                    Log.e("Size", drBeanArrayList.size() + "");
                    if (isActive) {
                        for (WalletModel walletModel1 : drBeanArrayList)
                            walletModel1.setActive(false);
                        if (drBeanArrayList.size() > 0) {
                            drBeanArrayList.get(0).setActive(true);

                            if (drBeanArrayList.get(0).getType() == 1) {
                                PrefUtilities.saveString(mContext, Constants.PRIVATE_KEY, drBeanArrayList.get(0).getPrivateKey());
                                PrefUtilities.saveString(mContext, Constants.PUBLIC_KEY, drBeanArrayList.get(0).getPublicKey());
                                PrefUtilities.saveString(mContext, Constants.IV_KEY, drBeanArrayList.get(0).getIvKey());
                                PrefUtilities.saveString(mContext, Constants.SALF_KEY, drBeanArrayList.get(0).getSalfKey());
                                PrefUtilities.saveString(mContext, Constants.WALLET_NAME, drBeanArrayList.get(0).getWalletName());
                                PrefUtilities.saveInt(mContext, Constants.WALLET_TYPE, 1);
                            } else {
                                PrefUtilities.saveString(mContext, Constants.PRIVATE_KEY, drBeanArrayList.get(0).getPrivateKey());
                                PrefUtilities.saveString(mContext, Constants.PUBLIC_KEY, drBeanArrayList.get(0).getPublicKey());
                                PrefUtilities.saveString(mContext, Constants.WALLET_NAME, drBeanArrayList.get(0).getWalletName());
                                PrefUtilities.saveInt(mContext, Constants.WALLET_TYPE, 2);
                            }
                        }
                    }
                    walletObject.remove(Constants.WALLETS);
                    walletObject.put(Constants.WALLETS, new Gson().toJson(drBeanArrayList));

                    PrefUtilities.saveString(mContext, Constants.WALLETS, walletObject.toString());
                    Log.e("WalletObjet", walletObject.toString());
//                    PrefUtilities.saveString(mContext, Constants.WALLETS, new Gson().toJson(walletObject));
                    adapter.refreshData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sheetBehaviorDelete.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
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

    public void removeWallet(WalletModel walletModel) {

        btnDeleteWallet.setTag(walletModel);
        sheetBehaviorDelete.setState(BottomSheetBehavior.STATE_EXPANDED);


//        Intent myIntent = new Intent(WalletListActivity.this, WalletDeleteActivity.class);
//        startActivity(myIntent);
    }

    public void selectWallet(WalletModel wallet, int position) {

        try {
            JSONObject walletObject = new JSONObject(PrefUtilities
                    .getString(mContext, Constants.WALLETS));
            ArrayList<WalletModel> drBeanArrayList = new Gson().fromJson(walletObject.getString(Constants.WALLETS)
                    , new TypeToken<ArrayList<WalletModel>>() {
                    }.getType());
            Log.e("Size", drBeanArrayList.size() + "");
            for (WalletModel walletModel1 : drBeanArrayList)
                walletModel1.setActive(false);
            drBeanArrayList.get(position).setActive(true);

            walletObject.remove(Constants.WALLETS);
            walletObject.put(Constants.WALLETS, new Gson().toJson(drBeanArrayList));

            PrefUtilities.saveString(mContext, Constants.WALLETS, walletObject.toString());


            if (wallet.getType() == 1) {
                PrefUtilities.saveString(mContext, Constants.PRIVATE_KEY, wallet.getPrivateKey());
                PrefUtilities.saveString(mContext, Constants.PUBLIC_KEY, wallet.getPublicKey());
                PrefUtilities.saveString(mContext, Constants.IV_KEY, wallet.getIvKey());
                PrefUtilities.saveString(mContext, Constants.SALF_KEY, wallet.getSalfKey());
                PrefUtilities.saveInt(mContext, Constants.WALLET_TYPE, 1);
                PrefUtilities.saveString(mContext, Constants.WALLET_NAME, wallet.getWalletName());
            } else {
                PrefUtilities.saveString(mContext, Constants.PRIVATE_KEY, wallet.getPrivateKey());
                PrefUtilities.saveString(mContext, Constants.PUBLIC_KEY, wallet.getPublicKey());
                PrefUtilities.saveInt(mContext, Constants.WALLET_TYPE, 2);
                PrefUtilities.saveString(mContext, Constants.WALLET_NAME, wallet.getWalletName());
            }

            Log.e("WalletObjet", walletObject.toString());
//                    PrefUtilities.saveString(mContext, Constants.WALLETS, new Gson().toJson(walletObject));
            adapter.refreshData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void CallWalletExportApi(String password, String privateKey,
                                     String publicKey, String salfKey, String ivKey) {
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
