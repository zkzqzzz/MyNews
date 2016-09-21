package com.demo.zk.mynews.module.photo.view;


import com.demo.zk.mynews.base.BaseView;
import com.demo.zk.mynews.bean.SinaPhotoDetail;

/**
 * ClassName: IPhotoDetailView<p>
 * Fuction: 图片新闻详情视图接口<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IPhotoDetailView extends BaseView {

    void initViewPager(SinaPhotoDetail photoList);

}
