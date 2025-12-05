package com.example.mypertiwikab.model;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse extends DefaultResponse {
    @SerializedName("guru")
    private GuruProfile guruProfile;

    public GuruProfile getGuruProfile() {
        return guruProfile;
    }
}