package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.SettingAModelImpl;
import com.xl.exdiary.model.inter.ISettingAModel;
import com.xl.exdiary.presenter.inter.ISettingAPresenter;
import com.xl.exdiary.view.inter.ISettingAView;

public class SettingAPresenterImpl implements ISettingAPresenter {
    private ISettingAView mISettingAView;
    private ISettingAModel mISettingAModel;

    public SettingAPresenterImpl(ISettingAView aISettingAView) {
        mISettingAView = aISettingAView;
        mISettingAModel = new SettingAModelImpl();
    }
}
