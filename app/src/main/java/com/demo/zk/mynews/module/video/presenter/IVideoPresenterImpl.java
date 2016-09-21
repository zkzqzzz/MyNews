package com.demo.zk.mynews.module.video.presenter;


import com.demo.zk.mynews.base.BasePresenterImpl;
import com.demo.zk.mynews.module.video.view.IVideoView;

/**
 * ClassName: IVideoPresenterImpl<p>
 * Fuction: 视频代理接口实现<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IVideoPresenterImpl extends BasePresenterImpl<IVideoView, Void>
        implements IVideoPresenter {

    public IVideoPresenterImpl(IVideoView view) {
        super(view);
        mView.initViewPager();
    }


}
