package com.sig.baubau.model;

public class modelList {
    private String kel;
    private String hasil;

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }

    public String getKel() {
        return kel;
    }

    public void setKel(String kel) {
        this.kel = kel;
    }

    public modelList(String kel, String hasil) {
        this.kel = kel;
        this.hasil = hasil;
    }
}
