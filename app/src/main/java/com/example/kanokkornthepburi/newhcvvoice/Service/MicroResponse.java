package com.example.kanokkornthepburi.newhcvvoice.Service;

import com.example.kanokkornthepburi.newhcvvoice.MicroController;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kanokkornthepburi on 5/21/2017 AD.
 */

public class MicroResponse {
    @SerializedName("MicroController")
    ArrayList<MicroController> microControllers;

    public ArrayList<MicroController> getMicroControllers() {
        return microControllers;
    }
}
