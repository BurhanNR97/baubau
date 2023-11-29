package com.sig.baubau.model;

import android.os.Parcel;
import android.os.Parcelable;

public class modelLongsor implements Parcelable {

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public double getSedang() {
        return sedang;
    }

    public void setSedang(double sedang) {
        this.sedang = sedang;
    }

    public double getRendah() {
        return rendah;
    }

    public void setRendah(double rendah) {
        this.rendah = rendah;
    }

    public double getTidak() {
        return tidak;
    }

    public void setTidak(double tidak) {
        this.tidak = tidak;
    }

    private String kode;
    private double sedang;
    private double rendah;
    private double tidak;

    public modelLongsor() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kode);
        dest.writeDouble(this.sedang);
        dest.writeDouble(this.rendah);
        dest.writeDouble(this.tidak);
    }

    protected modelLongsor(Parcel in) {
        this.kode = in.readString();
        this.sedang = in.readDouble();
        this.rendah = in.readDouble();
        this.tidak = in.readDouble();
    }

    public static final Creator<modelLongsor> CREATOR = new Creator<modelLongsor>() {
        @Override
        public modelLongsor createFromParcel(Parcel source) {
            return new modelLongsor(source);
        }

        @Override
        public modelLongsor[] newArray(int size) {
            return new modelLongsor[size];
        }
    };
}