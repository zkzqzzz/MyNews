package com.demo.zk.mynews.module.photo.presenter;


import com.demo.zk.mynews.base.BasePresenterImpl;
import com.demo.zk.mynews.bean.SinaPhotoDetail;
import com.demo.zk.mynews.module.photo.model.IPhotoDetailInteractor;
import com.demo.zk.mynews.module.photo.model.IPhotoDetailInteractorImpl;
import com.demo.zk.mynews.module.photo.view.IPhotoDetailView;

/**
 * ClassName: IPhotoDetailPresenterImpl<p>
 * Fuction: 图片详情代理接口实现<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class IPhotoDetailPresenterImpl extends BasePresenterImpl<IPhotoDetailView, SinaPhotoDetail>
        implements IPhotoDetailPresenter {

    private IPhotoDetailInteractor<SinaPhotoDetail> mDetailInteractor;

    public IPhotoDetailPresenterImpl(IPhotoDetailView view, String id, SinaPhotoDetail data) {
        super(view);
        mDetailInteractor = new IPhotoDetailInteractorImpl();
        if (data != null) {
            mView.initViewPager(data);
        } else {
            mSubscription = mDetailInteractor.requestPhotoDetail(this, id);
        }
    }

    @Override
    public void requestSuccess(SinaPhotoDetail data) {
        mView.initViewPager(data);
    }
}
