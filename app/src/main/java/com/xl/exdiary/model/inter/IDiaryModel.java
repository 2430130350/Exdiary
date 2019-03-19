package com.xl.exdiary.model.inter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IDiaryModel {
    public JSONArray getAllDiaryList();
    public boolean saveDiary(JSONObject diary);
    public Boolean deleteDiary(JSONObject diary);
}
