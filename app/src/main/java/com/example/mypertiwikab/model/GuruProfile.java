package com.example.mypertiwikab.model;

import com.google.gson.annotations.SerializedName;

public class GuruProfile {
    @SerializedName("id")
    private int id;
    @SerializedName("fullname")
    private String fullname;
    @SerializedName("email")
    private String email;
    @SerializedName("jabatan")
    private String jabatan;
    @SerializedName("nisn")
    private String nisn;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("foto_url")
    private String fotoUrl; // Dari get_profile.php

    public String getFullname() { return fullname; }
    public String getJabatan() { return jabatan; }
    public String getNisn() { return nisn; }
    public String getAlamat() { return alamat; }
    public String getFotoUrl() { return fotoUrl; }
    public String getEmail() { return email; }
    public int getId() { return id; }
}