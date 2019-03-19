package com.xl.exdiary.presenter.inter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IMainAPresenter {
    public boolean delDiary(JSONObject diary);
    public boolean saveDiary(JSONObject diary);
    public JSONArray getAllDiaryList();

}
