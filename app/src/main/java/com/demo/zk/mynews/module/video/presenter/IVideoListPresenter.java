package com.demo.zk.mynews.module.video.presenter;


import com.demo.zk.mynews.base.BasePresenter;

/**
 * ClassName: IVideoListPresenter<p>
 * Fuction: 视频列表代理接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IVideoListPresenter extends BasePresenter {

    void refreshData();

    void loadMoreData();
    
    void onVisibleToUser();
    
    void onInvisibleToUser();

}
