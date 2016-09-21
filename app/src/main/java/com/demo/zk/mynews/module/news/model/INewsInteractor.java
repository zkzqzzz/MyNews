package com.demo.zk.mynews.module.news.model;


import com.demo.zk.mynews.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: INewsInteractor<p>
 * Author: oubowu<p>
 * Fuction: 新闻Model层接口<p>
 * CreateDate: 2016/2/20 15:05<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsInteractor<T> {

    Subscription operateChannelDb(RequestCallback<T> callback);

}
