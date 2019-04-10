package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.Diary;
import com.xl.exdiary.model.impl.DiaryModel;
import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IDiaryModel;
import com.xl.exdiary.model.inter.IShareDiaryModel;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.model.impl.ShareDiaryModel;
import com.xl.exdiary.presenter.inter.IFriendAPresenter;
import com.xl.exdiary.presenter.inter.IShareAPresenter;
import com.xl.exdiary.view.activity.AnonymousActivity;
import com.xl.exdiary.view.inter.IFriendAView;
import com.xl.exdiary.view.inter.IMainAView;
import com.xl.exdiary.model.impl.ShareDiary;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 public class IShareAPresenterImpl implements IShareAPresenter {
    private IUserModel mIUserModel;
    private IShareDiaryModel miShareDiaryModel;
    private IDiaryModel miDiaryModel;
    private IMainAView maIMainAview;
    private IFriendAView mIFriendAView;
    private AnonymousActivity iAnonymousAView;
    private IFriendAPresenter miFriendAPresenter;

    public IShareAPresenterImpl(IMainAView aIMainAView) {//主界面 activity
        mIUserModel = new UserModel();
        maIMainAview = aIMainAView;
        miShareDiaryModel = new ShareDiaryModel();
        miDiaryModel = new DiaryModel();
        miFriendAPresenter = new FriendAPresenterImpl(maIMainAview);
    }

    public IShareAPresenterImpl(IFriendAView iFriendAView){//朋友界面 activity
        mIUserModel = new UserModel();
        mIFriendAView = iFriendAView;
        miShareDiaryModel = new ShareDiaryModel();
        miDiaryModel = new DiaryModel();
        miFriendAPresenter = new FriendAPresenterImpl(iFriendAView);
    }

    public IShareAPresenterImpl(AnonymousActivity anonymousActivity){//树洞界面 activity
        iAnonymousAView = anonymousActivity;
        mIUserModel = new UserModel();
        miShareDiaryModel = new ShareDiaryModel();
        miDiaryModel = new DiaryModel();
        miFriendAPresenter = new FriendAPresenterImpl(anonymousActivity);
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
                        sjso.put("shareDiary",djso);
                        return miShareDiaryModel.shareDiary(sjso);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //分享异常处理
                }
            }
        }
        return false;
    }

    //获得我的分享日记
    @Override
    public ShareDiary[] getMyShareDiary() {
        JSONObject ujso = mIUserModel.getUserInfo();
        JSONObject jso;
        JSONArray jsa = null;
        int counts = 0;
        try {
            jsa = miShareDiaryModel.getAllShareToDiary(ujso.getString("uuid"));
        } catch (JSONException e) {
            e.printStackTrace();
            //获取所有分享日记错误处理
        }
        ShareDiary[] diary = new ShareDiary[jsa.length()];
        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                    jso = jsa.getJSONObject(i);
                        counts += 1;
                        User user = miFriendAPresenter.getFriend("",jso.getString("friendID"));
                        diary[i] = new ShareDiary(jso.getString("shareTitle"),
                                jso.getString("shareBody"), jso.getString("shareDate"),
                                user.getName(),ujso.getString("name"),jso.getString("friendID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //异常处理
                }
            }
            ShareDiary[] tdiary = new ShareDiary[counts];
            for(int t = 0; t < counts; t++){
                tdiary[t] = diary[t];
            }
            return tdiary;
        }
        else
            return new ShareDiary[0];
    }

    //获得朋友分享的日记
    @Override
    public ShareDiary[] getFriendDiary() {
        JSONObject ujso = mIUserModel.getUserInfo();
        JSONObject jso;
        JSONArray jsa = null;
        int counts = 0;
        try {
            jsa = miShareDiaryModel.getAllShareFromDiary(ujso.getString("uuid"));
        } catch (JSONException e) {
            e.printStackTrace();
            //获取所有分享日记错误处理
        }
        ShareDiary[] diary = new ShareDiary[jsa.length()];

        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                    jso = jsa.getJSONObject(i);
                        counts += 1;
                        User user = miFriendAPresenter.getFriend("",jso.getString("friendID"));
                        diary[i] = new ShareDiary(jso.getString("shareTitle"),
                                jso.getString("shareBody"), jso.getString("shareDate"),
                               user.getName(),ujso.getString("name"),jso.getString("friendID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //异常处理
                }
            }
            ShareDiary[] tdiary = new ShareDiary[counts];
            for(int t = 0; t < counts; t++){
                tdiary[t] = diary[t];
            }
            return tdiary;
        }
        else
            return new ShareDiary[0];
    }

    //获得所有的分享日记
    @Override
    public ShareDiary[] getAllDiary() {
        JSONObject ujso = mIUserModel.getUserInfo();
        JSONObject jso;
        JSONArray jsa = null;
        ShareDiary[] mdiary = getMyShareDiary();
        ShareDiary[] fdiary = getFriendDiary();
       int i = 0;
       int t = 0;

        if((mdiary.length+fdiary.length) != 0)
        {
            ShareDiary[] diary = new ShareDiary[(mdiary.length+fdiary.length)];
            for(i = 0; i < mdiary.length; i++)
            {
                diary[i] = mdiary[i];
            }
            for(t = 0; t < fdiary.length; t++)
            {
                diary[i+t] = fdiary[t];
            }
            return diary;
        }
            return new ShareDiary[0];
    }

    //取消分享的日记
    @Override
    public boolean disableShareDiary(String name, String uuid, String date) {
        //取消分享日记
        JSONObject jso = mIUserModel.getUserInfo();
        JSONObject djso;//临时变量
        JSONObject sjso = new JSONObject();//分享的日记
        JSONArray jsa = null;

        jsa = miDiaryModel.getAllDiaryList();

        if (uuid.length() != 0 /// 保证数据完整
                && date.length() != 0 && jso != null) {
            for (int i = 0; i < jsa.length(); i++) {
                try {
                        djso = jsa.getJSONObject(i);
                        if (djso.getString("date").equals(date)) {
                            //取消分享日记
                            djso.put("myUuid",jso.getString("uuid"));
                            djso.put("friendID",uuid);
                            return miShareDiaryModel.disableShareDiary(djso);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //分享异常处理
                }
            }
        }
        return false;
    }
}
