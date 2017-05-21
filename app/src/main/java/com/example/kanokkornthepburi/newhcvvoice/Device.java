package com.example.kanokkornthepburi.newhcvvoice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kanokkornthepburi on 5/20/2017 AD.
 */

public class Device {
    private int channel;
    @SerializedName("device_name_th")
    private String nameThai;
    @SerializedName("device_name")
    private String nameEng;
    private String status;
    private int id;

    public int getId() {
        return id;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getNameThai() {
        return nameThai;
    }

    public void setNameThai(String nameThai) {
        this.nameThai = nameThai;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public boolean getStatus() {
        return status != null && status.equals("ON");
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
