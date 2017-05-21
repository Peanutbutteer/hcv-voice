package com.example.kanokkornthepburi.newhcvvoice.Service;

/**
 * Created by kanokkornthepburi on 5/20/2017 AD.
 */

public class LoginResponse {
    private String result;
    private String username;


    public Boolean getResult() {
        return result.toLowerCase().equals("true");
    }

    public String getUsername() {
        return username;
    }
}