package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.AnonymousAModelImpl;
import com.xl.exdiary.model.inter.IAnonymousAModel;
import com.xl.exdiary.presenter.inter.IAnonymousAPresenter;
import com.xl.exdiary.view.inter.IAnonymousAView;

public class AnonymousAPresenterImpl implements IAnonymousAPresenter {
    private IAnonymousAView mIAnonymousAView;
    private IAnonymousAModel mIAnonymousAModel;

    public AnonymousAPresenterImpl(IAnonymousAView aIAnonymousAView) {
        mIAnonymousAView = aIAnonymousAView;
        mIAnonymousAModel = new AnonymousAModelImpl();
    }
}
