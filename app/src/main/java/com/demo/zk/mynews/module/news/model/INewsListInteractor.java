package com.demo.zk.mynews.module.news.model;


import com.demo.zk.mynews.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: INewsListInteractor<p>
 * Fuction: 新闻列表Model层接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsListInteractor<T> {

    Subscription requestNewsList(RequestCallback<T> callback, String type, String id, int startPage);

}
