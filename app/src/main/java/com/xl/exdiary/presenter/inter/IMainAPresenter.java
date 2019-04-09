package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.Diary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface IMainAPresenter {
    /*
        删除日记

        输入： 删除日记信息  date

        输出： 执行结果  true false

        异常： 返回异常
     */
    public boolean delDiary(String date);

    /*
        获得所有的日记

        输入：

        输出： Diary[]

        异常： 返回异常
     */
    public Diary[] getAllDiaryList();

    /*
        获得天气信息 （太懒没有做）
     */
    public JSONObject getWeather();

    /*
        对日记进行排序

        内部函数 不对外调用
     */
    public void orderJSONArrayList(JSONArray diarys, int first, int end);//对日记 JSONArray 进行排序

}
