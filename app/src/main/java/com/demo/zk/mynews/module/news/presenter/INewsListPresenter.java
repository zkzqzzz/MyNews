package com.demo.zk.mynews.module.news.presenter;


import com.demo.zk.mynews.base.BasePresenter;

/**
 * ClassName: INewsListPresenter<p>
 * Fuction: 新闻列表代理接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsListPresenter extends BasePresenter {

    void refreshData();

    void loadMoreData();

}
