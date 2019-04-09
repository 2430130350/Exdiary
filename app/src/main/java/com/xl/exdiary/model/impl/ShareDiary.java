package com.xl.exdiary.model.impl;

public class ShareDiary {
    private String title;
    private String body;
    private String startTime;
    private String friendName;
    private String myName;
    private String friendUuid;

    public ShareDiary(String title, String body, String startTime, String friendName, String myName, String friendUuid) {
        this.title = title;
        this.body = body;
        this.startTime = startTime;
        this.friendName = friendName;
        this.myName = myName;
        this.friendUuid = friendUuid;
    }

    public String getFriendUuid() {
        return friendUuid;
    }

    public void setFriendUuid(String friendUuid) {
        this.friendUuid = friendUuid;
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

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }
}
