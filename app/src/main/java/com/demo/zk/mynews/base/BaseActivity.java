package com.demo.zk.mynews.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.demo.zk.mynews.Annotation.ActivityFragmentInject;
import com.demo.zk.mynews.BuildConfig;
import com.demo.zk.mynews.R;
import com.demo.zk.mynews.app.AppManager;
import com.demo.zk.mynews.module.news.ui.NewsActivity;
import com.demo.zk.mynews.module.photo.ui.PhotoActivity;
import com.demo.zk.mynews.module.settings.ui.SettingsActivity;
import com.demo.zk.mynews.module.video.ui.VideoActivity;
import com.demo.zk.mynews.utils.GlideCircleTransform;
import com.demo.zk.mynews.utils.MeasureUtil;
import com.demo.zk.mynews.utils.RxBus;
import com.demo.zk.mynews.utils.SlidrUtil;
import com.demo.zk.mynews.utils.SpUtil;
import com.demo.zk.mynews.utils.ThemeUtil;
import com.demo.zk.mynews.utils.ViewUtil;
import com.demo.zk.mynews.widget.slidr.model.SlidrInterface;
import com.socks.library.KLog;
import com.zk.changeskin.SkinManager;

import rx.Observable;
import rx.functions.Action1;

/**
 * ClassName: BaseActivity<p>
 * Fuction: Activity的基类<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity
        implements View.OnClickListener, BaseView {

    /**
     * 将代理类通用行为抽出来
     */
    protected T mPresenter;

    /**
     * 标示该activity是否可滑动退出,默认false
     */
    protected boolean mEnableSlidr;

    /**
     * 布局的id
     */
    protected int mContentViewId;

    /**
     * 是否存在NavigationView
     */
    protected boolean mHasNavigationView;

    /**
     * 滑动布局
     */
    protected DrawerLayout mDrawerLayout;

    /**
     * 侧滑导航布局
     */
    protected NavigationView mNavigationView;

    /**
     * 菜单的id
     */
    private int mMenuId;

    /**
     * Toolbar标题
     */
    private int mToolbarTitle;

    /**
     * 默认选中的菜单项
     */
    private int mMenuDefaultCheckedItem;

    /**
     * Toolbar左侧按钮的样式
     */
    private int mToolbarIndicator;

    /**
     * 控制滑动与否的接口
     */
    protected SlidrInterface mSlidrInterface;

    /**
     * 结束Activity的可观测对象
     */
    private Observable<Boolean> mFinishObservable;

    /**
     * 跳转的类
     */
    private Class mClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getClass().isAnnotationPresent(ActivityFragmentInject.class)) {
            ActivityFragmentInject annotation = getClass()
                    .getAnnotation(ActivityFragmentInject.class);
            mContentViewId = annotation.contentViewId();
            mEnableSlidr = annotation.enableSlidr();
            mHasNavigationView = annotation.hasNavigationView();
            mMenuId = annotation.menuId();
            mToolbarTitle = annotation.toolbarTitle();
            mToolbarIndicator = annotation.toolbarIndicator();
            mMenuDefaultCheckedItem = annotation.menuDefaultCheckedItem();
        } else {
            throw new RuntimeException(
                    "Class must add annotations of ActivityFragmentInitParams.class");
        }

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        if (this instanceof SettingsActivity) {
            SkinManager.getInstance().register(this);
        }

        //初始化主题  白or夜
        initTheme();
        //显示view
        setContentView(mContentViewId);

        if (mEnableSlidr && !SpUtil.readBoolean("disableSlide")) {
            // 默认开启侧滑，默认是整个页码侧滑
            mSlidrInterface = SlidrUtil
                    .initSlidrDefaultConfig(this, SpUtil.readBoolean("enableSlideEdge"));
        }

        //是否存在NavigationView  就是左侧的选择
        if (mHasNavigationView) {
            initNavigationView();
            initFinishRxBus();
        }
        //左上方的工具栏
        initToolbar();
        //初始化view   由子类取具体实现
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this instanceof SettingsActivity) {
            SkinManager.getInstance().unregister(this);
        }

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }

        if (mFinishObservable != null) {
            RxBus.get().unregister("finish", mFinishObservable);
        }

        ViewUtil.fixInputMethodManagerLeak(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (mHasNavigationView) {
            KLog.e("FLAG_ACTIVITY_REORDER_TO_FRONT标志位的重新排序去除跳转动画");
            overridePendingTransition(0, 0);
        }
    }

    /**
     * 初始化主题
     */
    private void initTheme() {
        if (this instanceof NewsActivity) {
            setTheme(SpUtil.readBoolean(
                    "enableNightMode") ? R.style.BaseAppThemeNight_LauncherAppTheme : R.style.BaseAppTheme_LauncherAppTheme);
        } else if (!mEnableSlidr && mHasNavigationView) {
            setTheme(SpUtil.readBoolean(
                    "enableNightMode") ? R.style.BaseAppThemeNight_AppTheme : R.style.BaseAppTheme_AppTheme);
        } else {
            setTheme(SpUtil.readBoolean(
                    "enableNightMode") ? R.style.BaseAppThemeNight_SlidrTheme : R.style.BaseAppTheme_SlidrTheme);
        }
    }

    private void initToolbar() {

        // 针对父布局非DrawerLayout的状态栏处理方式
        // 设置toolbar上面的View实现类状态栏效果，这里是因为状态栏设置为透明的了，
        // 而默认背景是白色的，不设的话状态栏处就是白色
        final View statusView = findViewById(R.id.status_view);

        if (statusView != null) {
            statusView.getLayoutParams().height = MeasureUtil.getStatusBarHeight(this);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            if (mToolbarTitle != -1) {
                setToolbarTitle(mToolbarTitle);
            }
            if (mToolbarIndicator != -1) {
                setToolbarIndicator(mToolbarIndicator);
            } else {
                setToolbarIndicator(R.drawable.ic_menu_back);
            }
        }
    }

    protected void setToolbarIndicator(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(resId);
        }
    }

    protected void setToolbarTitle(String str) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(str);
        }
    }

    protected void setToolbarTitle(int strId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(strId);
        }
    }

    protected ActionBar getToolbar() {
        return getSupportActionBar();
    }

    protected View getDecorView() {
        return getWindow().getDecorView();
    }

    protected abstract void initView();

    //侧边栏选择
    private void initNavigationView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        handleStatusView();

        handleAppBarLayoutOffset();

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (mMenuDefaultCheckedItem != -1 && mNavigationView != null) {
            mNavigationView.setCheckedItem(mMenuDefaultCheckedItem);
        }
        //侧边导航点击事件
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        if (item.isChecked()) return true;
                        switch (item.getItemId()) {
                            case R.id.action_news:
                                mClass = NewsActivity.class;
                                break;
                            case R.id.action_video:
                                mClass = VideoActivity.class;
                                break;
                            case R.id.action_photo:
                                mClass = PhotoActivity.class;
                                break;
                            case R.id.action_settings:
                                mClass = SettingsActivity.class;
                                break;
                        }
                        //关闭左侧导航栏
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return false;
                    }
                });
        mNavigationView.post(new Runnable() {
            @Override
            public void run() {
                final ImageView imageView = (ImageView) BaseActivity.this.findViewById(R.id.avatar);
                if (imageView != null) {
                    //换头像
                    Glide.with(mNavigationView.getContext()).load(R.drawable.ic_header).crossFade()
                            .transform(new GlideCircleTransform(mNavigationView.getContext()))
                            .into(imageView);
                }
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerClosed(View drawerView) {
                if (mClass != null) {
                    //不是返回键的情况
                    showActivityReorderToFront(BaseActivity.this, mClass, false);
                    mClass = null;
                }
            }
        });

    }

    /**
     * 处理与AppBarLayout偏移量相关事件处理
     */
    private void handleAppBarLayoutOffset() {
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (appBarLayout != null && toolbar != null) {
            final int toolbarHeight = MeasureUtil.getToolbarHeight(this);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || BaseActivity.this instanceof SettingsActivity) {
                        // appBarLayout偏移量为Toolbar高度，这里为了4.4往上滑的时候由于透明状态栏Toolbar遮不住看起来难看，
                        // 根据偏移量设置透明度制造出往上滑动逐渐消失的效果
                        toolbar.setAlpha((toolbarHeight + verticalOffset) * 1.0f / toolbarHeight);
                    }
                    // AppBarLayout没有偏移量时，告诉Fragment刷新控件可用
                    RxBus.get().post("enableRefreshLayoutOrScrollRecyclerView", verticalOffset == 0);
                }
            });
        }
    }

    /**
     * 处理布局延伸到状态栏，对4.4以上系统有效
     */
    private void handleStatusView() {
        // 针对4.4和SettingsActivity(因为要做换肤，而状态栏在5.0是设置为透明的，若不这样处理换肤时状态栏颜色不会变化)
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || this instanceof SettingsActivity) {

            // 生成一个状态栏大小的矩形
            View statusBarView = ViewUtil
                    .createStatusView(this, ThemeUtil.getColor(this, R.attr.colorPrimary));
            // 添加 statusBarView 到布局中
            ViewGroup contentLayout = (ViewGroup) mDrawerLayout.getChildAt(0);
            contentLayout.addView(statusBarView, 0);
            // 内容布局不是 LinearLayout 时,设置margin或者padding top
            final View view = contentLayout.getChildAt(1);
            final int statusBarHeight = MeasureUtil.getStatusBarHeight(this);
            if (!(contentLayout instanceof LinearLayout) && view != null) {
                view.setPadding(0, statusBarHeight, 0, 0);
            }
            // 设置属性
            ViewGroup drawer = (ViewGroup) mDrawerLayout.getChildAt(1);
            mDrawerLayout.setFitsSystemWindows(false);
            contentLayout.setFitsSystemWindows(false);
            contentLayout.setClipToPadding(true);
            drawer.setFitsSystemWindows(false);

            if (this instanceof SettingsActivity) {
                // 因为要SettingsActivity做换肤，所以statusBarView也要设置
                statusBarView.setTag("skin:primary:background");
                view.setTag("skin:primary:background");
            }

        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            // 5.0以上跟4.4统一，状态栏颜色和Toolbar的一致
            mDrawerLayout.setStatusBarBackgroundColor(ThemeUtil.getColor(this, R.attr.colorPrimary));
        }
    }

    /**
     * 订阅结束自己的事件，这里使用来结束导航的Activity
     */
    private void initFinishRxBus() {
        mFinishObservable = RxBus.get().register("finish", Boolean.class);
        mFinishObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean themeChange) {
                try {
                    if (themeChange && !AppManager.getAppManager().getCurrentNavActivity().getName()
                            .equals(BaseActivity.this.getClass().getName())) {
                        //  切换皮肤的做法是设置页面通过鸿洋大大的不重启换肤，其他后台导航页面的统统干掉，跳转回去的时候，
                        //  因为用了FLAG_ACTIVITY_REORDER_TO_FRONT，发现栈中无之前的activity存在了，就重启设置了主题，
                        // 这样一来就不会所有Activity都做无重启刷新控件更该主题造成的卡顿现象
                        finish();
                    } else if (!themeChange) {
                        // 这个是入口新闻页面退出时发起的通知所有导航页面退出的事件
                        finish();
                        AppManager.getAppManager().clear();
                        KLog.e("订阅事件结束：" + BaseActivity.this.getClass().getName());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    KLog.e("找不到此类");
                }
            }
        });
    }

    protected void showSnackbar(String msg) {
        Snackbar.make(getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnackbar(int id) {
        Snackbar.make(getDecorView(), id, Snackbar.LENGTH_SHORT).show();
    }

    public void showActivityReorderToFront(Activity aty, Class<?> cls, boolean backPress) {

        AppManager.getAppManager().orderNavActivity(cls.getName(), backPress);

        KLog.e("跳转回去：" + cls.getName());

        Intent intent = new Intent();
        intent.setClass(aty, cls);
        // 此标志用于启动一个Activity的时候，若栈中存在此Activity实例，则把它调到栈顶。不创建多一个
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        aty.startActivity(intent);
        overridePendingTransition(0, 0);//切换效果
    }

    public void showActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(mMenuId, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerLayout != null && item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (item.getItemId() == android.R.id.home && mToolbarIndicator == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                // 返回键时未关闭侧栏时关闭侧栏
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (!(this instanceof NewsActivity) && mHasNavigationView) {
                try {
                    showActivityReorderToFront(this,
                            AppManager.getAppManager().getLastNavActivity(), true);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    KLog.e("找不到类名啊");
                }
                return true;
            } else if (this instanceof NewsActivity) {
                // NewsActivity发送通知结束所有导航的Activity
                RxBus.get().post("finish", false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 继承BaseView抽出显示信息通用行为
     *
     * @param msg 信息
     */
    @Override
    public void toast(String msg) {
        showSnackbar(msg);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    /**
     * 用户已选择tab的情况再次点击该tab，列表返回顶部，需要在setupWithViewPager后设置比如监听会把覆盖
     *
     * @param viewPager
     * @param tabLayout TabLayout
     */
    protected void setOnTabSelectEvent(final ViewPager viewPager, final TabLayout tabLayout) {
        tabLayout.setOnTabSelectedListener(new OnTabSelectedListenerAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                RxBus.get().post("enableRefreshLayoutOrScrollRecyclerView", tab.getPosition());
            }
        });
    }

}
