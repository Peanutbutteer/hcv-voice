package com.example.kanokkornthepburi.newhcvvoice.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kanokkornthepburi on 5/20/2017 AD.
 */

public class Client {
    private static Client instance;
    private Retrofit retrofit;
    private final String BASE_URl = "http://158.108.207.4/sp_HCvVoice/";
    private Service service;

    private Client() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }


    public Service getService() {
        if (service == null) {
            service = retrofit.create(Service.class);
        }
        return service;
    }
}
