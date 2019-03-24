package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.Diary;

import org.json.JSONException;
import org.json.JSONObject;

public interface IMainAPresenter {

    public boolean delDiary(String date);//diary 包含日期，以日期和 user 查找文件
    public Diary[] getAllDiaryList();//获得所以日记
    public JSONObject getWeather();//获得天气信息

}
