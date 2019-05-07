package com.example.android.nee_chanpriode.Model;

public class PeriodeHaid {
    private String uid;
    private int siklus,jml_hari;
    private String tgl_haid;

    public PeriodeHaid(int siklus, int jml_hari, String tgl_haid) {
        this.siklus = siklus;
        this.jml_hari = jml_hari;
        this.tgl_haid = tgl_haid;
    }

    public String getUid() {
        return uid;
    }

    public int getSiklus() {
        return siklus;
    }

    public void setSiklus(int siklus) {
        this.siklus = siklus;
    }

    public int getJml_hari() {
        return jml_hari;
    }

    public void setJml_hari(int jml_hari) {
        this.jml_hari = jml_hari;
    }

    public String getTgl_haid() {
        return tgl_haid;
    }

    public void setTgl_haid(String tgl_haid) {
        this.tgl_haid = tgl_haid;
    }
}
