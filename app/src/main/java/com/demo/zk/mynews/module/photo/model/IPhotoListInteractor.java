package com.demo.zk.mynews.module.photo.model;


import com.demo.zk.mynews.callback.RequestCallback;

import rx.Subscription;

/**
 * ClassName: IPhotoListInteractor<p>
 * Fuction: 图片列表Model层接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IPhotoListInteractor<T> {

    Subscription requestPhotoList(RequestCallback<T> callback, String id, int startPage);

}
