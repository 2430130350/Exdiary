package com.xl.exdiary.presenter.impl;
import com.xl.exdiary.model.impl.DiaryModel;
import com.xl.exdiary.model.impl.UserModel;
import com.xl.exdiary.model.inter.IDiaryModel;
import com.xl.exdiary.presenter.inter.IMainAPresenter;
import com.xl.exdiary.view.inter.IMainAView;
import com.xl.exdiary.model.inter.IUserModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainAPresenterImpl implements IMainAPresenter {
    private IMainAView mIMainAView;
    private IDiaryModel mIDiaryModel;
    private IUserModel mIUserModel;

    public MainAPresenterImpl(IMainAView aIMainAView) {
        mIMainAView = aIMainAView;//实例化主界面
        mIDiaryModel = new DiaryModel();//实例化日记类
        mIUserModel = new UserModel();
    }

    @Override
    public DiaryModel[] getAllDiaryList() throws JSONException {//返回所有的日记 jsonarray 格式
        JSONObject jso;
        JSONArray jsa = mIDiaryModel.getAllDiaryList();
        DiaryModel[] objl = new DiaryModel[jsa.length()];
        if(jsa.length() == 0)
        {//传入一个 长度为0 的 json array 的对象
            return null;
        }
        else {
            for(int i = 0; i< jsa.length(); i++){
                jso = jsa.getJSONObject(i);
                objl[i] = DiaryModel(jso.getString("title"),jso.getString("body"),jso.getString("startTime"));
            }
            return objl;
        }
    }

    @Override
    public boolean delDiary(String date) throws JSONException {//删除一个日记 包含 账户/id date
        JSONObject jso = mIUserModel.getUserInfo();
        if(date.isEmpty())
        {//删除为空的日记对象
            return false;
        }
        else {
            JSONObject ddiary = new JSONObject();
            ddiary.put("user",jso.getString("user"));
            ddiary.put("date",date);
            return mIDiaryModel.deleteDiary(ddiary);
        }
    }
}
