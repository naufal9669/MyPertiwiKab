package com.example.mypertiwikab.model;

import com.google.gson.annotations.SerializedName;

public class AbsensiHistory {
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("jam_datang")
    private String jamDatang;
    @SerializedName("jam_pulang")
    private String jamPulang;

    public String getTanggal() { return tanggal; }
    public String getJamDatang() { return jamDatang; }
    public String getJamPulang() { return jamPulang; }
}