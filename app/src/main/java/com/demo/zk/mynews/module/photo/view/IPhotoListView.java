package com.demo.zk.mynews.module.photo.view;



import com.demo.zk.mynews.base.BaseView;
import com.demo.zk.mynews.bean.SinaPhotoList;
import com.demo.zk.mynews.common.DataLoadType;

import java.util.List;

/**
 * ClassName: IPhotoListView<p>
 * Author: oubowu<p>
 * Fuction: 图片新闻列表接口<p>
 * CreateDate: 2016/2/21 1:35<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IPhotoListView extends BaseView {

    void updatePhotoList(List<SinaPhotoList.DataEntity.PhotoListEntity> data, @DataLoadType.DataLoadTypeChecker int type);

}
