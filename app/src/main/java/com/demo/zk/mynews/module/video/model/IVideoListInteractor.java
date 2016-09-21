package com.demo.zk.mynews.module.video.model;

import com.demo.zk.mynews.callback.RequestCallback;

import rx.Subscription;
/**
 * ClassName: IVideoListInteractor<p>
 * Fuction: 视频列表Model层接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IVideoListInteractor<T> {

    Subscription requestVideoList(RequestCallback<T> callback, String id, int startPage);

}
