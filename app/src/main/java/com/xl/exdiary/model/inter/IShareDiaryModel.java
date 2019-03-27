package com.xl.exdiary.model.inter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IShareDiaryModel {
    public boolean shareDiary(String myUUID,JSONObject shareDiary,String friendUUID);//分享日记
    public JSONArray getAllShareDiary(String myUUID);//获得所有分享日记
    public boolean disableShareDiary(String myUUID,JSONObject disShareDiary,String friendUUID);//取消分享
}
