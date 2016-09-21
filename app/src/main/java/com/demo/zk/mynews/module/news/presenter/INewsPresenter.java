package com.demo.zk.mynews.module.news.presenter;


import com.demo.zk.mynews.base.BasePresenter;

/**
 * ClassName: INewsPresenter<p>
 * Fuction: 新闻代理接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsPresenter extends BasePresenter {

    /**
     * 频道排序或增删变化后调用此方法更新数据库
     */
    void operateChannelDb();

}
