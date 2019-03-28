package com.xl.exdiary.presenter.impl;

import android.content.Context;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.presenter.inter.IEditUserPresenter;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.view.inter.IFriendAView;
import com.xl.exdiary.view.inter.IMainAView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IEditUserPresenterImpl implements IEditUserPresenter {
    private IUserModel mIUserModel;
    private IMainAView maIMainAview;
    private IFriendAView mIFriendAView;

    public IEditUserPresenterImpl(IMainAView aIMainAView) {//主界面 activity
        mIUserModel = new UserModel();
        maIMainAview = aIMainAView;
    }

    public IEditUserPresenterImpl(IFriendAView iFriendAView){//朋友界面 activity
        mIUserModel = new UserModel();
        mIFriendAView = iFriendAView;
   }

    //返回保存的用户信息 true 表示正确保存 false 表示错误保存
    @Override
    public boolean saveUserInfor(String nickName, String mail, String signature) {
        String device = DeviceUuidFactory.getInstance((Context)maIMainAview).getDeviceUuid().toString();
        JSONObject jsoUser = new JSONObject();
        if(nickName.length() != 0 )
        {
            try {
                jsoUser.put("name",nickName);
                jsoUser.put("uuid",device);
                jsoUser.put("signature",signature);
                jsoUser.put("mail",mail);
                if(mIUserModel.saveUserInfoInLocal(jsoUser))//本地保存
                    return mIUserModel.saveUserInfoOnServer(jsoUser);//服务器保存
            } catch (JSONException e) {
                maIMainAview.exception();
                return false;
            }
        }
        else
            return false;
        return false;
    }

    //返回用户信息的 json 对象 无用户信息则为 null
    @Override
    public User getUserInfor() {
        JSONObject jsa = mIUserModel.getUserInfo();
        if( jsa != null)
        {
            User user = null;
            try {
                user = new User(jsa.getString("name"), jsa.getString("uuid")
                        ,jsa.getString("signature"), jsa.getString("mail"));
            } catch (JSONException e) {
                maIMainAview.exception();
                return null;
            }
            return user;
        }
        else
            return null;
    }

    //删除用户信息
    @Override
    public boolean delUserInfor() {
        //mIUserModel.delUser(String name, String uuid);
        return false;
    }

    //修改用户
    @Override
    public boolean updateUserInfor(String nickName, String uuid, String mail, String signature) {
        JSONObject jso = mIUserModel.getUserInfo();
        JSONObject jsoUser = new JSONObject();
        if(jso != null)
        {
            try {
                if(uuid.equals(jso.getString("uuid")))
                {
                    if(!nickName.equals(jso.getString("nickName")) || !mail.equals(jso.getString("mail"))
                            || !signature.equals(jso.getString("signature")))
                    {
                        JSONObject mjso = new JSONObject();
                        mjso.put("nickName",nickName);
                        mjso.put("uuid",uuid);
                        mjso.put("mail",mail);
                        mjso.put("signature",signature);
                        if(mIUserModel.saveUserInfoInLocal(mjso))
                            if( mIUserModel.saveUserInfoInLocal(mjso))
                                mIUserModel.saveUserInfoInLocal(null);//删除本地用户信息
                        else
                            return false;
                        //更新用户信息
                    }
                }
                else
                    return false;
            } catch (JSONException e) {
                e.printStackTrace();
                maIMainAview.exception();
                //更新异常
            }
        }
        else
            return false;
        return false;
    }

    //判断登陆
    @Override
    public boolean isLogic() {
        return mIUserModel.getUserInfo() != null;
    }
}


