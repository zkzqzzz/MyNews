package com.demo.zk.mynews.module.video.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo.zk.mynews.Annotation.ActivityFragmentInject;
import com.demo.zk.mynews.R;
import com.demo.zk.mynews.base.BaseFragment;
import com.demo.zk.mynews.base.BaseRecyclerAdapter;
import com.demo.zk.mynews.base.BaseRecyclerViewHolder;
import com.demo.zk.mynews.base.BaseSpacesItemDecoration;
import com.demo.zk.mynews.bean.NeteastVideoSummary;
import com.demo.zk.mynews.callback.OnItemClickAdapter;
import com.demo.zk.mynews.common.DataLoadType;
import com.demo.zk.mynews.module.video.presenter.IVideoListPresenter;
import com.demo.zk.mynews.module.video.presenter.IVideoListPresenterImpl;
import com.demo.zk.mynews.module.video.view.IVideoListView;
import com.demo.zk.mynews.utils.ClickUtils;
import com.demo.zk.mynews.utils.MeasureUtil;
import com.demo.zk.mynews.widget.AutoLoadMoreRecyclerView;
import com.demo.zk.mynews.widget.ThreePointLoadingView;
import com.demo.zk.mynews.widget.refresh.RefreshLayout;

import java.util.List;
import java.util.Random;

/**
 * ClassName: VideoListFragment<p>
 * Fuction: 视频列表界面<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.fragment_video_list,
        handleRefreshLayout = true)
public class VideoListFragment extends BaseFragment<IVideoListPresenter> implements IVideoListView {

    protected static final String VEDIO_ID = "video_id";
    protected static final String POSITION = "position";

    protected String mVideoId;

    private BaseRecyclerAdapter<NeteastVideoSummary> mAdapter;
    private AutoLoadMoreRecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ThreePointLoadingView mLoadingView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mPresenter == null) {
            return;
        }
        if (isVisibleToUser) {
            //可见时执行的操作
            mPresenter.onVisibleToUser();
        } else {
            //不可见时执行的操作
            mPresenter.onInvisibleToUser();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideoId = getArguments().getString(VEDIO_ID);
            mPosition = getArguments().getInt(POSITION);
        }
    }

    public static VideoListFragment newInstance(String newsId, int position) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VEDIO_ID, newsId);
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initView(View fragmentRootView) {

        mLoadingView = (ThreePointLoadingView) fragmentRootView.findViewById(R.id.tpl_view);
        mLoadingView.setOnClickListener(this);


        mRecyclerView = (AutoLoadMoreRecyclerView) fragmentRootView
                .findViewById(R.id.recycler_view);

        mRefreshLayout = (RefreshLayout) fragmentRootView.findViewById(R.id.refresh_layout);

        mPresenter = new IVideoListPresenterImpl(this, mVideoId, 0);
        if (mPosition == 0) {
            mPresenter.onVisibleToUser();
        }
    }


    @Override
    public void showProgress() {
        mLoadingView.play();
    }

    @Override
    public void hideProgress() {
        mLoadingView.stop();
    }

    @Override
    public void updateVideoList(List<NeteastVideoSummary> data, @DataLoadType.DataLoadTypeChecker int type) {

        if (mAdapter == null) {
            initVideoList(null);
        }

        switch (type) {
            case DataLoadType.TYPE_REFRESH_SUCCESS:
                mRefreshLayout.refreshFinish();
                mAdapter.setData(data);
                if (mRecyclerView.isAllLoaded()) {
                    // 之前全部加载完了的话，这里把状态改回来供底部加载用
                    mRecyclerView.notifyMoreLoaded();
                }
                break;
            case DataLoadType.TYPE_REFRESH_FAIL:
                mRefreshLayout.refreshFinish();
                break;
            case DataLoadType.TYPE_LOAD_MORE_SUCCESS:
                // 隐藏尾部加载
                mAdapter.hideFooter();
                if (data == null || data.size() == 0) {
                    mRecyclerView.notifyAllLoaded();
                    toast("全部加载完毕噜(☆＿☆)");
                } else {
                    mAdapter.addMoreData(data);
                    mRecyclerView.notifyMoreLoaded();
                }
                break;
            case DataLoadType.TYPE_LOAD_MORE_FAIL:
                mAdapter.hideFooter();
                mRecyclerView.notifyMoreLoadedFail();
                break;
        }
    }

    private void initVideoList(List<NeteastVideoSummary> data) {

        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        mAdapter = new BaseRecyclerAdapter<NeteastVideoSummary>(getActivity(), data, true,
                layoutManager) {

            Random mRandom = new Random();

            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_video_summary;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, final int position, final NeteastVideoSummary item) {
                final ImageView imageView = holder.getImageView(R.id.iv_video_summary);
                final ViewGroup.LayoutParams params = imageView.getLayoutParams();
                // KLog.e("图片网址：" + item.kpic);
                if (item.picWidth == -1 && item.picHeight == -1) {
                    item.picWidth = MeasureUtil.getScreenSize(getActivity()).x / 2 - MeasureUtil
                            .dip2px(getActivity(), 4) * 2 - MeasureUtil.dip2px(getActivity(), 2);
                    item.picHeight = (int) (item.picWidth * (mRandom.nextFloat() / 2 + 0.7));
                }
                params.width = item.picWidth;
                params.height = item.picHeight;
                imageView.setLayoutParams(params);

                Glide.with(getActivity()).load(item.cover).asBitmap()
                        .placeholder(R.drawable.ic_loading).error(R.drawable.ic_fail)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

                holder.getTextView(R.id.tv_video_summary).setText(Html.fromHtml(item.title));
            }
        };

        mAdapter.setOnItemClickListener(new OnItemClickAdapter() {
            @Override
            public void onItemClick(View view, int position) {

                if (ClickUtils.isFastDoubleClick()) {
                    return;
                }

                final String mp4Url = mAdapter.getData().get(position).mp4Url;
                if (TextUtils.isEmpty(mp4Url)) {
                    toast("此视频无播放网址哎╮(╯Д╰)╭");
                    return;
                }
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra("videoUrl", mp4Url);
                intent.putExtra("videoName", mAdapter.getData().get(position).title);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0,
                                0);
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
            }
        });

        mRecyclerView.setAutoLayoutManager(layoutManager).addAutoItemDecoration(
                new BaseSpacesItemDecoration(MeasureUtil.dip2px(getActivity(), 4)))
                .setAutoItemAnimator(new DefaultItemAnimator()).setAutoItemAnimatorDuration(250)
                .setAutoAdapter(mAdapter);

        mRecyclerView.setOnLoadMoreListener(new AutoLoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                // 状态停止，并且滑动到最后一位
                mPresenter.loadMoreData();
                // 显示尾部加载
                // KLog.e("显示尾部加载前："+mAdapter.getItemCount());
                mAdapter.showFooter();
                // KLog.e("显示尾部加载后："+mAdapter.getItemCount());
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

        mRefreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                mPresenter.refreshData();
            }
        });

    }

}
