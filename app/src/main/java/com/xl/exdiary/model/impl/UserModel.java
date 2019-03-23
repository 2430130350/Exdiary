package com.xl.exdiary.model.impl;


import android.os.Environment;

import com.xl.exdiary.model.inter.IUserModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class UserModel implements IUserModel {
    @Override
    public boolean saveUserInfo(JSONObject userInfo) {
        try {
            /**
             * 下面这一行在执行时直接异常、被catch、待修复、
             * */
            BufferedWriter bw=new BufferedWriter(new FileWriter(new File(Environment.getExternalStorageDirectory(),"ExDiary/userInfo.json")));
            /**
             *
             * */


            bw.write(userInfo.toString());
            bw.flush();
            bw.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JSONObject getUserInfo() {
        try {
            StringBuilder result = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(
                    new File(Environment.getExternalStorageDirectory(),"ExDiary/userInfo.json")));
            String s = null;
            while ((s = br.readLine()) != null)
                result.append(System.lineSeparator() + s);
            br.close();
            return new JSONObject(result.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
