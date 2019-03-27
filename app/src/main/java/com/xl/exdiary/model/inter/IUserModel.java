package com.xl.exdiary.model.inter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IUserModel {
    public boolean saveUserInfoInLocal(JSONObject userInfo);
    public boolean saveUserInfoOnServer(JSONObject userInfo);
    public JSONObject getUserInfo();
    public boolean saveFriendInLocal(JSONObject friend);
    public boolean delFriendInLocal(JSONObject friend);
    public JSONArray getAllFriend();
    public boolean delFriendOnServer(String myUUID,String friendUUID);
    public boolean addFriendOnServer(String myUUID,String friendUUID);
}
