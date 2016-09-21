package com.demo.zk.mynews.module.video.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.demo.zk.mynews.Annotation.ActivityFragmentInject;
import com.demo.zk.mynews.R;
import com.demo.zk.mynews.base.BaseActivity;
import com.demo.zk.mynews.base.BaseFragment;
import com.demo.zk.mynews.base.BaseFragmentAdapter;
import com.demo.zk.mynews.http.Api;
import com.demo.zk.mynews.module.video.presenter.IVideoPresenter;
import com.demo.zk.mynews.module.video.presenter.IVideoPresenterImpl;
import com.demo.zk.mynews.module.video.view.IVideoView;
import com.demo.zk.mynews.utils.RxBus;
import com.demo.zk.mynews.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * ClassName: VideoActivity<p>
 * Fuction: 视频界面<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_video,
        menuId = R.menu.menu_video,
        hasNavigationView = true,
        toolbarTitle = R.string.video,
        toolbarIndicator = R.drawable.ic_list_white,
        menuDefaultCheckedItem = R.id.action_video)
public class VideoActivity extends BaseActivity<IVideoPresenter> implements IVideoView {

    private View mBg;
    private Observable<Boolean> mBgObservable;

    @Override
    protected void initView() {

        mPresenter = new IVideoPresenterImpl(this);

        mBg = findViewById(R.id.bg);

        mBgObservable = RxBus.get().register("Bg", Boolean.class);
        mBgObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    mBg.setVisibility(View.INVISIBLE);
                } else {
                    mBg.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister("Bg", mBgObservable);
    }

    @Override
    public void initViewPager() {

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        List<BaseFragment> fragments = new ArrayList<>();
        final List<String> title = Arrays.asList("热点", "娱乐", "搞笑", "精品");

        fragments.add(VideoListFragment.newInstance(Api.VIDEO_HOT_ID, 0));
        fragments.add(VideoListFragment.newInstance(Api.VIDEO_ENTERTAINMENT_ID, 1));
        fragments.add(VideoListFragment.newInstance(Api.VIDEO_FUN_ID, 2));
        fragments.add(VideoListFragment.newInstance(Api.VIDEO_CHOICE_ID, 3));

        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments, title);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        ViewUtil.dynamicSetTabLayoutMode(tabLayout);

        setOnTabSelectEvent(viewPager, tabLayout);

    }

}
