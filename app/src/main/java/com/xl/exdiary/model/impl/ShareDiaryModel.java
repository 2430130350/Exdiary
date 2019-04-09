package com.xl.exdiary.model.impl;

import android.annotation.SuppressLint;

import com.xl.exdiary.model.inter.IShareDiaryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareDiaryModel implements IShareDiaryModel {
    @Override
    public JSONArray getMyDiaries(String myUUID) {
        try{
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("operation",2);
            jsonObject.put("deviceID",myUUID);
            OutputStream os=socket.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();
            InputStream is=socket.getInputStream();
            byte[] bytes=new byte[1024];
            is.read(bytes);
            JSONObject result=new JSONObject(bytes.toString());
            os.close();
            is.close();
            socket.close();
            return result.getJSONArray("result");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JSONArray();
    }

    @Override
    public boolean updateDiary(JSONObject diary) {
        try{
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("operation",3);
            jsonObject.put("deviceID",diary.getString("uuid"));
            jsonObject.put("title",diary.getString("title"));
            jsonObject.put("content",diary.getString("body"));
            jsonObject.put("time",diary.getString("date"));
            OutputStream os=socket.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();
            InputStream is=socket.getInputStream();
            byte[] bytes=new byte[1024];
            is.read(bytes);
            JSONObject result=new JSONObject(bytes.toString());
            os.close();
            is.close();
            socket.close();
            if(result.get("result").equals("1"))
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean shareDiary(JSONObject shareDiary) {
        try{
            if(updateDiary(shareDiary.getJSONObject("shareDiary"))==false)
                return false;
            JSONArray jsonArray = getMyDiaries(shareDiary.getString("myUuid"));
            String ID=new String();
            for(int i=0;i<jsonArray.length();i++){
                if(jsonArray.getJSONObject(i).getString("time").equals(shareDiary.getJSONObject("shareDiary").getString("date"))){
                    ID=jsonArray.getJSONObject(i).getString("articleID");
                    break;
                }
            }
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("operation",7);
            jsonObject.put("deviceID",shareDiary.getString("myUuid"));
            jsonObject.put("friendID",shareDiary.getString("friendUuid"));
            Date date = new Date(System.currentTimeMillis());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new  SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            jsonObject.put("time",simpleDateFormat.format(date));
            jsonObject.put("articleID",ID);
            OutputStream os=socket.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();
            InputStream is=socket.getInputStream();
            byte[] bytes=new byte[1024];
            is.read(bytes);
            JSONObject result=new JSONObject(bytes.toString());
            os.close();
            is.close();
            socket.close();
            if(result.get("result").equals("1"))
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JSONArray getAllShareToDiary(String myUUID) {
        try{
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("operation",6);
            jsonObject.put("deviceID",myUUID);
            OutputStream os=socket.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();
            InputStream is=socket.getInputStream();
            byte[] bytes=new byte[1024];
            StringBuilder string= new StringBuilder();
            while (is.read(bytes)!=-1)
                string.append(bytes);
            JSONArray result=new JSONArray(string.toString());
            os.close();
            is.close();
            socket.close();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JSONArray();
    }

    @Override
    public JSONArray getAllShareFromDiary(String myUUID) {
        try{
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("operation",5);
            jsonObject.put("deviceID",myUUID);
            OutputStream os=socket.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();
            InputStream is=socket.getInputStream();
            byte[] bytes=new byte[1024];
            StringBuilder string= new StringBuilder();
            while (is.read(bytes)!=-1)
                string.append(bytes);
            JSONArray result=new JSONArray(string.toString());
            os.close();
            is.close();
            socket.close();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JSONArray();
    }

    @Override
    public boolean disableShareDiary(JSONObject disShareDiary) {
        try {
            String ID=new String();
            JSONArray jsonArray = getAllShareToDiary(disShareDiary.getString("myUuid"));
            for(int i=0;i<jsonArray.length();i++){
                if(jsonArray.getJSONObject(i).getString("time").equals(disShareDiary.getJSONObject("shareDiary").getString("date"))){
                    ID=jsonArray.getJSONObject(i).getString("articleID");
                    break;
                }
            }
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("operation",5);
            jsonObject.put("deviceID",disShareDiary.getString("myUuid"));
            jsonObject.put("ID",ID);
            OutputStream os=socket.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();
            InputStream is=socket.getInputStream();
            byte[] bytes=new byte[1024];
            is.read(bytes);
            JSONObject result=new JSONObject(bytes.toString());
            os.close();
            is.close();
            socket.close();
            if(result.get("result").equals("1"))
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
