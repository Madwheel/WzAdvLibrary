package com.iwangzhe.wzadvlibrary.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.iwangzhe.wzadvlibrary.R;
import com.iwangzhe.wzadvlibrary.serv.OnWzAdvViewPagerListener;
import com.iwangzhe.wzadvlibrary.tool.WzAdvTool;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1514:55
 * desc   :
 */
public class WzAdvViewPager extends AdvView {
    private Context mContext;
    private RelativeLayout rl_group;
    private RelativeLayout rl_indicator_group;
    private Banner banner;
    private LinearLayout ll_point_group;
    private LinearLayout ll_point;
    private RelativeLayout rl_indicator;
    private TextView tv_title;
    private List<String> mImageUrlList;
    private List<String> mRedirectUrlList;
    private ImageView[] imageViews;
    private OnWzAdvViewPagerListener mListener;
    private OnWzAdvViewPagerListener mCurrentListener;
    private int indicator_selected;
    private int indicator_unselected;

    public WzAdvViewPager(Context context) {
        this(context, null);
    }

    public WzAdvViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.viewpager_wzadv, this);
        initView();
    }

    private void initView() {
        rl_group = findViewById(R.id.rl_slideshow);
        rl_indicator_group = findViewById(R.id.rl_indicator_group);
        banner = findViewById(R.id.banner);
        ll_point_group = findViewById(R.id.ll_point_group);
        ll_point = findViewById(R.id.ll_point);
        rl_indicator = findViewById(R.id.rl_indicator);
        tv_title = findViewById(R.id.tv_title);
    }

    public void bindData(List<String> jumpUrlList, List<String> imageUrlList, final List<Integer> imageMapIdList
            , final List<String> titleList, int indicatorType, int indicatorLocation, int indicatorLaoutBottom
            , final boolean isShowTitle, boolean isAutoPlay, final OnWzAdvViewPagerListener listener) {
        this.mCurrentListener = this.mListener = listener;
        rl_indicator.setGravity(indicatorLocation);
        mImageUrlList = new ArrayList<>();
        LinearLayout pointGroup = ll_point_group;
        if (imageUrlList.size() > 0) {
            rl_group.setVisibility(View.VISIBLE);
            if (indicatorType == 0) {
                rl_indicator_group.setVisibility(GONE);
                ll_point.setVisibility(VISIBLE);
                pointGroup = ll_point;
                indicator_selected = R.drawable.indicator_wzad_rectangle_selected;
                indicator_unselected = R.drawable.indicator_wzad_rectangle_unselected;
            } else {
                pointGroup = ll_point_group;
                rl_indicator_group.setVisibility(VISIBLE);
                ll_point.setVisibility(GONE);
                indicator_selected = R.drawable.indicator_wzad_oval_selected;
                indicator_unselected = R.drawable.indicator_wzad_oval_unselected;
            }
            //添加图片到图片列表里
            mRedirectUrlList = jumpUrlList;
            if (!WzAdvTool.getInstance().compareList(mImageUrlList, imageUrlList)) {
                mImageUrlList.clear();
                mImageUrlList = imageUrlList;
                //添加轮播点
                loadPoint(mImageUrlList, pointGroup, indicatorType, indicatorLaoutBottom);
                //设置图片加载器
                banner.setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        if (mCurrentListener != null) {
                            mCurrentListener.displayImage(context, path, imageView);
                        }
                    }
                });
                banner.setImages(mImageUrlList);
                //设置banner动画效果
                banner.setBannerAnimation(Transformer.Default);
                //设置自动轮播，默认为true
                if (mImageUrlList.size() > 1) {
                    //设置banner样式
                    banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                    //设置图片集合
                    banner.setIndicatorGravity(BannerConfig.CENTER);
                    if (isAutoPlay) {
                        banner.isAutoPlay(true);
                        //设置轮播时间
                        banner.setDelayTime(3000);
                        banner.startAutoPlay();
                    } else {
                        banner.isAutoPlay(false);
                    }
                } else {
                    //设置banner样式
                    banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                    banner.isAutoPlay(false);
                }
                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        if (mCurrentListener != null) {
                            mCurrentListener.onItemClick(position, mImageUrlList.get(position), mRedirectUrlList.get(position), banner.getResources().getResourceEntryName(banner.getId()));
                        }
                    }
                });
                banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float v, int i1) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        mCurrentListener.onItemSelected(imageMapIdList.get(position), position, imageViews.length);
                        // 遍历数组让当前选中图片下的小圆点设置颜色
                        for (int i = 0; i < imageViews.length; i++) {
                            imageViews[position].setBackgroundResource(indicator_selected);
                            if (isShowTitle) {
                                tv_title.setVisibility(VISIBLE);
                                String text = titleList.get(position);
                                tv_title.setText(text);
                            } else {
                                tv_title.setVisibility(GONE);
                            }
                            if (position != i) {
                                imageViews[i].setBackgroundResource(indicator_unselected);
                            }
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {
                    }
                });
                banner.start();
            }
        } else {
            rl_group.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化小点，有几张图片下面就显示几个小圆点
     */
    private void loadPoint(List<String> headerImages, LinearLayout pointGroup, int indicatorType, int indicatorLaoutBottom) {
        if (headerImages.size() == 1) {
            pointGroup.setVisibility(View.GONE);
        } else {
            pointGroup.setVisibility(View.VISIBLE);
        }
        imageViews = new ImageView[headerImages.size()];
        pointGroup.removeAllViews();
        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置每个小圆点距离左边的间距
        if (indicatorLaoutBottom == 0) {
            indicatorLaoutBottom = 3;
        }
        margin.setMargins(WzAdvTool.getInstance().dip2px(mContext, 4)
                , WzAdvTool.getInstance().dip2px(mContext, indicatorLaoutBottom)
                , WzAdvTool.getInstance().dip2px(mContext, 4)
                , WzAdvTool.getInstance().dip2px(mContext, indicatorLaoutBottom));
        for (int i = 0; i < headerImages.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            // 设置每个小圆点的宽高
            if (indicatorType == 0) {
                imageView.setLayoutParams(new ViewGroup.LayoutParams(12, 3));
            } else {
                imageView.setLayoutParams(new ViewGroup.LayoutParams(6, 6));
            }
            imageViews[i] = imageView;
            if (i == 0) {
                // 默认选中第一张图片
                imageViews[i].setBackgroundResource(indicator_selected);
            } else {
                // 其他图片都设置未选中状态
                imageViews[i].setBackgroundResource(indicator_unselected);
            }
            pointGroup.addView(imageViews[i], margin);
            pointGroup.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCurrentListener = null;
        banner.stopAutoPlay();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mCurrentListener == null && mListener != null) {
            mCurrentListener = mListener;
            banner.start();
        }
    }
}
