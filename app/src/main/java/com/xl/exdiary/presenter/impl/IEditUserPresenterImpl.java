package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.presenter.inter.IEditUserPresenter;
import com.xl.exdiary.model.inter.IUserModel;

import org.json.JSONException;
import org.json.JSONObject;

public class IEditUserPresenterImpl implements IEditUserPresenter {

    private IUserModel mIUserModel;
    public IEditUserPresenterImpl() {
        mIUserModel = new UserModel();
    }

    //返回保存的用户信息 true 表示正确保存 false 表示错误保存
    @Override
    public boolean saveUserInfor(String nickName, String device, String mail, String autograph) throws JSONException {
        JSONObject jso = mIUserModel.getUserInfo();
        if(jso.length() != 0
                || !jso.getString("nickName").equals(nickName) || !jso.getString("device").equals(device)
                || !jso.getString("mail").equals(mail) || !jso.getString("autograph").equals(autograph) )
        {//用户信息改变
            JSONObject jsoUser = new JSONObject();
            jsoUser.put("nickName",nickName);
            jsoUser.put("device",device);
            jsoUser.put("mail",mail);
            jsoUser.put("autograph",autograph);
            return mIUserModel.saveUserInfo(jsoUser);
        }
        else {//用户信息未改变
            return false;
        }
    }

    @Override
    public UserModel getUserInfor() throws JSONException {
        //返回用户信息的 json 对象 无用户信息则为 null
        JSONObject jsa = mIUserModel.getUserInfo();
        if( jsa.length() != 0)
        {
            UserModel user = new UserModel(jsa.getString("nickName"), jsa.getString("device"),
                    jsa.getString("mail"), jsa.getString("autograph"));
                return user;
        }
        else
            return null;
    }
}
