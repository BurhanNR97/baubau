package com.sig.baubau.algoritma;

import java.util.List;

public class grupBanjir {
    public List<Double> getTidak() {
        return tidak;
    }

    public void setTidak(List<Double> tidak) {
        this.tidak = tidak;
    }

    public List<Double> getRendah() {
        return rendah;
    }

    public void setRendah(List<Double> rendah) {
        this.rendah = rendah;
    }

    public List<Double> getSedang() {
        return sedang;
    }

    public void setSedang(List<Double> sedang) {
        this.sedang = sedang;
    }

    public List<Double> getTinggi() {
        return tinggi;
    }

    public void setTinggi(List<Double> tinggi) {
        this.tinggi = tinggi;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    private List<String> id;
    private List<Double> tidak;
    private List<Double> rendah;
    private List<Double> sedang;
    private List<Double> tinggi;
}
