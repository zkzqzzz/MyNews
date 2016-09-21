package com.demo.zk.mynews.module.photo.model;



import com.demo.zk.mynews.base.BaseSubscriber;
import com.demo.zk.mynews.bean.SinaPhotoDetail;
import com.demo.zk.mynews.callback.RequestCallback;
import com.demo.zk.mynews.http.HostType;
import com.demo.zk.mynews.http.manager.RetrofitManager;

import rx.Subscription;

/**
 * ClassName: IPhotoDetailInteractorImpl<p>
 * Fuction: 图片详情的Model层接口实现<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IPhotoDetailInteractorImpl implements IPhotoDetailInteractor<SinaPhotoDetail> {
    @Override
    public Subscription requestPhotoDetail(final RequestCallback<SinaPhotoDetail> callback, String id) {
        return RetrofitManager.getInstance(HostType.SINA_NEWS_PHOTO).getSinaPhotoDetailObservable(id)
                .subscribe(new BaseSubscriber<SinaPhotoDetail>(callback));
                /*.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callback.beforeRequest();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SinaPhotoDetail>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.requestError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(SinaPhotoDetail sinaPhotoDetail) {
                        callback.requestSuccess(sinaPhotoDetail);
                    }
                });*/
    }
}
