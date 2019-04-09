package com.xl.exdiary.presenter.impl;
import android.annotation.SuppressLint;
import android.location.Location;

import com.xl.exdiary.model.impl.DiaryModel;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IDiaryModel;
import com.xl.exdiary.presenter.inter.IMainAPresenter;
import com.xl.exdiary.view.inter.IMainAView;
import com.xl.exdiary.model.inter.IUserModel;
import com.xl.exdiary.model.impl.Diary;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainAPresenterImpl implements IMainAPresenter {
    private IMainAView mIMainAView;
    private IDiaryModel mIDiaryModel;
    private IUserModel mIUserModel;

    public MainAPresenterImpl(IMainAView aIMainAView) {
        mIMainAView = aIMainAView;//实例化主界面
        mIDiaryModel = new DiaryModel();//实例化日记类
        mIUserModel = new UserModel();
    }
    //返回所有的日记 diary数组 格式
    @Override
    public Diary[] getAllDiaryList()  {
        JSONObject jso = null;
        JSONArray jsa = mIDiaryModel.getAllDiaryList();
        orderJSONArrayList(jsa, 0 , jsa.length()-1);
        Diary[] diaries = new Diary[jsa.length()];
        if( jsa.length() == 0)
        {//传入一个 长度为0 的 json array 的对象
            return new Diary[0];
        }
        else {
            for(int i = 0; i< jsa.length(); i++){
                try {
                    jso = jsa.getJSONObject(i);
                    diaries[i] = new Diary(jso.getString("title"), jso.getString("body"), jso.getString("date"));
                } catch (JSONException e) {
                    mIMainAView.exception();
                    return null;
                    //异常处理
                }
            }
            return diaries;
        }
    }

    @Override
    public JSONObject getWeather() {
       // Location location = new Location();
        return null;
    }

    //删除一个日记 包含 账户/id date
    @Override
    public boolean delDiary(String date)  {
        JSONObject jso = mIUserModel.getUserInfo();
        if(date.length() == 0)
        {//删除为空的日记对象
            return false;
        }
        else if(jso != null) {
            JSONObject diary = new JSONObject();
            try {
                diary.put("user",jso.getString("name"));
                diary.put("date",date);
            } catch (JSONException e) {
                mIMainAView.exception();
                return false;
            }
            return mIDiaryModel.deleteDiary(diary);
        }
        else
            return false;
    }

    //针对日记进行排序
    @Override
    public void orderJSONArrayList(JSONArray diarys, int first, int end) {
        int mid;
        if(first < end)
        {
            try {
                mid = partition(diarys, first, end);
                orderJSONArrayList(diarys, first, mid-1);
                orderJSONArrayList(diarys, mid+1, end);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    //划分JSONArray
    public int partition(JSONArray jsa, int first, int end) throws JSONException, ParseException {
        int i = first, j = end;
        Date date1 = null;
        Date date2 = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new  SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        while (i < j)
        {
                date1 = simpleDateFormat.parse(jsa.getJSONObject(i).getString("date"));
                date2 = simpleDateFormat.parse(jsa.getJSONObject(j).getString("date"));
            //将右端进行划分
            while (i < j && date1.compareTo(date2) != 1 )
            {
                j--;
                date2 = simpleDateFormat.parse(jsa.getJSONObject(j).getString("date"));
            }
            if(i < j)
            {
                JSONObject tem = null;
                tem = jsa.getJSONObject(i);
                jsa.put(i, jsa.getJSONObject(j));
                jsa.put(j, tem);
                i++;
            }
            //左端进行划分
            date1 = simpleDateFormat.parse(jsa.getJSONObject(i).getString("date"));
            date2 = simpleDateFormat.parse(jsa.getJSONObject(j).getString("date"));
            while (i < j && date1.compareTo(date2) != 1)
            {
                i++;
                date1 = simpleDateFormat.parse(jsa.getJSONObject(i).getString("date"));
            }
            if(i < j ){
                JSONObject tem = null;
                tem = jsa.getJSONObject(i);
                jsa.put(i, jsa.getJSONObject(j));
                jsa.put(j, tem);
                i++;
                j--;
            }
        }
        return i;
    }
}
