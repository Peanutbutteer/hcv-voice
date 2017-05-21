package com.example.kanokkornthepburi.newhcvvoice.Service;

import java.util.List;

/**
 * Created by satjawatpanakarn on 5/22/2017 AD.
 */

public class History {
    private String date;
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