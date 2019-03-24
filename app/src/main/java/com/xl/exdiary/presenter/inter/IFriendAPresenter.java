package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.User;

public interface IFriendAPresenter {
    public boolean addFriends(String name, String uuid, String mail, String signature);//添加朋友
    public boolean delFriends(String name, String uuid, String mail, String signature);//删除朋友
    public User getFriend(String name, String uuid);//获得朋友信息
    public boolean modifyFriend(String name, String uuid, String mail, String signature);//修改用户信息
}
