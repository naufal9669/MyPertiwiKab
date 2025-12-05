package com.example.mypertiwikab.model;

public class DefaultResponse {
    private String status;
    private String message;
    // Jika API Izin mengembalikan 'id_izin', kita perlu menambahkannya di sini:
    // private int id_izin;

    public String getStatus() { return status; }
    public String getMessage() { return message; }

    public boolean isSuccess() { return "success".equalsIgnoreCase(status); }
}