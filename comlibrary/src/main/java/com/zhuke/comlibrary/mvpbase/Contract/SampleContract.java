package com.zhuke.comlibrary.mvpbase.Contract;

import com.zhuke.comlibrary.mvpbase.base.BasePresenter;
import com.zhuke.comlibrary.mvpbase.base.BaseView;

/**
 * Created by 15653 on 2018/5/16.
 * 契约类
 */

public interface SampleContract {

    interface SamplePersenter extends BasePresenter<View>{
        void loadData(String data);
    }

    interface View extends BaseView<SamplePersenter> {
        void loadSuccse(String msg);
        void loadFeild(String msg);

    }
}
