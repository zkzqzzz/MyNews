package com.demo.zk.mynews.module.news.view;

import com.demo.zk.mynews.base.BaseView;
import com.demo.zk.mynews.greendao.NewsChannelTable;

import java.util.List;

/**
 * ClassName: INewsChannelView<p>
 * Fuction: 新闻频道管理视图接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsChannelView extends BaseView {

    void initTwoRecyclerView(List<NewsChannelTable> selectChannels, List<NewsChannelTable> unSelectChannels);

}
