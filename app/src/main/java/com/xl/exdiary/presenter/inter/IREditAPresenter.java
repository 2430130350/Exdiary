package com.xl.exdiary.presenter.inter;

import org.json.JSONException;

public interface IREditAPresenter {
   public  boolean saveDiary(String title, String body);//包含title，和body
   public  boolean modifyDiary(String date, String title, String body);//修改日记接口
}
