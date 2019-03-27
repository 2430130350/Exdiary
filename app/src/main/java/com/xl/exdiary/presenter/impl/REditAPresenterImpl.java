package com.xl.exdiary.presenter.impl;

import android.annotation.SuppressLint;

import com.xl.exdiary.model.impl.DiaryModel;
import com.xl.exdiary.model.impl.REditAModelImpl;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IDiaryModel;
import com.xl.exdiary.model.inter.IREditAModel;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.presenter.inter.IREditAPresenter;
import com.xl.exdiary.view.inter.IMainAView;
import com.xl.exdiary.view.inter.IREditAView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class REditAPresenterImpl implements IREditAPresenter {
    private IUserModel mIUserModel;
    private IDiaryModel mIDiaryModel;
    private IMainAView mIMainAview;

    public REditAPresenterImpl(IMainAView tIMainAview) {
        mIUserModel = new UserModel();
        mIDiaryModel = new DiaryModel();
        mIMainAview = tIMainAview;
    }

    @Override
    public boolean saveDiary(String title, String body){//添加一个日记 JSONObject 包含 账户姓名/id 日记 title body
        JSONObject diary = new JSONObject();
        JSONObject user = mIUserModel.getUserInfo();
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new  SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        if(title.length() == 0 || body.length() == 0)
        {//传入一个参数为空的 json 对象
            return false;
        }
        else if (user != null){
            try {
                diary.put("name",user.getString("name"));
                diary.put("uuid",user.getString("uuid"));
                diary.put("date",simpleDateFormat.format(date));
                diary.put("title",title);
                diary.put("body",body);
            } catch (JSONException e) {
               mIMainAview.exception();
               return false;
            }
            return mIDiaryModel.saveDiary(diary);
        }
        else
            return false;
    }

    @Override
    public boolean modifyDiary(String date, String title, String body) {//修改日记
        JSONObject diary = new JSONObject();
        JSONObject user = mIUserModel.getUserInfo();
        if(title.length() == 0 || body.length() == 0 || date.length() == 0)
        {//传入一个参数为空的 json 对象
            return false;
        }
        else if(user != null) {
            try {
                diary.put("name",user.getString("name"));
                diary.put("uuid",user.getString("uuid"));
                diary.put("date",date);
                diary.put("title",title);
                diary.put("body",body);
            } catch (JSONException e) {
                mIMainAview.exception();
                return false;
            }
            return mIDiaryModel.saveDiary(diary);
        }
        else
            return false;
    }
}
