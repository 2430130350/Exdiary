package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.User;

public interface IFriendAPresenter {
    public boolean addFriends(String uuid);//添加朋友
    public boolean delFriends(String uuid);//删除朋友
    public User getFriend(String uuid);//获得朋友信息
    public User[] getAllFriend();//获得所以朋友信息
    public boolean modifyFriend(String name, String uuid, String mail, String signature);//修改用户信息
}
