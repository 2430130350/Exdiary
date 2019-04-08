package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.Diary;
import com.xl.exdiary.model.impl.DiaryModel;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IDiaryModel;
import com.xl.exdiary.model.inter.IShareDiaryModel;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.model.impl.ShareDiaryModel;
import com.xl.exdiary.presenter.inter.IShareAPresenter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class IShareAPresenterImpl implements IShareAPresenter {
    private IUserModel mIUserModel;
    private IShareDiaryModel miShareDiaryModel;
    private IDiaryModel miDiaryModel;
    public IShareAPresenterImpl() {
        this.mIUserModel = new UserModel();
        miShareDiaryModel = new ShareDiaryModel();
        miDiaryModel = new DiaryModel();
    }

    //分享日记
    @Override
    public boolean shareDiary(String name, String uuid, String date) {
        JSONObject jso = mIUserModel.getUserInfo();
        JSONObject djso;//临时变量
        JSONObject sjso = new JSONObject();//分享的日记
        JSONArray jsa = miDiaryModel.getAllDiaryList();
        if(name.length() != 0 && uuid.length() != 0 /// 保证数据完整
                && date.length() != 0 && jso != null)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                    djso = jsa.getJSONObject(i);
                    if(djso.getString("date").equals(date))
                    {
                        sjso.put("myUuid",djso.getString("uuid"));
                        sjso.put("friendUuid",uuid);
                        sjso.put("shareDate",djso.getString("date"));
                        sjso.put("shareTitle",djso.getString("title"));
                        sjso.put("shareBody",djso.getString("body"));
                        return miShareDiaryModel.shareDiary(sjso);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //分享异常处理
                }
            }
        }
        else
            return false;
        return false;
    }

    //获得我的分享日记
    @Override
    public Diary[] getMyShareDiary() {
        JSONObject ujso = mIUserModel.getUserInfo();
        JSONObject jso;
        JSONArray jsa = null;
        int counts = 0;
        try {
            jsa = miShareDiaryModel.getAllShareDiary(ujso.getString("uuid"));
        } catch (JSONException e) {
            e.printStackTrace();
            //获取所有分享日记错误处理
        }

        Diary[] diary = new Diary[jsa.length()];

        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {

                try {
                    jso = jsa.getJSONObject(i);
                    if(jso.getString("myUuid").equals(ujso.getString("uuid")))
                    {
                        counts += 1;
                        diary[i] = new Diary(jso.getString("shareTitle"),
                                jso.getString("shareBody"), jso.getString("shareDate"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //异常处理
                }

            }
            Diary[] tdiary = new Diary[counts];
            for(int t = 0; t < counts; t++){
                tdiary[t] = diary[t];
            }
            return tdiary;
        }
        else
            return null;
    }

    //获得朋友分享的日记
    @Override
    public Diary[] getFriendDiary() {
        JSONObject ujso = mIUserModel.getUserInfo();
        JSONObject jso;
        JSONArray jsa = null;
        int counts = 0;
        try {
            jsa = miShareDiaryModel.getAllShareDiary(ujso.getString("uuid"));
        } catch (JSONException e) {
            e.printStackTrace();
            //获取所有分享日记错误处理
        }

        Diary[] diary = new Diary[jsa.length()];

        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                    jso = jsa.getJSONObject(i);
                    if(!jso.getString("myUuid").equals(ujso.getString("uuid")))
                    {
                        counts += 1;
                        diary[i] = new Diary(jso.getString("shareTitle"),
                                jso.getString("shareBody"), jso.getString("shareDate"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //异常处理
                }
            }
            Diary[] tdiary = new Diary[counts];
            for(int t = 0; t < counts; t++){
                tdiary[t] = diary[t];
            }
            return tdiary;
        }
        else
            return null;
    }

    //获得所有的分享日记
    @Override
    public Diary[] getAllDiary() {
        JSONObject ujso = mIUserModel.getUserInfo();
        JSONObject jso;
        JSONArray jsa = null;
        try {
            jsa = miShareDiaryModel.getAllShareDiary(ujso.getString("uuid"));
        } catch (JSONException e) {
            e.printStackTrace();
            //获取所有分享日记错误处理
        }

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

    //取消分享的日记
    @Override
    public boolean disableShareDiary(String name, String uuid, String date) {
        //取消分享日记
        JSONObject jso = mIUserModel.getUserInfo();
        JSONObject djso;//临时变量
        JSONObject sjso = new JSONObject();//分享的日记
        JSONArray jsa = miDiaryModel.getAllDiaryList();
        if (name.length() != 0 && uuid.length() != 0 /// 保证数据完整
                && date.length() != 0 && jso.length() != 0) {
            for (int i = 0; i < jsa.length(); i++) {
                try {
                    djso = jsa.getJSONObject(i);
                    if(djso.getString("myUuid").equals(jso.getString("uuid"))){
                        if (djso.getString("date").equals(date)) {
                            //取消分享日记
                            return miShareDiaryModel.disableShareDiary(djso);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //分享异常处理
                }
            }
        } else
            return false;
        return false;
    }
}
