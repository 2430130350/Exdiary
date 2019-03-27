package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.Diary;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IShareDiaryModel;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.presenter.inter.IShareAPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class IShareAPresenterImpl implements IShareAPresenter {
    private IUserModel mIUserModel;
    private IShareDiaryModel miShareDiaryModel;
    public IShareAPresenterImpl() {
        this.mIUserModel = new UserModel();
        miShareDiaryModel = new ShareDiaryModel();
    }

    @Override
    public boolean shareDiary(String name, String uuid, String date) {
        JSONObject jso = mIUserModel.getUserInfo();
        if(name.length() != 0 && uuid.length() != 0 && date.length() != 0
                && miShareDiaryModel != null && jso.length() != 0)
        {
            return miShareDiaryModel.shareDiary(jso);
        }
        else
        {
            return false;
        }
    }

    @Override
    public Diary[] getMyShareDiary() {
        JSONArray jsa = miShareDiaryModel.getAllShareDiary();
        JSONObject ujso = mIUserModel.getUserInfo();
        JSONObject jso;
        Diary[] diary = new Diary[jsa.length()];
        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                    jso = jsa.getJSONObject(i);
                    if(jso.getString("uuid").equals(ujso.getString("uuid")))
                    {
                        diary[i] = new Diary(jso.getString("title"),
                                jso.getString("body"), jso.getString("date"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //异常处理
                }
            }
            return diary;
        }
        else
            return null;
    }

    @Override
    public Diary[] getFriendDiary() {
        JSONArray jsa = miShareDiaryModel.getFriendDiary();
        JSONObject jso;
        Diary[] diary = new Diary[jsa.length()];
        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
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
        else
            return null;
    }

    @Override
    public Diary[] getAllDiary() {
        Diary[] mdiary = getMyShareDiary();
        Diary[] fdiary = getFriendDiary();
        Diary[] diary = new Diary[mdiary.length + fdiary.length];
        int i = 0;
        if(mdiary != null && fdiary != null)
        {
            for( i = 0; i < mdiary.length; i++)
            {
                diary[i] = mdiary[i];
            }
            for(int t = i; t < (mdiary.length + fdiary.length); t++)
            {
                diary[t] = fdiary[t-i];
            }
            return diary;
        }
        else
            return null;
    }

    @Override
    public boolean diaableShareDiary(String name, String uuid, String date) {
        //取消分享日记
        if(name.length() != 0 && uuid.length() != 0 && date.length() != 0)
        {
            return miShareDiaryModel.disableShareDiary(name, uuid, date);
        }
        else
            return false;
    }
}
