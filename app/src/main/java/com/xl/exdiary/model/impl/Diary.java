package com.xl.exdiary.model.impl;

public class Diary {
    private String title;
    private String body;
    private String startTime;

    public Diary(String title,String body,String startTime){
        this.title=title;
        this.body=body;
        this.startTime=startTime;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
