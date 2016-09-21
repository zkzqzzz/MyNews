package com.demo.zk.mynews.module.news.model;


import com.demo.zk.mynews.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: INewsDetailInteractor<p>
 * Fuction: 新闻详情的Model层接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsDetailInteractor<T> {

    Subscription requestNewsDetail(RequestCallback<T> callback, String id);

}
