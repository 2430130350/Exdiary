package com.xl.exdiary.presenter.inter;

import org.json.JSONObject;

public interface IEditUserPresenter {
    public boolean saveUserInfor(JSONObject userInfor);//保存用户信息
    public JSONObject getUserInfor();//获得用户信息
}
