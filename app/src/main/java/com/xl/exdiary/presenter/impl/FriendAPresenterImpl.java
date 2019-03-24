package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.FriendAModelImpl;
import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IFriendAModel;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.presenter.inter.IFriendAPresenter;
import com.xl.exdiary.view.inter.IFriendAView;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class FriendAPresenterImpl implements IFriendAPresenter {
    private IFriendAView mIFriendAView;
    private IUserModel mIFriendAModel;

    public FriendAPresenterImpl(IFriendAView aIFriendAView) {
        mIFriendAView = aIFriendAView;
        mIFriendAModel = new UserModel();
    }

    @Override
    public boolean addFriends(String name, String uuid, String mail, String signature) {
        //添加好友
        if(name.length() != 0 && uuid.length() != 0)
        {
            JSONObject jso = new JSONObject();
            try {
                jso.put("name",name);
                jso.put("uuid",uuid);
                jso.put("mail",mail);
                jso.put("signature",signature);
            } catch (JSONException e) {
                e.printStackTrace();
                //异常处理
            }
            return mIFriendAModel.saveFriend(jso);
        }
        else
            return false;
    }

    @Override
    public boolean delFriends(String name, String uuid, String mail, String signature) {
        //删除好友
        if(name.length() !=0 && uuid.length() != 0)
        {
            JSONObject jso = new JSONObject();
            try {
                jso.put("name",name);
                jso.put("uuid",uuid);
                jso.put("mail",mail);
                jso.put("signature",signature);
            } catch (JSONException e) {
                e.printStackTrace();
                //异常处理
            }
            return mIFriendAModel.delFriend(jso);
        }
        else
            return false;
    }

    @Override
    public User getFriend(String name, String uuid) {
        //获得好友信息
        JSONArray jsa = mIFriendAModel.getAllFriend();
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

    @Override
    public boolean modifyFriend(String name, String uuid, String mail, String signature) {
        //修改用户信息
        JSONArray jsa = mIFriendAModel.getAllFriend();
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
                       if(!jso.getString("name").equals(name)
                               || !jso.getString("uuid").equals(uuid)
                               || !jso.getString("mail").equals(mail)
                               || !jso.getString("signature").equals(signature))
                       {
                           JSONObject jst = new JSONObject();
                           jst.put("name",name);
                           jst.put("uuid",uuid);
                           jst.put("mail",mail);
                           jst.put("signature",signature);
                           return mIFriendAModel.saveFriend(jst);
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
        else
            return false;
        return false;
    }
}
