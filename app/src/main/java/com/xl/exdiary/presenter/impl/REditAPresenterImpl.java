package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.REditAModelImpl;
import com.xl.exdiary.model.inter.IREditAModel;
import com.xl.exdiary.presenter.inter.IREditAPresenter;
import com.xl.exdiary.view.inter.IREditAView;

public class REditAPresenterImpl implements IREditAPresenter {
    private IREditAView mIREditAView;
    private IREditAModel mIREditAModel;

    public REditAPresenterImpl(IREditAView aIREditAView) {
        mIREditAView = aIREditAView;
        mIREditAModel = new REditAModelImpl();
    }
}
