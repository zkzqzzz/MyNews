package com.demo.zk.mynews.module.video.presenter;

import android.text.TextUtils;

import com.demo.zk.mynews.base.BasePresenterImpl;
import com.demo.zk.mynews.bean.NeteastVideoSummary;
import com.demo.zk.mynews.common.DataLoadType;
import com.demo.zk.mynews.module.video.model.IVideoListInteractor;
import com.demo.zk.mynews.module.video.model.IVideoListInteractorImpl;
import com.demo.zk.mynews.module.video.view.IVideoListView;
import com.socks.library.KLog;

import java.util.List;

/**
 * ClassName: IVideoListPresenterImpl<p>
 * Fuction: 视频列表代理接口实现<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IVideoListPresenterImpl
        extends BasePresenterImpl<IVideoListView, List<NeteastVideoSummary>>
        implements IVideoListPresenter {

    private IVideoListInteractor<List<NeteastVideoSummary>> mVideoListInteractor;

    private String mId;
    private int mStartPage;

    private boolean mIsRefresh = true;
    private boolean mHasInit;
    private boolean mIsVisibleToUser;
    private String errorMsg;

    public IVideoListPresenterImpl(IVideoListView view, String id, int startPage) {
        super(view);
        mId = id;
        mStartPage = startPage;
        mVideoListInteractor = new IVideoListInteractorImpl();
        mSubscription = mVideoListInteractor.requestVideoList(this, id, startPage);
    }

    @Override
    public void beforeRequest() {
        if (!mHasInit) {
            mView.showProgress();
        }
    }

    @Override
    public void requestError(String e) {
        // super.requestError(e);
        mView.hideProgress();

        mHasInit = true;

        if (mIsVisibleToUser) {
            mView.toast(e);
        } else {
            errorMsg = e;
        }

        mView.updateVideoList(null,
                mIsRefresh ? DataLoadType.TYPE_REFRESH_FAIL : DataLoadType.TYPE_LOAD_MORE_FAIL);
    }


    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        mSubscription = mVideoListInteractor.requestVideoList(this, mId, mStartPage);
    }

    @Override
    public void loadMoreData() {
        KLog.e("加载更多数据: " + mId + ";" + mStartPage);
        mIsRefresh = false;
        mSubscription = mVideoListInteractor.requestVideoList(this, mId, mStartPage);
    }

    @Override
    public void onVisibleToUser() {
        mIsVisibleToUser = true;
        if (!TextUtils.isEmpty(errorMsg)) {
            mView.toast(errorMsg);
        }
    }

    @Override
    public void onInvisibleToUser() {
        mIsVisibleToUser = false;
        if (!TextUtils.isEmpty(errorMsg)) {
            errorMsg = "";
        }
    }

    @Override
    public void requestSuccess(List<NeteastVideoSummary> data) {
        KLog.e("请求成功: ");
        mHasInit = true;
        if (data != null && data.size() > 0) {
            mStartPage += 10;
        }
        mView.updateVideoList(data,
                mIsRefresh ? DataLoadType.TYPE_REFRESH_SUCCESS : DataLoadType.TYPE_LOAD_MORE_SUCCESS);
    }
}
