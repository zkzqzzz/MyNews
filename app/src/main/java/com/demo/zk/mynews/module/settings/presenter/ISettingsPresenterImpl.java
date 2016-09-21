package com.demo.zk.mynews.module.settings.presenter;


import com.demo.zk.mynews.base.BasePresenterImpl;
import com.demo.zk.mynews.module.settings.view.ISettingsView;

/**
 * ClassName: <p>
 * Fuction: <p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class ISettingsPresenterImpl extends BasePresenterImpl<ISettingsView, Void> implements ISettingsPresenter{

    public ISettingsPresenterImpl(ISettingsView view) {
        super(view);
        mView.initItemState();
    }
}
