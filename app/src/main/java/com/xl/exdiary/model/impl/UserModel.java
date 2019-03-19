package com.xl.exdiary.model.impl;


import com.xl.exdiary.model.inter.IUserModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class UserModel implements IUserModel {
    @Override
    public boolean saveUserInfo(JSONObject userInfo) {
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter("userInfo.json"));
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
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("userInfo.json"), StandardCharsets.UTF_8));
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
