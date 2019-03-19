package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.presenter.inter.IEditUserPresenter;
import com.xl.exdiary.model.inter.IUserModel;

import org.json.JSONObject;

public class IEditUserPresenterImpl implements IEditUserPresenter {

    private IUserModel mIUserModel;
    public IEditUserPresenterImpl() {
        mIUserModel = new UserModel();
    }

    @Override
    public boolean saveUserInfor(JSONObject userInfor) {
        //返回保存的用户信息 true 表示正确保存 false 表示错误保存
        if(userInfor != null && mIUserModel != null)
        {
                if (mIUserModel.saveUserInfo(userInfor))
                {
                    return true;
                }
                else
                    return false;
        }
        else
            return false;
    }

    @Override
    public JSONObject getUserInfor() {
        //返回用户信息的 json 对象 无用户信息则为 null
        JSONObject jsa = null;
        if( mIUserModel != null)
        {
            jsa = mIUserModel.getUserInfo();
            if ( jsa != null)
                return jsa;
            else
                return null;
        }
        else
            return null;
    }

}
