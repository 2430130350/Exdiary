package com.xl.exdiary.presenter.inter;
import com.xl.exdiary.model.impl.User;

import org.json.JSONException;

public interface IEditUserPresenter {
    public boolean saveUserInfor(String nickName, String device, String mail, String autograph) throws JSONException;//保存用户信息
    public User getUserInfor() throws JSONException;//获得用户信息
}
