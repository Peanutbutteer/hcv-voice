package com.example.kanokkornthepburi.newhcvvoice.Service;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HistoryResponse {
    @SerializedName("History")
    private ArrayList<History> histories;

    public ArrayList<History> getHistories() {
        return histories;
    }
}
