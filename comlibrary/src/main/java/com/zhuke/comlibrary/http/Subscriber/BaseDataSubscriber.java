package com.zhuke.comlibrary.http.Subscriber;


import com.zhuke.comlibrary.bean.HttpResult;
import com.zhuke.comlibrary.http.exception.ApiException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by 15653 on 2018/3/15.
 */

public abstract class BaseDataSubscriber<T> implements Observer<HttpResult<T>>, IDataSubscriber<T> {


    @Override
    public void onSubscribe(Disposable d) {
        doOnCompleted();
    }

    @Override
    public void onNext(HttpResult<T> result) {
        doOnNext(result);
    }

    @Override
    public void onError(Throwable e) {
        String error = ApiException.handleException(e).getMessage();
        doOnError(error);
    }

    @Override
    public void onComplete() {
        doOnCompleted();
    }
}
