package com.xl.exdiary.presenter.impl;

import android.content.Context;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.presenter.inter.IEditUserPresenter;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.view.inter.IMainAView;
import org.json.JSONException;
import org.json.JSONObject;

public class IEditUserPresenterImpl implements IEditUserPresenter {
    private IUserModel mIUserModel;
    private IMainAView maIMainAview;

    public IEditUserPresenterImpl(IMainAView aIMainAView) {//未定义 activity
        mIUserModel = new UserModel();
        maIMainAview = aIMainAView;
    }

    //返回保存的用户信息 true 表示正确保存 false 表示错误保存
    @Override
    public boolean saveUserInfor(String nickName, String mail, String signature) {
        String device = DeviceUuidFactory.getInstance((Context)maIMainAview).getDeviceUuid().toString();
        JSONObject jso = mIUserModel.getUserInfo();
        JSONObject jsoUser = new JSONObject();
        try {
            jsoUser.put("name",nickName);
            jsoUser.put("deviceNumber",device);
            jsoUser.put("signature",signature);
            jsoUser.put("mail",mail);
        } catch (JSONException e) {
            maIMainAview.exception();
            return false;
        }
        if(jso.length() == 0)
        {//当前无用户
            return mIUserModel.saveUserInfo(jsoUser);
        }
        else
        {
            try {
                if( !jso.getString("name").equals(nickName) || !jso.getString("deviceNumber").equals(device)
                        || !jso.getString("mail").equals(mail) || !jso.getString("signature").equals(signature) )
                {//用户信息改变
                    return mIUserModel.saveUserInfo(jsoUser);
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

    @Override
    public User getUserInfor() {
        //返回用户信息的 json 对象 无用户信息则为 null
        JSONObject jsa = mIUserModel.getUserInfo();
        if( jsa.length() != 0)
        {
            User user = null;
            try {
                user = new User(jsa.getString("name"), jsa.getString("deviceNumber")
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

        return false;
    }
}


