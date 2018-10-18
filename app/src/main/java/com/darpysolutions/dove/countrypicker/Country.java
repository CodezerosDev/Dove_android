package com.darpysolutions.dove.countrypicker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GODARD Tuatini on 07/05/15.
 */
public class Country implements Parcelable {
    private String isoCode;
    private String dialingCode;

    public Country() {

    }

    public Country(String isoCode, String dialingCode) {
        this.isoCode = isoCode;
        this.dialingCode = dialingCode;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getDialingCode() {
        return dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isoCode);
        dest.writeString(this.dialingCode);
    }

    protected Country(Parcel in) {
        this.isoCode = in.readString();
        this.dialingCode = in.readString();
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
