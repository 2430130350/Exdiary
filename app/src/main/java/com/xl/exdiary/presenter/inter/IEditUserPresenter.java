package com.xl.exdiary.presenter.inter;
import com.xl.exdiary.model.impl.User;

import org.json.JSONArray;
import org.json.JSONException;

public interface IEditUserPresenter {
    /*
        保存用户信息 提供服务器保存

        输入： 用户信息 name  mail  signature  (uuid自主读取，用户信息可为 null)

        输出： 执行结果 true  false

        异常： 返回异常
     */
    public boolean saveUserInfor(String nickName, String mail, String signature) ;

    /*
        获得用户信息

        输入：

        输出：存在用户  User实例
              无用户   null

         异常： 返回异常
     */
    public User getUserInfor();

    /*
        删除用户信息（为上架）

        输入：

        输出： 正确执行 true
               错误执行 false

        异常： 返回异常
     */
    public boolean delUserInfor();

    /*
        更新用户信息 服务器同步更新

        输入： 用户信息  name  uuid  mail  signature  （uuid不可更新）

        输出： 正确执行 true
               错误执行 false

        异常： 返回异常 （提供主界面的异常返回，其它界面未上架）
     */
    public boolean updateUserInfor(String nickName, String uuid, String mail, String signature);

    /*
        检查用户是否登陆

        输入：

        输出： 存在用户  true
               不存在用户 false

        异常：不存在异常
     */
    public boolean isLogin();//检查用户是否登陆
}
