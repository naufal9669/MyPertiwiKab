package com.example.mypertiwikab.model;

import com.google.gson.annotations.SerializedName;

public class GuruModel {
    // Sesuaikan dengan nama kolom/key JSON dari API login_guru.php
    private int id;
    private String fullname;
    private String email;
    private String jabatan;

    public int getId() { return id; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getJabatan() { return jabatan; }
}