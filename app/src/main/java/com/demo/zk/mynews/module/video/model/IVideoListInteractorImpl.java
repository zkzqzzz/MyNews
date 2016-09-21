package com.demo.zk.mynews.module.video.model;


import com.demo.zk.mynews.base.BaseSubscriber;
import com.demo.zk.mynews.bean.NeteastVideoSummary;
import com.demo.zk.mynews.callback.RequestCallback;
import com.demo.zk.mynews.http.HostType;
import com.demo.zk.mynews.http.manager.RetrofitManager;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * ClassName: IVideoListInteractorImpl<p>
 * Fuction: 视频列表Model层接口实现<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IVideoListInteractorImpl implements IVideoListInteractor<List<NeteastVideoSummary>> {

    @Override
    public Subscription requestVideoList(final RequestCallback<List<NeteastVideoSummary>> callback, final String id, int startPage) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO)
                .getVideoListObservable(id, startPage)
                /*.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callback.beforeRequest();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())*/
                .flatMap(
                        new Func1<Map<String, List<NeteastVideoSummary>>, Observable<NeteastVideoSummary>>() {
                            @Override
                            public Observable<NeteastVideoSummary> call(Map<String, List<NeteastVideoSummary>> map) {
                                // 通过id取到list
                                return Observable.from(map.get(id));
                            }
                        })
                .toSortedList(new Func2<NeteastVideoSummary, NeteastVideoSummary, Integer>() {
                    @Override
                    public Integer call(NeteastVideoSummary neteastVideoSummary, NeteastVideoSummary neteastVideoSummary2) {
                        // 时间排序
                        return neteastVideoSummary2.ptime.compareTo(neteastVideoSummary.ptime);
                    }
                })
                .subscribe(new BaseSubscriber<List<NeteastVideoSummary>>(callback));
                /*.subscribe(new Subscriber<List<NeteastVideoSummary>>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e + "\n" + e.getLocalizedMessage());
                        callback.requestError(e + "\n" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<NeteastVideoSummary> data) {
                        callback.requestSuccess(data);
                    }
                });*/
    }
}
