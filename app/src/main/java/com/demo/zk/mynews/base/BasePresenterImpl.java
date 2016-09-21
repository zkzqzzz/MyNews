package com.demo.zk.mynews.base;

import com.demo.zk.mynews.callback.RequestCallback;
import com.socks.library.KLog;

import rx.Subscription;

/**
 * ClassName: BasePresenterBasePresenterImpl<p>
 * Fuction: 代理的基类实现<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class BasePresenterImpl<T extends BaseView, V> implements BasePresenter, RequestCallback<V> {

    protected Subscription mSubscription;
    protected T mView;

    public BasePresenterImpl(T view) {
        mView = view;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mView = null;
    }


    @Override
    public void beforeRequest() {
        mView.showProgress();
    }

    @Override
    public void requestError(String msg) {
        KLog.e(msg);
        mView.toast(msg);
        mView.hideProgress();
    }

    @Override
    public void requestComplete() {
        mView.hideProgress();
    }

    @Override
    public void requestSuccess(V data) {

    }
}
