package com.zhuke.comlibrary.mvpbase.ui;

import android.os.Bundle;
import android.view.View;

import com.zhuke.comlibrary.base.BaseFragment;
import com.zhuke.comlibrary.mvpbase.base.BasePresenter;

/**
 * Created by 15653 on 2018/5/16.
 */

public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {

    protected P mvpPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
}
