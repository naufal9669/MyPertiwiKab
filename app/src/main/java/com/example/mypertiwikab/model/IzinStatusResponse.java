package com.example.mypertiwikab.model;

import com.google.gson.annotations.SerializedName;

public class IzinStatusResponse {

    // Harus sama dengan key JSON di PHP get_izin_status.php
    @SerializedName("izin_status")
    private String izinStatus;

    public String getIzinStatus() {
        return izinStatus;
    }
}