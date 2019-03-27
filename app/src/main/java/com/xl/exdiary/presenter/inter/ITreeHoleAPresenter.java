package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.Diary;

public interface ITreeHoleAPresenter {
    public Diary[] getTreeHoleDiary();//获得树洞信息
    public boolean addTreeHoleDiary(String title, String body);//发布树洞
    public boolean delTreeHoleDiary(String date, String title);//删除树洞
}
