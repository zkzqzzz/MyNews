package com.demo.zk.mynews.module.photo.model;


import com.demo.zk.mynews.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: IPhotoDetailInteractor<p>
 * Fuction: 图片详情的Model层接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IPhotoDetailInteractor<T> {

    Subscription requestPhotoDetail(RequestCallback<T> callback, String id);

}
