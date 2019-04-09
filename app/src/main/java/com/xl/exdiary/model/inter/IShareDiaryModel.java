package com.xl.exdiary.model.inter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IShareDiaryModel {
    public JSONArray getMyDiaries(String  myUUID);//获取我上传的日记
    public boolean updateDiary(JSONObject diary);//上传日记
    public boolean shareDiary(JSONObject shareDiary);//分享日记
    public JSONArray getAllShareToDiary(String myUUID);//获得自己分享出去的日记
    public JSONArray getAllShareFromDiary(String myUUID);//获得自己接受到的日记
    public boolean disableShareDiary(JSONObject disShareDiary);//取消分享
}
