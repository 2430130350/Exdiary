package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.Diary;

import org.json.JSONException;

public interface IMainAPresenter {

    public boolean delDiary(String date);//diary 包含日期，以日期和 user 查找文件
    public Diary[] getAllDiaryList();

}
