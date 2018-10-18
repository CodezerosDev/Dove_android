package com.darpysolutions.dove.NetUtils;

import android.net.ParseException;
import android.os.AsyncTask;
import android.provider.Contacts;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by narendra on 2/20/2017.
 */

public class ImageUploadTask extends AsyncTask<Void, Void, String> {
    String Url;
    HashMap<String, String> sendMap, fileMap;
    OnTaskCompleted listener;

    public ImageUploadTask(String mUrl, HashMap<String, String> mMap, HashMap<String, String> mfileMap, OnTaskCompleted mTaskCompleteListener) {
        sendMap = mMap;
        Url = mUrl;
        fileMap = mfileMap;
        listener = mTaskCompleteListener;
        Log.e("ImageUploadTask", "Calling: " + Url + "Param:" + sendMap.toString() + " File:" + fileMap.toString());
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            if (fileMap != null && fileMap.size() > 0) {
                Iterator it = fileMap.entrySet().iterator();
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
                String filePath = (String) pair.getValue();
                String fileParam = (String) pair.getKey();
                String s = multipartRequest(Url, sendMap, fileParam, filePath);
                return s;
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("Result", "" + s);
        listener.onTaskCompleted(s);
        super.onPostExecute(s);
    }

    public String multipartRequest(String urlTo, HashMap<String, String> sendMap, String filefield, String filepath) throws ParseException, IOException {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";
        String result = "";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filepath);
            Log.e("FILE TOTAL SPACE", "" + file.getTotalSpace());
            Log.e("FILE LENGTH", "" + file.length());

            FileInputStream fileInputStream = new FileInputStream(file);
            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

//            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("client-service", "frontend-client");
            connection.setRequestProperty("auth-key", "simplerestapi");


            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            Iterator it = sendMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + pair.getKey() + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes("" + pair.getValue());
                outputStream.writeBytes(lineEnd);
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }


            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            inputStream = connection.getInputStream();
            result = this.convertStreamToString(inputStream);
            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
