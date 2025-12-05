package com.example.mypertiwikab.model;

import com.google.gson.annotations.SerializedName;

public class AbsenRequest {
    @SerializedName("guru_id")
    private int guruId;

    @SerializedName("tipe")
    private String tipe;

    @SerializedName("tanggal")
    private String tanggal;

    @SerializedName("waktu")
    private String waktu;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    // KONSTRUKTOR 1: Untuk Absen Biasa (Hanya ID & Tipe)
    public AbsenRequest(int guruId, String tipe) {
        this.guruId = guruId;
        this.tipe = tipe;
    }

    // KONSTRUKTOR 2: Untuk Absen Lokasi (Lengkap 6 Data - INI YANG KAMU BUTUHKAN)
    public AbsenRequest(int guruId, String tanggal, String waktu, String tipe, String latitude, String longitude) {
        this.guruId = guruId;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.tipe = tipe;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}