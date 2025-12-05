package com.example.mypertiwikab.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AbsensiHistoryResponse extends DefaultResponse {
    @SerializedName("data")
    private List<AbsensiHistory> historyList;

    public List<AbsensiHistory> getHistoryList() {
        return historyList;
    }
}