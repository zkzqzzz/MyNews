package com.demo.zk.mynews.module.video.view;


import com.demo.zk.mynews.base.BaseView;
import com.demo.zk.mynews.bean.NeteastVideoSummary;
import com.demo.zk.mynews.common.DataLoadType;

import java.util.List;

/**
 * ClassName: IVideoListView<p>
 * Fuction: 视频列表视图接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IVideoListView extends BaseView {

    void updateVideoList(List<NeteastVideoSummary> data, @DataLoadType.DataLoadTypeChecker int type);

}
