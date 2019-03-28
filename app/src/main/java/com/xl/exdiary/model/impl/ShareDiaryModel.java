package com.xl.exdiary.model.impl;

import com.xl.exdiary.model.inter.IShareDiaryModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShareDiaryModel implements IShareDiaryModel {
    @Override
    public boolean shareDiary(JSONObject shareDiary) {
        return false;
    }

    @Override
    public JSONArray getAllShareDiary(String myUUID) {
        return null;
    }

    @Override
    public boolean disableShareDiary(JSONObject disShareDiary) {
        return false;
    }
}
