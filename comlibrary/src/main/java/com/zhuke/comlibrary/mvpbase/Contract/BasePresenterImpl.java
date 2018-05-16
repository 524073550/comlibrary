package com.zhuke.comlibrary.mvpbase.Contract;

import com.zhuke.comlibrary.mvpbase.base.BasePresenter;
import com.zhuke.comlibrary.mvpbase.base.BaseView;

/**
 * Created by 15653 on 2018/5/16.
 */

public abstract class BasePresenterImpl<V extends BaseView> implements  BasePresenter {
    private V mView;
    public BasePresenterImpl(V view) {
        mView = view;
        view.setPresenter(this);
    }



    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }
}
