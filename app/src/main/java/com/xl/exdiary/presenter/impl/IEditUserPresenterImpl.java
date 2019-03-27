package com.xl.exdiary.presenter.impl;

import android.content.Context;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.presenter.inter.IEditUserPresenter;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.view.inter.IFriendAView;
import com.xl.exdiary.view.inter.IMainAView;
import org.json.JSONException;
import org.json.JSONObject;

public class IEditUserPresenterImpl implements IEditUserPresenter {
    private IUserModel mIUserModel;
    private IMainAView maIMainAview;
    private IFriendAView mIFriendAView;

    public IEditUserPresenterImpl(IMainAView aIMainAView) {//主界面 activity
        mIUserModel = new UserModel();
        maIMainAview = aIMainAView;
    }

    public IEditUserPresenterImpl(IFriendAView iFriendAView){//朋友界面 activity
        mIUserModel = new UserModel();
        mIFriendAView = iFriendAView;
   }

    //返回保存的用户信息 true 表示正确保存 false 表示错误保存
    @Override
    public boolean saveUserInfor(String nickName, String mail, String signature) {
        String device = DeviceUuidFactory.getInstance((Context)maIMainAview).getDeviceUuid().toString();
        JSONObject jso = mIUserModel.getUserInfo();
        JSONObject jsoUser = new JSONObject();
        if(nickName.length() != 0 )
        {
                try {
                    jsoUser.put("name",nickName);
                    jsoUser.put("uuid",device);
                    jsoUser.put("signature",signature);
                    jsoUser.put("mail",mail);
                } catch (JSONException e) {
                    maIMainAview.exception();
                    return false;
                }
                if(jso == null)
                {//当前无用户
                    if(mIUserModel.saveUserInfoInLocal(jsoUser))
                        if(mIUserModel.saveUserInfoOnServer(jsoUser))
                            return true;
                        else
                            return false;
                    return false;
                }
                else
                {
                    try {
                        if( !jso.getString("name").equals(nickName) || !jso.getString("uuid").equals(device)
                                || !jso.getString("mail").equals(mail) || !jso.getString("signature").equals(signature) )
                        {//用户信息改变
                            if(mIUserModel.saveUserInfoInLocal(jsoUser))//本地更新
                                return mIUserModel.saveUserInfoOnServer(jsoUser);//服务器更新
                            return false;//本地更新失败，返回
                        }
                        else {//用户信息未改变
                            return true;
                        }
                    } catch (JSONException e) {
                        maIMainAview.exception();
                        return false;
                    }
                }
        }
        else
            return false;
    }

    @Override
    public User getUserInfor() {
        //返回用户信息的 json 对象 无用户信息则为 null
        JSONObject jsa = mIUserModel.getUserInfo();
        if( jsa != null)
        {
            User user = null;
            try {
                user = new User(jsa.getString("name"), jsa.getString("uuid")
                        ,jsa.getString("signature"), jsa.getString("mail"));
            } catch (JSONException e) {
                maIMainAview.exception();
                return null;
            }
            return user;
        }
        else
            return null;
    }

    @Override
    public boolean delUserInfor() {//删除用户信息
        //mIUserModel.delUser(String name, String uuid);
        return false;
    }
}


