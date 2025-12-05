package com.example.mypertiwikab.model;

import com.google.gson.annotations.SerializedName;

public class StatusAbsenResponse extends DefaultResponse {
    @SerializedName("status_datang")
    private String statusDatang;
    @SerializedName("status_pulang")
    private String statusPulang;
    @SerializedName("jam_sekarang")
    private String jamSekarang;

    public String getStatusDatang() { return statusDatang; }
    public String getStatusPulang() { return statusPulang; }
    public String getJamSekarang() { return jamSekarang; }
}