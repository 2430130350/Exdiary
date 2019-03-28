package com.xl.exdiary.model.inter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IShareDiaryModel {
    public boolean shareDiary(JSONObject shareDiary);//分享日记
    public JSONArray getAllShareDiary(String myUUID);//获得所有分享日记
    public boolean disableShareDiary(JSONObject disShareDiary);//取消分享
}
