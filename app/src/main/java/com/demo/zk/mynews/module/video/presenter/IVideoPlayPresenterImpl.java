package com.demo.zk.mynews.module.video.presenter;


import com.demo.zk.mynews.base.BasePresenterImpl;
import com.demo.zk.mynews.module.video.view.IVideoPlayView;

/**
 * ClassName: IVideoPlayPresenterImpl<p>
 * Fuction: 视频播放代理接口实现<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IVideoPlayPresenterImpl extends BasePresenterImpl<IVideoPlayView, Void> implements IVideoPlayPresenter {

    public IVideoPlayPresenterImpl(IVideoPlayView view, String path, String name) {
        super(view);
        // mView.registerScreenBroadCastReceiver();
        mView.playVideo(path, name);
        mView.showProgress();
    }
}
