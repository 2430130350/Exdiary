package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.DiaryModel;
import com.xl.exdiary.model.impl.REditAModelImpl;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IDiaryModel;
import com.xl.exdiary.model.inter.IREditAModel;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.presenter.inter.IREditAPresenter;
import com.xl.exdiary.view.inter.IREditAView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class REditAPresenterImpl implements IREditAPresenter {
    private IUserModel mIUserModel;
    private IDiaryModel mIDiaryModel;

    public REditAPresenterImpl(IREditAView aIREditAView) {
        mIUserModel = new UserModel();
        mIDiaryModel = new DiaryModel();
    }

    @Override
    public boolean saveDiary(String title, String body) throws JSONException {//添加一个日记 JSONObject 包含 账户姓名/id 日记 title body
        JSONObject diary = new JSONObject();
        JSONObject user = mIUserModel.getUserInfo();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new  SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        if(title.isEmpty() || body.isEmpty() )
        {//传入一个参数为空的 json 对象
            return false;
        }
        else {
            diary.put("name",user.getString("name"));
            diary.put("deviceNumber",user.getString("deviceNumber"));
            diary.put("date",simpleDateFormat.format(date));
            diary.put("title",title);
            diary.put("body",body);
            return mIDiaryModel.saveDiary(diary);
        }
    }
}
