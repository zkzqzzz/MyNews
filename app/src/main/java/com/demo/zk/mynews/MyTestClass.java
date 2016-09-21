package com.demo.zk.mynews;

import com.demo.zk.mynews.base.BaseActivity;
import com.demo.zk.mynews.greendao.NewsChannelTable;
import com.demo.zk.mynews.module.news.presenter.INewsPresenter;
import com.demo.zk.mynews.module.news.view.INewsView;

import java.util.List;

/**
 * com.demo.zk.mynews
 *
 * @author zk
 * @time 2016/9/16 17:54
 * @des TODO
 * <p>
 * <p>
 * Created by Administrator on 2016/9/16.
 */
public class MyTestClass extends BaseActivity<INewsPresenter> implements INewsView {

    @Override
    public void initViewPager(List<NewsChannelTable> newsChannels) {

    }

    @Override
    public void initRxBusEvent() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
