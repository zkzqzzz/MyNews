package com.demo.zk.mynews.module.news.view;


import com.demo.zk.mynews.base.BaseView;
import com.demo.zk.mynews.bean.NeteastNewsDetail;

/**
 * ClassName: INewsDetailView<p>
 * Fuction: 新闻详情视图接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsDetailView extends BaseView {

    void initNewsDetail(NeteastNewsDetail data);

}
