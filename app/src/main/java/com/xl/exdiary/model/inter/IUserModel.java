package com.xl.exdiary.model.inter;

import org.json.JSONObject;

public interface IUserModel {
    public boolean saveUserInfo(JSONObject userInfo);
    public JSONObject getUserInfo();
}
