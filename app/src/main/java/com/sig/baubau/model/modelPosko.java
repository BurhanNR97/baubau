package com.sig.baubau.model;

import android.os.Parcel;
import android.os.Parcelable;

public class modelPosko implements Parcelable {
    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    private String kode;
    private String nama;
    private String lat;
    private String lng;

    public modelPosko() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kode);
        dest.writeString(this.nama);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
    }

    protected modelPosko(Parcel in) {
        this.kode = in.readString();
        this.nama = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
    }

    public static final Creator<modelPosko> CREATOR = new Creator<modelPosko>() {
        @Override
        public modelPosko createFromParcel(Parcel source) {
            return new modelPosko(source);
        }

        @Override
        public modelPosko[] newArray(int size) {
            return new modelPosko[size];
        }
    };
}
