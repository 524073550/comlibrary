package com.zhuke.comlibrary.mvpbase;

import com.zhuke.comlibrary.mvpbase.Contract.SampleContract;

/**
 * Created by 15653 on 2018/5/16.
 */

public class SimplePersenterImpl implements SampleContract.SamplePersenter {
    private SampleContract.View mView;
    @Override
    public void attachView(SampleContract.View mvpView) {
        mView  = mvpView;
        mvpView.setPresenter(this);
    }

    @Override
    public void detachView() {
        if (mView != null){
            mView = null;
        }
    }

    @Override
    public void loadData(String data) {

    }
}
