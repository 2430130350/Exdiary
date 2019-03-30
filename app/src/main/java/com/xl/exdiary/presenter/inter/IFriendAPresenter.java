package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.User;

public interface IFriendAPresenter {
    /*
    以下获得好友的增删改查
     */
    public User getFriend(String name, String uuid);//获得朋友信息
    public User[] getAllFriend();//获得所有朋友信息
    public boolean delFriends(String uuid);//删除朋友
    public boolean modifyFriend(String name, String uuid, String mail, String signature);//修改用户信息
    public boolean saveFriend(String name, String uuid, String mail, String signature);//保存好友信息

    /*
    以下完成好友添加和请求
     */
    public User[] getFriendToSure();//返回所有的未添加的好友
    public boolean acceptFriend(String fname, String fuuid);//接受好友请求
    public boolean addFriends(String name, String uuid);//添加朋友
    public boolean rejectFriend(String fname,String fuuid);//拒绝好友请求
}
