package com.example.kanokkornthepburi.newhcvvoice.Service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class History {
    private String date;
    @SerializedName("devices")
    private List<DeviceHistory> deviceHistories;

    public String getDate() {
        return date;
    }

    public List<DeviceHistory> getDeviceHistories() {
        return deviceHistories;
    }

    public History(String date, List<DeviceHistory> deviceHistories) {
        this.date = date;
        this.deviceHistories = deviceHistories;
    }
}