package com.demo.zk.mynews.module.video.view;


import com.demo.zk.mynews.base.BaseView;

/**
 * ClassName: IVideoPlayView<p>
 * Fuction: 视频播放视图接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IVideoPlayView extends BaseView{

    void playVideo(String path, String name);

    //    void registerScreenBroadCastReceiver();

}
