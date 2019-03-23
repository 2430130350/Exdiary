package com.xl.exdiary.presenter.impl;

import com.xl.exdiary.model.impl.FriendAModelImpl;
import com.xl.exdiary.model.inter.IFriendAModel;
import com.xl.exdiary.presenter.inter.IFriendAPresenter;
import com.xl.exdiary.view.inter.IFriendAView;

public class FriendAPresenterImpl implements IFriendAPresenter {
    private IFriendAView mIFriendAView;
    private IFriendAModel mIFriendAModel;

    public FriendAPresenterImpl(IFriendAView aIFriendAView) {
        mIFriendAView = aIFriendAView;
        mIFriendAModel = new FriendAModelImpl();
    }

    @Override
    public boolean addFriends(String name, String uuid) {
        return false;
    }

    @Override
    public boolean delFriends(String name) {
        return false;
    }
}
