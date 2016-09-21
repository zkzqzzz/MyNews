package com.demo.zk.mynews.module.news.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo.zk.mynews.Annotation.ActivityFragmentInject;
import com.demo.zk.mynews.R;
import com.demo.zk.mynews.base.BaseActivity;
import com.demo.zk.mynews.bean.NeteastNewsDetail;
import com.demo.zk.mynews.bean.SinaPhotoDetail;
import com.demo.zk.mynews.module.news.presenter.INewsDetailPresenter;
import com.demo.zk.mynews.module.news.presenter.INewsDetailPresenterImpl;
import com.demo.zk.mynews.module.news.view.INewsDetailView;
import com.demo.zk.mynews.module.photo.ui.PhotoDetailActivity;
import com.demo.zk.mynews.utils.MeasureUtil;
import com.demo.zk.mynews.utils.ViewUtil;
import com.demo.zk.mynews.widget.ThreePointLoadingView;

import java.util.ArrayList;

import zhou.widget.RichText;

/**
 * ClassName: NewsDetailActivity<p>
 * Fuction: 新闻详情界面<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_news_detail,
        menuId = R.menu.menu_settings,
        enableSlidr = true)
public class NewsDetailActivity extends BaseActivity<INewsDetailPresenter> implements INewsDetailView {

    private ThreePointLoadingView mLoadingView;
    private ImageView mNewsImageView;
    private TextView mTitleTv;
    private TextView mFromTv;
    private RichText mBodyTv;

    private String mNewsListSrc;

    private SinaPhotoDetail mSinaPhotoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            // 设置全屏，并且不会Activity的布局让出状态栏的空间
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            ViewUtil.showStatusBar(this);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            // 4.4设置全屏并动态修改Toolbar的位置实现类5.0效果，确保布局延伸到状态栏的效果
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
            mlp.topMargin = MeasureUtil.getStatusBarHeight(this);
        }

        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(getString(R.string.news_detail));
        toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.accent));
        toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.material_white));

        mNewsImageView = (ImageView) findViewById(R.id.iv_news_detail_photo);

        mLoadingView = (ThreePointLoadingView) findViewById(R.id.tpl_view);
        mLoadingView.setOnClickListener(this);

        mTitleTv = (TextView) findViewById(R.id.tv_news_detail_title);

        mFromTv = (TextView) findViewById(R.id.tv_news_detail_from);

        mBodyTv = (RichText) findViewById(R.id.tv_news_detail_body);

        findViewById(R.id.fab).setOnClickListener(this);

        mNewsListSrc = getIntent().getStringExtra("imgsrc");

        mPresenter = new INewsDetailPresenterImpl(this, getIntent().getStringExtra("postid"));

    }

    @Override
    public void initNewsDetail(NeteastNewsDetail data) {
        if (data.img != null && data.img.size() > 0) {
            // 设置tag用于点击跳转浏览图片列表的时候判断是否有图片可供浏览
            mNewsImageView.setTag(R.id.img_tag, true);
            // 显示第一张图片，通过pixel字符串分割得到图片的分辨率
            String[] pixel = null;
            if (!TextUtils.isEmpty(data.img.get(0).pixel)) {
                // pixel可能为空
                pixel = data.img.get(0).pixel.split("\\*");
            }
            // 图片高清显示，按屏幕宽度为准缩放
            if (pixel != null && pixel.length == 2) {

                int w = MeasureUtil.getScreenSize(this).x;
                int h = Integer.parseInt(pixel[1]) * w / Integer.parseInt(pixel[0]);

                if (data.img.get(0).src.contains(".gif")) {
                    Glide.with(this).load(data.img.get(0).src).asGif().placeholder(R.drawable.ic_loading).error(R.drawable.ic_fail)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).override(w, h).into(mNewsImageView);
                } else {
                    Glide.with(this).load(data.img.get(0).src).asBitmap().placeholder(R.drawable.ic_loading).format(DecodeFormat.PREFER_ARGB_8888)
                            .error(R.drawable.ic_fail).diskCacheStrategy(DiskCacheStrategy.ALL).override(w, h).into(mNewsImageView);
                }
            } else {
                Glide.with(this).load(data.img.get(0).src).asBitmap().placeholder(R.drawable.ic_loading).format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ic_fail).into(mNewsImageView);
            }

            // 以下将数据封装成新浪需要的格式，用于点击跳转到图片浏览
            mSinaPhotoDetail = new SinaPhotoDetail();
            mSinaPhotoDetail.data = new SinaPhotoDetail.SinaPhotoDetailDataEntity();
            mSinaPhotoDetail.data.title = data.title;
            mSinaPhotoDetail.data.content = data.digest;
            mSinaPhotoDetail.data.pics = new ArrayList<>();
            for (NeteastNewsDetail.ImgEntity entiity : data.img) {
                SinaPhotoDetail.SinaPhotoDetailPicsEntity sinaPicsEntity = new SinaPhotoDetail.SinaPhotoDetailPicsEntity();
                sinaPicsEntity.pic = entiity.src;
                sinaPicsEntity.alt = entiity.alt;
                sinaPicsEntity.kpic = entiity.src;
                if (pixel != null && pixel.length == 2) {
                    // 新浪分辨率是按100x100这种形式的
                    sinaPicsEntity.size = pixel[0] + "x" + pixel[1];
                }
                mSinaPhotoDetail.data.pics.add(sinaPicsEntity);
            }

        } else {
            // 图片详情列表没有数据的时候，取图片列表页面传送过来的图片显示
            mNewsImageView.setTag(R.id.img_tag, false);
            Glide.with(this).load(mNewsListSrc).asBitmap().placeholder(R.drawable.ic_loading).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .format(DecodeFormat.PREFER_ARGB_8888).error(R.drawable.ic_fail).into(mNewsImageView);
        }

        mTitleTv.setText(data.title);
        // 设置新闻来源和发布时间
        mFromTv.setText(getString(R.string.from, data.source, data.ptime));
        // 新闻内容可能为空
        if (!TextUtils.isEmpty(data.body)) {
            mBodyTv.setRichText(data.body);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
    }

    @Override
    public void showProgress() {
        mLoadingView.play();
    }

    @Override
    public void hideProgress() {
        mLoadingView.stop();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            if (!(boolean) mNewsImageView.getTag(R.id.img_tag) || mSinaPhotoDetail == null) {
                toast("没有图片供浏览哎o(╥﹏╥)o");
            } else {
                Intent intent = new Intent(this, PhotoDetailActivity.class);
                intent.putExtra("neteast", mSinaPhotoDetail);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                ActivityCompat.startActivity(this, intent, options.toBundle());
            }
        }
    }

}