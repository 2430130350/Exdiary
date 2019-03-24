package com.xl.exdiary.model.inter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IUserModel {
    public boolean saveUserInfo(JSONObject userInfo);
    public JSONObject getUserInfo();
    public boolean saveFriend(JSONObject friend);
    public boolean delFriend(JSONObject friend);
    public JSONArray getAllFriend();
}
