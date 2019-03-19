package com.xl.exdiary.presenter.impl;
import com.xl.exdiary.model.impl.DiaryModel;
import com.xl.exdiary.model.inter.IDiaryModel;
import com.xl.exdiary.presenter.inter.IMainAPresenter;
import com.xl.exdiary.view.inter.IMainAView;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainAPresenterImpl implements IMainAPresenter {
    private IMainAView mIMainAView;
    private IDiaryModel mIDiaryModel;

    public MainAPresenterImpl(IMainAView aIMainAView) {
        mIMainAView = aIMainAView;//实例化主界面
        mIDiaryModel = new DiaryModel();//实例化日记类
    }

    @Override
    public JSONArray getAllDiaryList() {//返回所有的日记 jsonarray 格式
        JSONArray jsa = mIDiaryModel.getAllDiaryList();
        if(jsa.length() == 0)
        {//传入一个 长度为0 的 json array 的对象
            return null;
        }
        else {
            return jsa;
        }
    }

    @Override
    public boolean saveDiary(JSONObject diary) {//添加一个日记 JSONObject 包含 账户姓名/id 日记 title body
        if(diary == null)
        {//传入一个参数为空的 json 对象
            return false;
        }
        else {
            if (mIDiaryModel == null)
                return false;
            else if (mIDiaryModel.saveDiary(diary))
                return true;
            else
                return false;
        }
    }

    @Override
    public boolean delDiary(JSONObject diary) {//删除一个日记 包含 账户/id title body date url
        if(diary == null)
        {//删除为空的日记对象
            return false;
        }
        else if (mIDiaryModel.deleteDiary(diary))
            return true;
        else
            return false;
    }
}
