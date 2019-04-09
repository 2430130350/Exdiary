package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.Diary;
import com.xl.exdiary.model.impl.ShareDiary;

public interface IShareAPresenter {
    /*
        分享日记

        输入： 好友信息  name  uuid   个人信息  date  （内容不可为空）

        输出： 正确执行 true
               错误执行 false

         异常： 暂无异常处理
     */
    public boolean shareDiary(String name, String uuid, String date);

    /*
        获得自己分享的日记

        输入：

        输出： shareDiary[]

        异常： 未对异常做处理
     */
    public ShareDiary[] getMyShareDiary();

    /*
         获得朋友分享的日记

        输入：

        输出： shareDiary[]

        异常： 未对异常做处理
     */
    public ShareDiary[] getFriendDiary();

    /*
         获得所有的分享的日记（先己后人）

        输入：

        输出： shareDiary[]

        异常： 未对异常做处理
     */
    public ShareDiary[] getAllDiary();

    /*
        取消分享日记

        输入： 好友信息 name  自己信息 uuid  日记日期 date   （不可为空)

        输出： true  false

        异常： 未对异常做处理
     */
    public boolean disableShareDiary(String name, String uuid, String date);//取消分享日记
}
