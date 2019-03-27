package com.xl.exdiary.model.inter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IUserModel {
    public boolean saveUserInfo(JSONObject userInfo);
    public boolean saveUserInfoOnServer(JSONObject userInfo);
    public JSONObject getUserInfo();
    public boolean saveFriend(JSONObject friend);
    public boolean delFriend(JSONObject friend);
    public JSONArray getAllFriend();
    public boolean delFriendOnServer(String myUUID,String friendUUID);
    public boolean addFriend(String myUUID,String friendUUID);
}
