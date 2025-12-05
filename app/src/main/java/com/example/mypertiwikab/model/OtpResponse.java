// File: model/OtpResponse.java
package com.example.mypertiwikab.model;

import com.google.gson.annotations.SerializedName;

public class OtpResponse extends DefaultResponse {
    @SerializedName("guru_id") // HARUS SAMA DENGAN KEY DI PHP
    private String guruId;

    public String getGuruId() {
        return guruId;
    }
}