package com.xl.exdiary.presenter.inter;
import com.xl.exdiary.model.impl.User;

import org.json.JSONArray;
import org.json.JSONException;

public interface IEditUserPresenter {
    public boolean saveUserInfor(String nickName, String mail, String signature) ;//保存用户信息
    public User getUserInfor();//获得用户信息
    public boolean delUserInfor();//删除用户信息
    public boolean updateUserInfor(String nickName, String uuid, String mail, String signature);//更新用户信息
    public boolean isLogin();//检查用户是否登陆
}
