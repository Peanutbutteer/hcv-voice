package com.example.kanokkornthepburi.newhcvvoice.Service;

/**
 * Created by satjawatpanakarn on 5/22/2017 AD.
 */

public class DeviceHistory {
    private String name;
    private float value;

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }

    public DeviceHistory(String name, float value) {
        this.name = name;
        this.value = value;
    }
}
