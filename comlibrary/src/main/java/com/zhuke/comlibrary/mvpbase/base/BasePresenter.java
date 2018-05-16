package com.zhuke.comlibrary.mvpbase.base;

/**
 * Created by 15653 on 2018/5/16.
 */

public interface BasePresenter<V extends BaseView>{
    void attachView(V mvpView);
    void detachView();
}
