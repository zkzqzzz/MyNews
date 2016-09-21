package com.demo.zk.mynews.module.news.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.demo.zk.mynews.Annotation.ActivityFragmentInject;
import com.demo.zk.mynews.R;
import com.demo.zk.mynews.app.AppManager;
import com.demo.zk.mynews.base.BaseActivity;
import com.demo.zk.mynews.base.BaseFragment;
import com.demo.zk.mynews.base.BaseFragmentAdapter;
import com.demo.zk.mynews.greendao.NewsChannelTable;
import com.demo.zk.mynews.module.news.presenter.INewsPresenter;
import com.demo.zk.mynews.module.news.presenter.INewsPresenterImpl;
import com.demo.zk.mynews.module.news.view.INewsView;
import com.demo.zk.mynews.utils.RxBus;
import com.demo.zk.mynews.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * ClassName:com.demo.zk.mynews.module.news.ui
 * Author: zk<p>.
 * time: 2016/8/28 10:36.
 * Function: 新闻界面
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_news,
        menuId = R.menu.menu_news,
        hasNavigationView = true,
        toolbarTitle = R.string.news,
        toolbarIndicator = R.drawable.ic_list_white,
        menuDefaultCheckedItem = R.id.action_news)
public class NewsActivity extends BaseActivity<INewsPresenter> implements INewsView {

    private Observable<Boolean> mChannelObservable;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister("channelChange", mChannelObservable);
    }

    @Override
    protected void initView() {

        // 设了默认的windowBackground使得冷启动没那么突兀，这里再设置为空 减少过度绘制
        getWindow().setBackgroundDrawable(null);
        ViewUtil.quitFullScreen(this);

        AppManager.getAppManager().orderNavActivity(getClass().getName(), false);

        mPresenter = new INewsPresenterImpl(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_channel_manage) {
            //  跳转到频道选择界面
            showActivity(this, new Intent(this, NewsChannelActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *  NewwsChannelTable 表示的是频道列表的实体类
     *  包含：
     *  newsChannelName，newsChannelId，newsChannelType，
     *  newsChannelSelect，newsChannelIndex，newsChannelFixed
     * @param newsChannels
     */
    @Override
    public void initViewPager(List<NewsChannelTable> newsChannels) {

        //<include layout="@layout/include_tablayout"/>
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        //<include layout="@layout/content_news"/>
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        List<BaseFragment> fragments = new ArrayList<>();

        final List<String> title = new ArrayList<>();

        if (newsChannels != null) {
            // 有除了固定的其他频道被选中，添加
            for (NewsChannelTable news : newsChannels) {
                final NewsListFragment fragment = NewsListFragment
                        .newInstance(news.getNewsChannelId(), news.getNewsChannelType(),
                                news.getNewsChannelIndex());

                fragments.add(fragment);
                title.add(news.getNewsChannelName());
            }

            if (viewPager.getAdapter() == null) {
                // 初始化ViewPager
                BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(),
                        fragments, title);
                viewPager.setAdapter(adapter);
            } else {
                final BaseFragmentAdapter adapter = (BaseFragmentAdapter) viewPager.getAdapter();
                adapter.updateFragments(fragments, title);
            }
            viewPager.setCurrentItem(0, false);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setScrollPosition(0, 0, true);
            // 根据Tab的长度动态设置TabLayout的模式
            ViewUtil.dynamicSetTabLayoutMode(tabLayout);

            setOnTabSelectEvent(viewPager, tabLayout);

        } else {
            toast("数据异常");
        }

    }

    @Override
    public void initRxBusEvent() {
        mChannelObservable = RxBus.get().register("channelChange", Boolean.class);
        mChannelObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean change) {
                if (change) {
                    mPresenter.operateChannelDb();
                }
            }
        });
    }


}