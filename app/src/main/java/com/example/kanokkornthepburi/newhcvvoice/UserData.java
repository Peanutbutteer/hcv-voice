package com.example.kanokkornthepburi.newhcvvoice;

import com.example.kanokkornthepburi.newhcvvoice.Utils.PrefUtils;

import java.util.ArrayList;

/**
 * Created by kanokkornthepburi on 5/11/2017 AD.
 */

public class UserData {
    private static UserData instance;

    private String username;

    private boolean micStatus = false;

    private ArrayList<Device> devices;

    private ArrayList<MicroController> microControllers;

    private String activeController = "";

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public ArrayList<MicroController> getMicroControllers() {
        return microControllers;
    }

    public void setMicroControllers(ArrayList<MicroController> microControllers) {
        this.microControllers = microControllers;
    }

    private UserData() {
        this.activeController = PrefUtils.getInstance().getHome();
    }

    public static UserData getInstance() { // Singleton pattern มาจากที่ว่า instance นั้นโปรแกรมนึงมีแค่อันเดียว (single) คือที่ return จาก getInstance() ไม่ว่าคลาสไหนเรียกก็จะได้ instance ตัวเดียวกันเสมอ
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOpenMic() {
        return micStatus;
    }

    public void setMicStatus(boolean micStatus) {
        this.micStatus = micStatus;
    }

    public String getActiveController() {
        return activeController;
    }

    public void setActiveController(String activeController) {
        this.activeController = activeController;
        PrefUtils.getInstance().setHome(activeController);
    }
}
