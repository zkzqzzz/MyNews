package com.demo.zk.mynews.module.news.view;


import com.demo.zk.mynews.base.BaseView;
import com.demo.zk.mynews.bean.NeteastNewsSummary;
import com.demo.zk.mynews.common.DataLoadType;

import java.util.List;

/**
 * ClassName: INewsListView<p>
 * Fuction: 新闻列表视图接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsListView extends BaseView {

    void updateNewsList(List<NeteastNewsSummary> data, @DataLoadType.DataLoadTypeChecker int type);

}
