package com.example.kanokkornthepburi.newhcvvoice.Service;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by satjawatpanakarn on 5/22/2017 AD.
 */

public class HistoryResponse {
    @SerializedName("History")
    private ArrayList<History> histories;

    public ArrayList<History> getHistories() {
        return histories;
    }
}
