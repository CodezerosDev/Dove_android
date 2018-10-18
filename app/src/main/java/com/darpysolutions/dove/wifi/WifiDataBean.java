package com.darpysolutions.dove.wifi;

import android.util.Log;

public class WifiDataBean {
    String vol = "";

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getVolUni() {
        return volUni;
    }

    public void setVolUni(String volUni) {
        this.volUni = volUni;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDurationUni() {
        return durationUni;
    }

    public void setDurationUni(String durationUni) {
        this.durationUni = durationUni;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSpeedUni() {
        return speedUni;
    }

    public void setSpeedUni(String speedUni) {
        this.speedUni = speedUni;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGeneratedString() {
        return generatedString;
    }

    public void setGeneratedString(String generatedString) {
        this.generatedString = generatedString;
    }

    String volUni = "";  //M/G

    String duration = "";
    String durationUni = ""; //M/H/D

    String speed = "";
    String speedUni = ""; //K/M/G

    String price = "";
    String generatedString = "";

    public WifiDataBean(String mvol, String mvolUni, String mduration, String mdurationUni, String mspeed, String mspeedUni, String mprice) {

        setVol(mvol);
        setVolUni(mvolUni);
        setDuration(mduration);
        setDurationUni(mdurationUni);
        setSpeed(mspeed);
        setSpeedUni(mspeedUni);
        setPrice(mprice);

        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("code");
//        stringBuilder.append('A');
        stringBuilder.append(vol);
        stringBuilder.append('P');
        stringBuilder.append(volUni);
        stringBuilder.append('A');
        stringBuilder.append(duration);
        stringBuilder.append('P');
        stringBuilder.append(durationUni);
        stringBuilder.append('A');
//        stringBuilder.append(speed);
//        stringBuilder.append('P');
//        stringBuilder.append(speedUni);
//        stringBuilder.append('A');
        stringBuilder.append(price);
        stringBuilder.append('A');
        stringBuilder.append("code");

        generatedString = stringBuilder.toString();
    }

    public WifiDataBean(String generated) {
        generatedString = generated;
        String[] decyp = generated.split("A");
        Log.e("Size", "" + decyp.length);
        for (int i = 0; i < decyp.length; i++) {
            Log.e("D", "" + decyp[i]);
        }

        if (decyp.length == 4) {
            String[] vol = decyp[0].split("P");
            String[] dur = decyp[1].split("P");
//             String[] spd=decyp[3].split("P");


            setVol(vol[0]);
            setVolUni(vol[1]);

            setDuration(dur[0]);
            setDurationUni(dur[1]);

            Log.e("Data: ", getVol() + " " + getVolUni() + " " + getDuration() + " " + getDurationUni() + " " + getPrice());
//             setSpeed(spd[0]);
//             setSpeedUni(spd[1]);

            setPrice(decyp[2]);
        }

    }

    public String[] getDetailString() {
        String wifiDetails[] = new String[2];
//        wifiDetails[0]  = getVol();
        wifiDetails[1] = getVol();
        if (getVolUni().equalsIgnoreCase("M"))
            wifiDetails[1] = wifiDetails[1] + " MB";
        else
            wifiDetails[1] = wifiDetails[1] + " GB";
        String temp = wifiDetails[1].replace("B","");
        wifiDetails[0] = temp;
        wifiDetails[1] = wifiDetails[1] + " - " + getDuration();

        if (getDurationUni().equalsIgnoreCase("M"))
            wifiDetails[1] = wifiDetails[1] + "Minutes";
        else if (getDurationUni().equalsIgnoreCase("H"))
            wifiDetails[1] = wifiDetails[1] + "Hours";
        else
            wifiDetails[1] = wifiDetails[1] + "Days";


        return wifiDetails;
    }
}
