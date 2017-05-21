package com.example.kanokkornthepburi.newhcvvoice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kanokkornthepburi on 5/21/2017 AD.
 */

public class MicroController {
    @SerializedName("nameTH")
    private String nameThai;
    @SerializedName("name")
    private String nameEng;
    private int id;

    public String getNameEng() {
        return nameEng;
    }

    public String getNameThai() {
        return nameThai;
    }

    public int getId() {
        return id;
    }
}