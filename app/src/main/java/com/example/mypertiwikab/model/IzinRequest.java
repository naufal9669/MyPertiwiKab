package com.example.mypertiwikab.model;

import com.google.gson.annotations.SerializedName;

public class IzinRequest {
    @SerializedName("guru_id")
    private int guruId;

    // BEDANYA DISINI: Harus "tanggal_izin" sesuai PHP kamu
    @SerializedName("tanggal_izin")
    private String tanggalIzin;

    @SerializedName("jenis_izin")
    private String jenisIzin;

    @SerializedName("deskripsi")
    private String deskripsi;

    public IzinRequest(int guruId, String tanggalIzin, String jenisIzin, String deskripsi) {
        this.guruId = guruId;
        this.tanggalIzin = tanggalIzin;
        this.jenisIzin = jenisIzin;
        this.deskripsi = deskripsi;
    }
}