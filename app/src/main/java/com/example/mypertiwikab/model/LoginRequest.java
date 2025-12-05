package com.example.mypertiwikab.model;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    // Karena kita mengirim data, kita hanya butuh Constructor.
}