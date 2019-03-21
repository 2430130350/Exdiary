package com.xl.exdiary.model.impl;

public class User {
    private String name;
    private int deviceNumber;
    private String signature;
    public User(String name, int deviceNumber,String signature) {
        this.name = name;
        this.deviceNumber=deviceNumber;
        this.signature=signature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
}

