package com.demo.zk.mynews.module.news.presenter;

import com.demo.zk.mynews.base.BasePresenterImpl;
import com.demo.zk.mynews.bean.NeteastNewsSummary;
import com.demo.zk.mynews.common.DataLoadType;
import com.demo.zk.mynews.module.news.model.INewsListInteractor;
import com.demo.zk.mynews.module.news.model.INewsListInteractorImpl;
import com.demo.zk.mynews.module.news.view.INewsListView;
import com.socks.library.KLog;

import java.util.List;

/**
 * ClassName: INewsListPresenterImpl<p>
 * Fuction: 新闻列表代理接口实现<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsListPresenterImpl
        extends BasePresenterImpl<INewsListView, List<NeteastNewsSummary>>
        implements INewsListPresenter {

    private INewsListInteractor<List<NeteastNewsSummary>> mNewsListInteractor;
    private String mNewsType;
    private String mNewsId;
    private int mStartPage;

    private boolean mIsRefresh = true;
    private boolean mHasInit;

    public INewsListPresenterImpl(INewsListView newsListView, String newsId, String newsType) {
        super(newsListView);
        mNewsListInteractor = new INewsListInteractorImpl();
        mSubscription = mNewsListInteractor.requestNewsList(this, newsType, newsId, mStartPage);
        mNewsType = newsType;
        mNewsId = newsId;
    }

    @Override
    public void beforeRequest() {
        if (!mHasInit) {
            mView.showProgress();
        }
    }

    @Override
    public void requestError(String e) {
        super.requestError(e);
        mView.updateNewsList(null,
                mIsRefresh ? DataLoadType.TYPE_REFRESH_FAIL : DataLoadType.TYPE_LOAD_MORE_FAIL);
    }

    @Override
    public void requestSuccess(List<NeteastNewsSummary> data) {
        KLog.e("请求成功");
        mHasInit = true;
        if (data != null) {
            mStartPage += 20;
        }
        mView.updateNewsList(data,mIsRefresh ? DataLoadType.TYPE_REFRESH_SUCCESS : DataLoadType.TYPE_LOAD_MORE_SUCCESS);

    }

    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        mSubscription = mNewsListInteractor.requestNewsList(this, mNewsType, mNewsId, mStartPage);
    }

    @Override
    public void loadMoreData() {
        mIsRefresh = false;
        mSubscription = mNewsListInteractor.requestNewsList(this, mNewsType, mNewsId, mStartPage);
    }

}
