package com.sig.baubau.model;

import android.os.Parcel;
import android.os.Parcelable;

public class modelBanjir implements Parcelable {

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public double getTinggi() {
        return tinggi;
    }

    public void setTinggi(double tinggi) {
        this.tinggi = tinggi;
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
    private double tinggi;
    private double sedang;
    private double rendah;
    private double tidak;

    public modelBanjir() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kode);
        dest.writeDouble(this.tinggi);
        dest.writeDouble(this.sedang);
        dest.writeDouble(this.rendah);
        dest.writeDouble(this.tidak);
    }

    protected modelBanjir(Parcel in) {
        this.kode = in.readString();
        this.tinggi = in.readDouble();
        this.sedang = in.readDouble();
        this.rendah = in.readDouble();
        this.tidak = in.readDouble();
    }

    public static final Creator<modelBanjir> CREATOR = new Creator<modelBanjir>() {
        @Override
        public modelBanjir createFromParcel(Parcel source) {
            return new modelBanjir(source);
        }

        @Override
        public modelBanjir[] newArray(int size) {
            return new modelBanjir[size];
        }
    };
}