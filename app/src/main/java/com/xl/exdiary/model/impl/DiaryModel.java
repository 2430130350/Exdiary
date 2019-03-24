package com.xl.exdiary.model.impl;


import android.os.Environment;

import com.xl.exdiary.model.inter.IDiaryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class DiaryModel implements IDiaryModel {

    @Override
    public JSONArray getAllDiaryList() {
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"ExDiary/");
            if(!file.exists())
                file.mkdir();
            StringBuilder result = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(
                    new File(Environment.getExternalStorageDirectory(),"ExDiary/diaries.json")));
            String s = null;
            while ((s = br.readLine()) != null)
                result.append(System.lineSeparator() + s);
            br.close();
            return new JSONArray(result.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JSONArray();
    }

    @Override
    public boolean saveDiary(JSONObject diary) {
        JSONArray diaries=getAllDiaryList();
        int flag=0;
        try{
            for(int i=0;i<diaries.length();i++)
                if(diaries.getJSONObject(i).get("date")==diary.get("date")) {
                    diaries.remove(i);
                    diaries.put(diary);
                    flag=1;
                }
            if(flag==0)
                diaries.put(diary);
            File file=new File(Environment.getExternalStorageDirectory(),"ExDiary/");
            if(!file.exists())
                file.mkdir();
            BufferedWriter bw=new BufferedWriter(new FileWriter(new File(Environment.getExternalStorageDirectory(),"ExDiary/diaries.json")));
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
    public boolean deleteDiary(JSONObject diary) {
        JSONArray diaries=getAllDiaryList();
        try{
            for(int i=0;i<diaries.length();i++)
                if(diaries.getJSONObject(i).get("date")==diary.get("date")) {
                    diaries.remove(i);
                    return true;
                }
            BufferedWriter bw=new BufferedWriter(new FileWriter(new File(Environment.getExternalStorageDirectory(),"ExDiary/diaries.json")));
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
