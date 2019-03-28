package com.xl.exdiary.presenter.impl;

import android.annotation.SuppressLint;

import com.xl.exdiary.model.impl.AnonymousAModelImpl;
import com.xl.exdiary.model.impl.Diary;
import com.xl.exdiary.model.impl.DiaryModel;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IAnonymousAModel;
import com.xl.exdiary.model.inter.IDiaryModel;
import com.xl.exdiary.model.inter.ITreeHoleModel;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.presenter.inter.IAnonymousAPresenter;
import com.xl.exdiary.view.inter.IAnonymousAView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnonymousAPresenterImpl implements IAnonymousAPresenter {
    private IAnonymousAView mIAnonymousAView;
    private IAnonymousAModel mIAnonymousAModel;
    private IDiaryModel mIDiaryModel;
    private IUserModel muserModel;
    private ITreeHoleModel mTreeHoleModel;

    public AnonymousAPresenterImpl(IAnonymousAView aIAnonymousAView) {
        mIAnonymousAView = aIAnonymousAView;
        mIAnonymousAModel = new AnonymousAModelImpl();
        mIDiaryModel = new DiaryModel();
        muserModel = new UserModel();
    }

    //获得树洞
    @Override
    public Diary[] getTreeHoleDiary()
    {
        JSONObject jso;
        JSONArray jsa = mTreeHoleModel.getAllTreeHoleDiary();
        Diary[] diary = new Diary[jsa.length()];
        if (jsa.length() != 0)
        {
            for (int i = 0; i < jsa.length(); i++)
            {
                try {
                    jso = jsa.getJSONObject(i);
                    diary[i] = new Diary(jso.getString("title"),
                            jso.getString("body"), jso.getString("date"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //异常处理
                }
            }
            return diary;
        }
        return null;
    }

    //添加树洞
    @Override
    public boolean addTreeHoleDiary(String title, String body)
    {
        JSONObject ujso = muserModel.getUserInfo();
        JSONObject djso = null;
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new  SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        if(ujso != null && title.length() != 0 && body.length() != 0 )
        {
            try {
                djso.put("name",ujso.getString("name"));
                djso.put("uuid",ujso.getString("uuid"));
                djso.put("title",title);
                djso.put("body",body);
                djso.put("date",date);
                djso.put("praise",0);
            } catch (JSONException e) {
                e.printStackTrace();
                //异常处理
            }
            //以下进行树洞分享

        }
        return false;
    }

    //删除树洞
    @Override
    public boolean delTreeHoleDiary(String date, String title)
    {
        JSONObject jso = new JSONObject();
        JSONObject ujso = muserModel.getUserInfo();
        if(ujso != null && date.length() != 0 && title.length() != 0)
        {
            try {
                jso.put("name",ujso.getString("name"));
                jso.put("uuid",ujso.getString("uuid"));
                jso.put("date",date);
                jso.put("title",title);
            } catch (JSONException e) {
                e.printStackTrace();
                //异常处理
            }
        }
        return false;
    }

}
