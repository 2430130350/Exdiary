package com.xl.exdiary.model.impl;


import android.os.Environment;

import com.xl.exdiary.model.inter.IUserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class UserModel implements IUserModel {
    @Override
    public boolean saveUserInfoInLocal(JSONObject userInfo) {
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"ExDiary/");
            if(!file.exists())
                file.mkdir();
            BufferedWriter bw=new BufferedWriter(new FileWriter(new File(Environment.getExternalStorageDirectory(),"ExDiary/userInfo.json")));
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
    public boolean saveUserInfoOnServer(JSONObject userInfo) {
        try{
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            userInfo.put("operation",1);
            userInfo.put("deviceID",userInfo.get("deviceNumber"));
            userInfo.put("username",userInfo.get("name"));
            userInfo.put("motto",userInfo.get("signature"));
            userInfo.remove("deviceNumber");
            userInfo.remove("name");
            userInfo.remove("signature");
            OutputStream os=socket.getOutputStream();
            os.write(userInfo.toString().getBytes());
            os.flush();
            InputStream is=socket.getInputStream();
            byte[] bytes=new byte[1024];
            is.read(bytes);
            os.close();
            is.close();
            socket.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JSONObject getUserInfo() {
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"ExDiary/");
            if(!file.exists())
                file.mkdir();
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

    @Override
    public boolean saveFriendInLocal(JSONObject friend) {
        JSONArray friends=getAllFriend();
        int flag=0;
        try{
            for(int i=0;i<friends.length();i++)
                if(friends.getJSONObject(i).get("uuid").equals(friend.get("uuid"))) {
                    friends.remove(i);
                    friends.put(friend);
                    flag=1;
                }
            if(flag==0)
                friends.put(friend);
            File file=new File(Environment.getExternalStorageDirectory(),"ExDiary/");
            if(!file.exists())
                file.mkdir();
            BufferedWriter bw=new BufferedWriter(new FileWriter(new File(Environment.getExternalStorageDirectory(),"ExDiary/friends.json")));
            bw.write(friends.toString());
            bw.flush();
            bw.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delFriendInLocal(JSONObject friend) {
        JSONArray friends=getAllFriend();
        try{
            for(int i=0;i<friends.length();i++)
                if(friends.getJSONObject(i).get("uuid").equals(friend.get("uuid"))) {
                    friends.remove(i);
                    break;
                }
            BufferedWriter bw=new BufferedWriter(new FileWriter(new File(Environment.getExternalStorageDirectory(),"ExDiary/friends.json")));
            bw.write(friends.toString());
            bw.flush();
            bw.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JSONArray getAllFriend() {
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"ExDiary/");
            if(!file.exists())
                file.mkdir();
            StringBuilder result = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(
                    new File(Environment.getExternalStorageDirectory(),"ExDiary/friends.json")));
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
    public JSONArray getAllFriendOnServer(String myUUID) {
        try{
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("operation",9);
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
    public boolean delFriendOnServer(String myUUID,String friendUUID) {
        return false;
    }

    @Override
    public boolean addFriendOnServer(String myUUID,String friendUUID) {
        try{
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("operation",10);
            jsonObject.put("deviceID",myUUID);
            jsonObject.put("friendID",friendUUID);
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
    public boolean acceptFriendRequest(String myUUID, String friendUUID) {
        try{
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("operation",11);
            jsonObject.put("deviceID",myUUID);
            jsonObject.put("friendID",friendUUID);
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
    public boolean rejectFriendRequest(String myUUID, String friendUUID) {
        try{
            Socket socket=new Socket(Server.getIP(),Server.getPost());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("operation",11);
            jsonObject.put("deviceID",myUUID);
            jsonObject.put("friendID",friendUUID);
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
