package com.xl.exdiary.model.impl;


import com.xl.exdiary.model.inter.IDiaryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DiaryModel implements IDiaryModel {

    @Override
    public JSONArray getAllDiaryList() {
        try {
            StringBuilder result = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("diaries.json"), StandardCharsets.UTF_8));
            String s = null;
            while ((s = br.readLine()) != null)
                result.append(System.lineSeparator() + s);
            br.close();
            return new JSONArray(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveDiary(JSONObject diary) {
        JSONArray diaries=getAllDiaryList();
        int flag=0;
        try{
            for(int i=0;i<diaries.length();i++)
                if(diaries.getJSONObject(i).get("startTime")==diary.get("startTime")) {
                    diaries.remove(i);
                    diaries.put(diary);
                    flag=1;
                }
            if(flag==0)
                diaries.put(diary);
            BufferedWriter bw=new BufferedWriter(new FileWriter("diaries.json"));
            bw.write(diaries.toString());
            bw.flush();
            bw.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean deleteDiary(JSONObject diary) {
        JSONArray diaries=getAllDiaryList();
        try{
            for(int i=0;i<diaries.length();i++)
                if(diaries.getJSONObject(i).get("startTime")==diary.get("startTime")) {
                    diaries.remove(i);
                    return true;
                }
            BufferedWriter bw=new BufferedWriter(new FileWriter("diaries.json"));
            bw.write(diaries.toString());
            bw.flush();
            bw.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
