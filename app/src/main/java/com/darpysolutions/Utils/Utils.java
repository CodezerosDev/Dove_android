package com.darpysolutions.Utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.darpysolutions.dove.NetUtils.ImagePicker.isDownloadsDocument;
import static com.darpysolutions.dove.NetUtils.ImagePicker.isExternalStorageDocument;
import static com.darpysolutions.dove.NetUtils.ImagePicker.isMediaDocument;

public class Utils {

    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    public static void hideKeyboardView(Context ctx, View view) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View v = view;
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    public static void generateNoteOnSD(Context context, String sFileName, String sBody, String success) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Dove network");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, success, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static final String DATEFORMAT = "yyyy-MM-dd'T'HH_mm_ss.sssX";


    public static String GetUTCDateTimeAsString() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }


    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static String getEvent(int keycode) {
        String value = "";
        switch (keycode) {
            case KeyEvent.KEYCODE_0:
                value = "0";
                break;
            case KeyEvent.KEYCODE_1:
                value = "1";
                break;
            case KeyEvent.KEYCODE_2:
                value = "2";
                break;
            case KeyEvent.KEYCODE_3:
                value = "3";
                break;
            case KeyEvent.KEYCODE_4:
                value = "4";
                break;
            case KeyEvent.KEYCODE_5:
                value = "5";
                break;
            case KeyEvent.KEYCODE_6:
                value = "6";
                break;
            case KeyEvent.KEYCODE_7:
                value = "7";
                break;
            case KeyEvent.KEYCODE_8:
                value = "8";
                break;
            case KeyEvent.KEYCODE_9:
                value = "9";
                break;

        }
        return value;
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static double getFileSizeInDouble(long size, boolean isMb) {
        if (size <= 0)
            return 0;

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        Log.e("data from utils", (size / Math.pow(1024, isMb ? 2 : 3)) + " " + units[digitGroups]);

//        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
//        return (int)(size / Math.pow(1024, digitGroups));
        return (size / Math.pow(1024, isMb ? 2 : 3));
    }


}
