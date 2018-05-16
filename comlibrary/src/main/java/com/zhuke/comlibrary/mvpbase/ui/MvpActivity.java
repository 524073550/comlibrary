package com.zhuke.comlibrary.mvpbase.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhuke.comlibrary.base.BaseActivity;
import com.zhuke.comlibrary.mvpbase.base.BasePresenter;

/**
 * Created by 15653 on 2018/5/16.
 */

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
    protected P mvpPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }
    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
}
