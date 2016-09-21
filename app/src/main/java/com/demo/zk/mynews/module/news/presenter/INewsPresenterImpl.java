package com.demo.zk.mynews.module.news.presenter;

import com.demo.zk.mynews.base.BasePresenterImpl;
import com.demo.zk.mynews.greendao.NewsChannelTable;
import com.demo.zk.mynews.module.news.model.INewsInteractor;
import com.demo.zk.mynews.module.news.model.INewsInteractorImpl;
import com.demo.zk.mynews.module.news.view.INewsView;

import java.util.List;

/**
 * ClassName:com.demo.zk.mynews.module.news.presenter
 * Author: zk<p>.
 * time: 2016/8/28 20:35.
 * Function: 新闻代理接口实现
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsPresenterImpl extends BasePresenterImpl<INewsView,List<NewsChannelTable>> implements INewsPresenter{

    private INewsInteractor<List<NewsChannelTable>> mNewsInteractor;

    public INewsPresenterImpl(INewsView newsView) {
        super(newsView);
        mNewsInteractor = new INewsInteractorImpl();
        mSubscription = mNewsInteractor.operateChannelDb(this);
        mView.initRxBusEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void requestSuccess(List<NewsChannelTable> data) {
        mView.initViewPager(data);
    }

    @Override
    public void operateChannelDb() {
        mSubscription = mNewsInteractor.operateChannelDb(this);
    }
}
