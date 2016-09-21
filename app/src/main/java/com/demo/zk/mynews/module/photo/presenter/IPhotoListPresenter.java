package com.demo.zk.mynews.module.photo.presenter;


import com.demo.zk.mynews.base.BasePresenter;

/**
 * ClassName: IPhotoListPresenter<p>
 * Fuction: 图片列表代理接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IPhotoListPresenter extends BasePresenter {

    void refreshData();

    void loadMoreData();

}
