package com.xl.exdiary.model.impl;

public class User {
    private String name;
    private String deviceNumber;
    private String signature;
    private String mail;

    public User(String name, String deviceNumber, String signature, String mail) {
        this.name = name;
        this.deviceNumber=deviceNumber;
        this.signature=signature;
        this.mail = mail;
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

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) { this.deviceNumber = deviceNumber; }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}

