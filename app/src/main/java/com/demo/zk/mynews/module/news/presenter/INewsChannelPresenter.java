package com.demo.zk.mynews.module.news.presenter;


import com.demo.zk.mynews.base.BasePresenter;

/**
 * ClassName: INewsChannelPresenter<p>
 * Fuction: 新闻频道管理代理接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsChannelPresenter extends BasePresenter {

    void onItemAddOrRemove(String channelName, boolean selectState);

    void onItemSwap(int fromPos, int toPos);

}
