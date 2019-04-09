package com.xl.exdiary.presenter.inter;

import org.json.JSONException;

public interface IREditAPresenter {
   /*
      保存日记

      输入： 日记信息 title  body  （不为空）

      输出： 执行结果 true false

      异常： 返回异常
    */
   public  boolean saveDiary(String title, String body);

   /*
      修改日记

      输入： 修改日记信息 date title  body

      输出： 执行结果 true false

      异常： 返回异常
    */
   public  boolean modifyDiary(String date, String title, String body);
}
