package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.User;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.presenter.inter.IFriendAPresenter;
import com.xl.exdiary.view.activity.AnonymousActivity;
import com.xl.exdiary.view.inter.IFriendAView;
import com.xl.exdiary.view.inter.IMainAView;
import org.json.JSONObject;
import org.json.JSONArray;

public class FriendAPresenterImpl implements IFriendAPresenter {
    private IFriendAView mIFriendAView = null;
    private IUserModel mIUserModel;
    private IMainAView maIMainAview = null;
    private AnonymousActivity iAnonymousAView = null;


    public FriendAPresenterImpl(IMainAView aIMainAView) {//主界面 activity
        mIUserModel = new UserModel();
        maIMainAview = aIMainAView;
        mIUserModel = new UserModel();
    }

    public FriendAPresenterImpl(IFriendAView iFriendAView){//朋友界面 activity
        mIUserModel = new UserModel();
        mIFriendAView = iFriendAView;
        mIUserModel = new UserModel();
    }

    public FriendAPresenterImpl(AnonymousActivity anonymousActivity){//树洞界面 activity
        iAnonymousAView = anonymousActivity;
        mIUserModel = new UserModel();
        mIUserModel = new UserModel();
    }

    //添加好友
    @Override
    public boolean addFriends(String name, String uuid) {
        JSONObject jso = mIUserModel.getUserInfo();
        if(jso != null)
        {
            try {
                mIUserModel.addFriendOnServer(jso.getString("uuid"),uuid);
                return true;
            } catch (Exception e) {
                //异常管理
                if(maIMainAview != null)
                {
                    maIMainAview.exception();
                }else if(mIFriendAView != null){
                    mIFriendAView.exception();
                } else if(iAnonymousAView != null) {
                    iAnonymousAView.exception();
                }else{
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    //删除好友,已完成
    @Override
    public boolean delFriends(String uuid) {
        if(uuid.length() != 0)
        {
            JSONArray jsa = mIUserModel.getAllFriend();
            JSONObject mjso = mIUserModel.getUserInfo();
            JSONObject fjso;
            if(mjso != null){
                for(int i = 0; i < jsa.length(); i++)
                {
                    try {
                        fjso = jsa.getJSONObject(i);
                        if (fjso.getString("uuid").equals(uuid))
                        {
                            if(mIUserModel.delFriendOnServer(mjso.getString("uuid"), uuid))//服务器删除好友
                                return mIUserModel.delFriendInLocal(fjso);//本地删除好友
                            else
                                return false;
                        }
                    } catch (Exception e) {
                        //异常处理
                        if(maIMainAview != null){
                            maIMainAview.exception();
                        }else if(mIFriendAView != null){
                            mIFriendAView.exception();
                        }else if(iAnonymousAView != null){
                            iAnonymousAView.exception();
                        }else{
                            e.printStackTrace();
                        }
                    }
            }
            }
        }
        return false;
    }

    //获得好友信息
    @Override
    public User getFriend(String name, String uuid) {
        JSONArray jsa = mIUserModel.getAllFriend();
        JSONObject jso = null;
        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                    jso = jsa.getJSONObject(i);
                    if(jso.getString("uuid").equals(uuid))
                    {
                        return new User(jso.getString("name"), jso.getString("uuid"),
                                jso.getString("signature"), jso.getString("mail"));
                    }
                } catch (Exception e) {
                    //异常处理
                    if(maIMainAview != null){
                        maIMainAview.exception();
                    }else if(mIFriendAView != null){
                        mIFriendAView.exception();
                    }else if(iAnonymousAView != null){
                        iAnonymousAView.exception();
                    }else{
                        e.printStackTrace();
                    }
                }
            }
        }
        else
            return null;
        return null;
    }

    //获得所有好友信息
    @Override
    public User[] getAllFriend() {
        JSONArray jsa = mIUserModel.getAllFriend();
        User[] friendonServer = getFriends();
        JSONObject jso = null;
        User user[] = new User[friendonServer.length];
        int counts = 0;//记录好友数目
        try {
            if (friendonServer.length != 0) {
                for (int t = 0; t < friendonServer.length; t++) {
                    User u = friendonServer[t];
                    if (jsa.length() != 0) {
                        for (int i = 0; i < jsa.length(); i++) {
                            jso = jsa.getJSONObject(i);
                            if (u.getDeviceNumber().equals(jso.getString("uuid"))) {
                                user[counts] = new User(u.getName(), u.getDeviceNumber(),
                                        u.getSignature(), u.getMail());
                                counts += 1;
                            } else {//本地无好友信息
                                user[counts] = new User(u.getName(), u.getDeviceNumber(),
                                        u.getSignature(), u.getMail());
                                counts += 1;
                            }
                        }
                    } else {
                        user[counts] = new User(u.getName(), u.getDeviceNumber(),
                                u.getSignature(), u.getMail());
                        counts += 1;
                    }
                }
                return user;
            }
        } catch (Exception e) {
            //异常处理
            if (maIMainAview != null) {
                maIMainAview.exception();
            } else if (mIFriendAView != null) {
                mIFriendAView.exception();
            } else if (iAnonymousAView != null) {
                iAnonymousAView.exception();
            } else {
                e.printStackTrace();
            }
        }
        return new User[0];
    }

    //修改用户信息 保证 uuid 号不变
    @Override
    public boolean modifyFriend(String name, String uuid, String mail, String signature) {
        JSONArray jsa = mIUserModel.getAllFriend();
        JSONObject jso = null;
        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                try {
                    jso = jsa.getJSONObject(i);
                    if(jso.getString("uuid").equals(uuid))
                    {
                           JSONObject jst = new JSONObject();
                           jst.put("name",name);
                           jst.put("uuid",uuid);
                           jst.put("mail",mail);
                           jst.put("signature",signature);
                           return mIUserModel.saveFriendInLocal(jst);
                    }
                } catch (Exception e) {
                    //异常处理
                    if(maIMainAview != null){
                        maIMainAview.exception();
                    }else if(mIFriendAView != null){
                        mIFriendAView.exception();
                    }else if(iAnonymousAView != null){
                        iAnonymousAView.exception();
                    }else{
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    //保存好友信息
    @Override
    public boolean saveFriend(String name, String uuid, String mail, String signature) {
        JSONObject mjso = mIUserModel.getUserInfo();
        JSONObject fjso = new JSONObject();
        if(uuid.length() != 0)
        {
            try {
                fjso.put("name",name);
                fjso.put("uuid",uuid);
                fjso.put("mail",mail);
                fjso.put("signature",signature);
                //接口未定义
               return  mIUserModel.saveFriendInLocal(fjso);
            } catch (Exception e) {
                //异常处理
                if(maIMainAview != null){
                    maIMainAview.exception();
                }else if(mIFriendAView != null){
                    mIFriendAView.exception();
                }else if(iAnonymousAView != null){
                    iAnonymousAView.exception();
                }else{
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    //获取云端好友请求信息
    @Override
    public User[] getFriendToSure() {
        JSONObject jso = mIUserModel.getUserInfo();
        JSONArray jsa = null;
        JSONObject tjso ;
        int counts = 0;
        try {
        jsa = mIUserModel.getAllFriendOnServer(jso.getString("uuid"));
        User user[] = new User[jsa.length()];
        if(jsa.length() != 0)
        {
            for(int i = 0; i < jsa.length(); i++)
            {
                tjso = jsa.getJSONObject(i);
                if(tjso.getInt("requested") == 0) {
                    user[i] = new User(tjso.getString("username"),
                            tjso.getString("friendID"),
                            tjso.getString("motto"),
                            tjso.getString("mail"));
                        counts+=1;
                }
            }
            User[] tuser = new User[counts];
            for(int t = 0; t < counts; t++){
                tuser[t] = user[t];
            }
            return tuser;
        }
        } catch (Exception e) {
            //异常处理
            if(maIMainAview != null){
                maIMainAview.exception();
            }else if(mIFriendAView != null){
                mIFriendAView.exception();
            }else if(iAnonymousAView != null){
                iAnonymousAView.exception();
            }else{
                e.printStackTrace();
            }
         }
        return new User[0];
    }

    //返回所有好友信息
    @Override
    public User[] getFriends() {
        JSONObject jso = mIUserModel.getUserInfo();
        JSONArray jsa = null;
        JSONObject tjso = null;
        try {
        if(jso != null)
        {  
            jsa = mIUserModel.getAllFriendOnServer(jso.getString("uuid"));
            User user[] = new User[jsa.length()];
            for(int i = 0; i < jsa.length(); i++)
            {
                tjso = jsa.getJSONObject(i);
                if(tjso.getInt("requested") == 1)
                    user[i] = new User(tjso.getString("username"),
                            tjso.getString("friendID"),
                            tjso.getString("motto"),
                            tjso.getString("mail"));
            }
            return user;
        }
        } catch (Exception e) {
            //异常处理
            if(maIMainAview != null){
                maIMainAview.exception();
            }else if(mIFriendAView != null){
                mIFriendAView.exception();
            }else if(iAnonymousAView != null){
                iAnonymousAView.exception();
            }else{
                e.printStackTrace();
            }
         }
            return new User[0];
    }

    //同意添加好友，并且本地保存好友信息，和更新云端好友申请信息
    @Override
    public boolean acceptFriend(String fname, String fuuid, String mail, String signature) {
        JSONObject fjso = new JSONObject();
        JSONObject mjso = mIUserModel.getUserInfo();
        if(fname != null && fuuid != null)
        {
            if(saveFriend(fname, fuuid, mail, signature)) {//将朋友信息保存到本地
                try {
                    mIUserModel.acceptFriendRequest(mjso.getString("uuid"), fuuid);//云端接受请求
                    return true;
                } catch (Exception e) {
                    //异常处理
                    if(maIMainAview != null){
                        maIMainAview.exception();
                    }else if(mIFriendAView != null){
                        mIFriendAView.exception();
                    }else if(iAnonymousAView != null){
                        iAnonymousAView.exception();
                    }else{
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    //拒绝好友请求
    @Override
    public boolean rejectFriend(String fuuid) {
        JSONObject mjso = mIUserModel.getUserInfo();
        if(mjso != null && fuuid.length() != 0)
        {
            try {
                mIUserModel.rejectFriendRequest(mjso.getString("uuid"), fuuid);
                return true;
            } catch (Exception e) {
                //异常处理
                if(maIMainAview != null){
                    maIMainAview.exception();
                }else if(mIFriendAView != null){
                    mIFriendAView.exception();
                }else if(iAnonymousAView != null){
                    iAnonymousAView.exception();
                }else{
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
