package com.xl.exdiary.presenter.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.presenter.inter.IEditUserPresenter;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.model.impl.User;
import org.json.JSONException;
import org.json.JSONObject;

public class IEditUserPresenterImpl implements IEditUserPresenter {
    private IUserModel mIUserModel;
    public IEditUserPresenterImpl() {
        mIUserModel = new UserModel();
    }

    //返回保存的用户信息 true 表示正确保存 false 表示错误保存
    @Override
    public boolean saveUserInfor(String nickName, String mail, String signature) throws JSONException {
        String device = DeviceUuidFactory.getInstance(mEditUserActivity).getDeviceUuid();
        JSONObject jso = mIUserModel.getUserInfo();
        if(jso.length() != 0
                || !jso.getString("name").equals(nickName) || !jso.getString("deviceNumber").equals(device)
                || !jso.getString("mail").equals(mail) || !jso.getString("signature").equals(signature) )
        {//用户信息改变
            JSONObject jsoUser = new JSONObject();
            jsoUser.put("name",nickName);
            jsoUser.put("deviceNumber",device);
            jsoUser.put("mail",mail);
            jsoUser.put("signature",signature);
            return mIUserModel.saveUserInfo(jsoUser);
        }
        else {//用户信息未改变
            return false;
        }
    }

    @Override
    public User getUserInfor() throws JSONException {
        //返回用户信息的 json 对象 无用户信息则为 null
        JSONObject jsa = mIUserModel.getUserInfo();
        if( jsa.length() != 0)
        {
            User user = new User(jsa.getString("name"), jsa.getString("deviceNumber")
                    ,jsa.getString("signature"), jsa.getString("mail"));
                return user;
        }
        else
            return null;
    }
}


