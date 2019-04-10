package com.xl.exdiary.presenter.inter;

import com.xl.exdiary.model.impl.User;

    /*
     以下获得好友的增删改查
    */
public interface IFriendAPresenter {

    /*
        获取指定好友信息

        输入朋友的 name uuid
        其中 name 可以为 null
             uuid 不为空
        返回朋友的 User 类的实例
        包含 name deviceNumber signature   mail 信息

        无好友信息则返回 null
         暂未对异常做处理
     */
    public User getFriend(String name, String uuid);

    /*
        获得所有好友信息

        输入  无

        输出 ： 有好友信息  User[]
                无好友信息  null

        异常 ： 暂无异常处理
     */
    public User[] getAllFriend();

    /*
        删除好友

        输入 ： 好友 uuid

        输出 ： 正确执行  true
                错误执行  false

        异常 ： 暂无异常处理
     */
    public boolean delFriends(String uuid);

    /*
        修改好友信息

        输入 ： 好友信息  name uuid mail signature （uuid不可变，其余参数可谓 null）

        输出 :  正确执行 true

        错误执行： false

        异常 ： 未对异常做处理
     */
    public boolean modifyFriend(String name, String uuid, String mail, String signature);

    /*
        保存好友信息

        本地函数 不对外调用
     */
    public boolean saveFriend(String name, String uuid, String mail, String signature);


    /*
     以下完成好友添加和请求
     */
    /*
        返回未添加的好友列表

        输入 ：

        输出 ： 返回 User[]

        异常 ： 未对异常做处理
     */
    public User[] getFriendToSure();

        /*
         以下完成好友添加和请求
         */
    /*
        返回未添加的好友列表

        输入 ：

        输出 ： 返回 User[]

        异常 ： 未对异常做处理
     */
        public User[] getFriends();

    /*
        接受好友请求

        输入 ： 好友信息 name  uuid mail signature (uuid不可为空，其余自定义）

        输出 ： 正确执行 true
                错误执行 false

         异常 ：未对异常做处理
     */

    public boolean acceptFriend(String fname, String fuuid, String mail, String signature);

    /*
        添加朋友请求

        输入 ： 好友信息  name  uuid

        输出 ： 正确执行  true
                错误执行  false

         异常 ： 未对异常做处理
     */
    public boolean addFriends(String name, String uuid);

    /*
        拒绝好友请求

        输入 : 好友 uuid

        输出 ： 正确执行 true
                错误执行 false

         异常 ： 未对异常做处理
     */
    public boolean rejectFriend(String fuuid);//拒绝好友请求
}
