package com.example.mypertiwikab.model;

public class LoginResponse {
    private String status; // Harus cocok dengan output PHP ('success'/'error')
    private String message;
    private GuruModel guru;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public GuruModel getGuru() { return guru; }

    public boolean isSuccess() { return "success".equalsIgnoreCase(status); }
}