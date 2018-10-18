package com.darpysolutions.dove.NetUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;


public class PostAsynchTask extends AsyncTask<Void, Void, String> {

    HashMap<String, String> dataMap;
    String CallingURL;
    URL url;
    OnTaskCompleted myListener;
    private static final String TAG = "PostAsyncTask";


    public PostAsynchTask(HashMap<String, String> hMap, String URL, OnTaskCompleted listener) {
        this.dataMap = hMap;
        this.CallingURL = URL;
        this.myListener = listener;
        try {
            url = new URL(CallingURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("client-service", "frontend-client");
//            connection.setRequestProperty("auth-key", "simplerestapi");


            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = getQuery(dataMap);
            writer.write(data);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = connection.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();

            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();

            return responseOutput.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (myListener != null)
            if (result != null) {
                Log.e(TAG, "output : " + result.toString());
                myListener.onTaskCompleted(result);
            } else {
                myListener.onTaskCompleted("");
            }
    }


    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        if (params == null) return "";
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
//            result.append(pair.getKey().toString());
            result.append(URLEncoder.encode(pair.getKey().toString(), "UTF-8"));
            result.append("=");
//            result.append(pair.getValue().toString());
            result.append(URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
        }
        Log.e(TAG, "Url:" + url + " Input : " + result.toString());
        return result.toString();
    }
}
