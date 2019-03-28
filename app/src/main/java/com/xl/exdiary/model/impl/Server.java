package com.xl.exdiary.model.impl;

public class Server {
    private static String IP="192.168.0.0";
    private static int post=5438;

    public static String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public static int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }
}
