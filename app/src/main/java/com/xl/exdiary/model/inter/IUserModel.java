package com.xl.exdiary.model.inter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IUserModel {
    public boolean saveUserInfoInLocal(JSONObject userInfo);//本地保存用户信息
    public boolean saveUserInfoOnServer(JSONObject userInfo);//云端保存用户信息
    public JSONObject getUserInfo();
    public boolean saveFriendInLocal(JSONObject friend);//本地保存好友信息
    public boolean delFriendInLocal(JSONObject friend);//本地删除好友
    public JSONArray getAllFriend();
    public boolean delFriendOnServer(String myUUID,String friendUUID);//云端删除好友
    public boolean addFriendOnServer(String myUUID,String friendUUID);//云端好友申请
    public boolean acceptFriendRequest(String myUUID,String friendUUID);//同意好友申请
    public boolean rejectFriendRequest(String myUUID,String friendUUID);//拒绝好友申请
    public JSONArray getAllFriendOnServer(String myUUID);//获取云端好友信息
}
