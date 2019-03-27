package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.Diary;

public interface IShareAPresenter {
    public boolean shareDiary(String name, String uuid, String date);//分享日记
    public Diary[] getMyShareDiary();//获得我的分享日记
    public Diary[] getFriendDiary();//获得朋友的分享日记
    public Diary[] getAllDiary();//获得所有分享日记
    public boolean diaableShareDiary(String name, String uuid, String date);//取消分享日记
}
