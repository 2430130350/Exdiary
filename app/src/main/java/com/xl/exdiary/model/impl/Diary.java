package com.xl.exdiary.model.impl;

import java.sql.Timestamp;

public class Diary {
    private String title;
    private String body;
    private Timestamp startTime;

    public Diary(String title,String body,Timestamp startTime){
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
}
