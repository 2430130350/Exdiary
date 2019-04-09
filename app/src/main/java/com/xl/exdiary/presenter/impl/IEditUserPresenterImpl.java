package com.xl.exdiary.presenter.impl;

import android.content.Context;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.presenter.inter.IEditUserPresenter;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.view.activity.AnonymousActivity;
import com.xl.exdiary.view.inter.IAnonymousAView;
import com.xl.exdiary.view.inter.IFriendAView;
import com.xl.exdiary.view.inter.IMainAView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class IEditUserPresenterImpl implements IEditUserPresenter {
    private IUserModel mIUserModel = null;
    private IMainAView maIMainAview = null;
    private IFriendAView mIFriendAView = null;
    private IAnonymousAView iAnonymousAView = null;

    public IEditUserPresenterImpl(IMainAView aIMainAView) {//主界面 activity
        mIUserModel = new UserModel();
        maIMainAview = aIMainAView;
    }

    public IEditUserPresenterImpl(IFriendAView iFriendAView){//朋友界面 activity
        mIUserModel = new UserModel();
        mIFriendAView = iFriendAView;
   }

    public IEditUserPresenterImpl(AnonymousActivity anonymousActivity){//树洞界面 activity
        iAnonymousAView = anonymousActivity;
        mIUserModel = new UserModel();
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
                if(maIMainAview != null )
                    maIMainAview.exception();
                else if(mIFriendAView != null)
                    mIFriendAView.exception();
                else if(iAnonymousAView != null)
                    iAnonymousAView.exception(); ;//树洞层异常返回
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
                        JSONObject mjso = new JSONObject();
                        mjso.put("name",nickName);
                        mjso.put("uuid",uuid);
                        mjso.put("mail",mail);
                        mjso.put("signature",signature);
                        if(mIUserModel.saveUserInfoOnServer(mjso))
                            //服务器更新失败， 同步删除本地用户信息（未处理）
                            return mIUserModel.saveUserInfoInLocal(mjso);
                        //更新用户信息
                }
                else
                    return false;
            } catch (JSONException e) {
                e.printStackTrace();
                if(maIMainAview != null)
                    maIMainAview.exception();
                else//其它异常捕获
                    ;
                //更新异常
            }
        }
        return false;
    }

    //判断登陆
    @Override
    public boolean isLogin() {
        return mIUserModel.getUserInfo() != null;
    }

}


