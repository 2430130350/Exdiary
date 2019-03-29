package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.presenter.inter.IFriendAPresenter;
import com.xl.exdiary.view.inter.IFriendAView;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class FriendAPresenterImpl implements IFriendAPresenter {
    private IFriendAView mIFriendAView;
    private IUserModel mIUserModel;

    public FriendAPresenterImpl(IFriendAView aIFriendAView) {
        mIFriendAView = aIFriendAView;
        mIUserModel = new UserModel();
    }

    //添加好友
    @Override
    public boolean addFriends(String name, String uuid) {
        JSONObject mjso = mIUserModel.getUserInfo();
        JSONObject fjso = new JSONObject();
        if(uuid.length() != 0)
        {
            try {
                fjso.put("name",name);
                fjso.put("uuid",uuid);
                fjso.put("mail",null);
                fjso.put("signature",null);
                //接口未定义
                if(mIUserModel.saveUserInfoOnServer(fjso))
                if(!mIUserModel.saveUserInfoInLocal(fjso))//进一步实现好友请求通过步骤，
                    /*
                     *  好友请求确认步骤
                     */
                    mIUserModel.delFriendInLocal(fjso);//服务器添加失败， 同步删除本地
                else
                        return true;
            } catch (JSONException e) {
                e.printStackTrace();
                //异常处理
            }
        }
        else
            return false;
        return false;
    }

    //删除好友
    @Override
    public boolean delFriends(String uuid) {
        if(uuid.length() != 0)
        {
            JSONArray jsa = mIUserModel.getAllFriend();
            JSONObject mjso = mIUserModel.getUserInfo();
            JSONObject fjso;
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                fjso = jsa.getJSONObject(i);
                if (fjso.getString("uuid").equals(uuid))
                {
                    if(mIUserModel.delFriendOnServer(mjso.getString("uuid"), uuid))
                        if(mIUserModel.delFriendInLocal(fjso))
                            return true;
                        else
                            return false;
                    else
                        return false;
                }
                } catch (JSONException e) {
                      e.printStackTrace();
                      //异常处理，删除异常
                    }
            }
        }
        return false;
    }

    //获得好友信息
    @Override
    public User getFriend(String name, String uuid) {
        JSONArray jsa = mIUserModel.getAllFriend();
        JSONObject jso = null;
        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                    jso = jsa.getJSONObject(i);
                    if(jso.getString("name").equals(name)
                            && jso.getString("uuid").equals(uuid))
                    {
                        return new User(jso.getString("name"), jso.getString("uuid"),
                                jso.getString("signature"), jso.getString("mail"));
                    }
                } catch (JSONException e) {
                    //异常处理
                    e.printStackTrace();
                }
            }
        }
        else
            return null;
        return null;
    }

    //获得所有好友信息
    @Override
    public User[] getAllFriend() {
        JSONArray jsa = mIUserModel.getAllFriend();
        JSONObject jso = null;
        User user[] = new User[jsa.length()];
        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                    jso = jsa.getJSONObject(i);
                     user[i] = new User(jso.getString("name"), jso.getString("uuid"),
                                jso.getString("signature"), jso.getString("mail"));
                    } catch (JSONException e) {
                    e.printStackTrace();
                    //异常处理
                }
            }
            return user;
        }
        else
            return null;
    }

    //修改用户信息 保证 uuid 号不变
    @Override
    public boolean modifyFriend(String name, String uuid, String mail, String signature) {
        JSONArray jsa = mIUserModel.getAllFriend();
        JSONObject jso = null;
        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                    jso = jsa.getJSONObject(i);
                    if(jso.getString("uuid").equals(uuid))
                    {
                       if(!jso.getString("name").equals(name)
                               || !jso.getString("mail").equals(mail)
                               || !jso.getString("signature").equals(signature))
                       {
                           JSONObject jst = new JSONObject();
                           jst.put("name",name);
                           jst.put("uuid",uuid);
                           jst.put("mail",mail);
                           jst.put("signature",signature);
                           if(mIUserModel.saveUserInfoInLocal(jst))
                               if(mIUserModel.saveUserInfoOnServer(jst))
                                   return true;
                               else
                                   return false;
                           else
                               return false;
                       }
                       else
                           return false;
                    }
                } catch (JSONException e) {
                    //异常处理
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
