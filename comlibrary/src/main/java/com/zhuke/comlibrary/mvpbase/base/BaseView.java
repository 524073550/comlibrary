package com.zhuke.comlibrary.mvpbase.base;

/**
 * Created by 15653 on 2018/5/16.
 */

public interface BaseView<P extends BasePresenter > {
    void setPresenter(P presenter);
}
