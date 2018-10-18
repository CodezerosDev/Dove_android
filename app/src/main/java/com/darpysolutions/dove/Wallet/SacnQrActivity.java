package com.darpysolutions.dove.Wallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.dove.R;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class SacnQrActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sacn_qr);
    }


    @Override
    public void onScanned(Barcode barcode) {
        Log.e("key", barcode.displayValue);
        setResult(RESULT_OK, new Intent().putExtra(Constants.PRIVATE_KEY, barcode.displayValue));
        finish();
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {
        // multiple barcodes scanned
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        // barcode scanned from bitmap image
    }

    @Override
    public void onScanError(String s) {
    }

    @Override
    public void onCameraPermissionDenied() {
        // camera permission denied
    }
}
