package com.demo.zk.mynews.module.news.presenter;


import com.demo.zk.mynews.base.BasePresenterImpl;
import com.demo.zk.mynews.bean.NeteastNewsDetail;
import com.demo.zk.mynews.module.news.model.INewsDetailInteractor;
import com.demo.zk.mynews.module.news.model.INewsDetailInteractorImpl;
import com.demo.zk.mynews.module.news.view.INewsDetailView;

/**
 * ClassName: INewsDetailPresenterImpl<p>
 * Fuction: 新闻详情代理接口实现<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsDetailPresenterImpl extends BasePresenterImpl<INewsDetailView, NeteastNewsDetail>
        implements INewsDetailPresenter {

    public INewsDetailPresenterImpl(INewsDetailView newsDetailView, String postId) {
        super(newsDetailView);
        INewsDetailInteractor<NeteastNewsDetail> newsDetailInteractor = new INewsDetailInteractorImpl();
        mSubscription = newsDetailInteractor.requestNewsDetail(this, postId);
    }

    @Override
    public void requestSuccess(NeteastNewsDetail data) {
        mView.initNewsDetail(data);
    }
}
