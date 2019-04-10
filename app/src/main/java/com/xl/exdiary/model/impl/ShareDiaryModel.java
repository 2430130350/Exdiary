package com.xl.exdiary.model.impl;

import android.annotation.SuppressLint;

import com.xl.exdiary.model.inter.IShareDiaryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder string= new StringBuilder();
            String read=new String();
            while (true){
                read=br.readLine();
                if(read==null)
                    break;
                string.append(read);
            }
            JSONArray result = new JSONArray(new JSONObject(string.toString()).getString("result"));
            os.close();
            br.close();
            socket.close();
            return result;
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
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder string= new StringBuilder();
            String read=new String();
            while (true){
                read=br.readLine();
                if(read==null)
                    break;
                string.append(read);
            }
            JSONObject result=new JSONObject(string.toString());
            os.close();
            br.close();
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
            jsonObject.put("time",shareDiary.getString("date"));
            jsonObject.put("articleID",ID);
            OutputStream os=socket.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder string= new StringBuilder();
            String read=new String();
            while (true){
                read=br.readLine();
                if(read==null)
                    break;
                string.append(read);
            }
            JSONObject result=new JSONObject(string.toString());
            os.close();
            br.close();
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
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder string= new StringBuilder();
            String read=new String();
            while (true) {
                read = br.readLine();
                if (read == null)
                    break;
                string.append(read);
            }
            JSONArray result = new JSONArray(new JSONObject(string.toString()).getString("result"));
            os.close();
            br.close();
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
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder string= new StringBuilder();
            String read=new String();
            while (true){
                read=br.readLine();
                if(read==null)
                    break;
                string.append(read);
            }
            JSONArray result = new JSONArray(new JSONObject(string.toString()).getString("result"));
            os.close();
            br.close();
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
                if(jsonArray.getJSONObject(i).getString("writetime").equals(disShareDiary.getJSONObject("shareDiary").getString("date"))){
                    ID=jsonArray.getJSONObject(i).getString("ID");
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
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder string= new StringBuilder();
            String read=new String();
            while (true){
                read=br.readLine();
                if(read==null)
                    break;
                string.append(read);
            }
            JSONObject result=new JSONObject(string.toString());
            os.close();
            br.close();
            socket.close();
            if(result.get("result").equals("1"))
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
