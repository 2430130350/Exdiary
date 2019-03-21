package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.DiaryModel;
import org.json.JSONException;

public interface IMainAPresenter {

    public boolean delDiary(String date) throws JSONException;//diary 包含日期，以日期和 user 查找文件
    public DiaryModel[] getAllDiaryList() throws JSONException;

}
