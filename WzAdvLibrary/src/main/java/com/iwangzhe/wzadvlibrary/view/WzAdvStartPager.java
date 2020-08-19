package com.iwangzhe.wzadvlibrary.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iwangzhe.wzadvlibrary.R;
import com.iwangzhe.wzadvlibrary.serv.OnWzAdvStartPagerListener;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/1713:57
 * desc   :
 */
public class WzAdvStartPager extends AdvView {
    private Context mContext;
    private RelativeLayout rl_wzad_item;
    private ImageView iv_wzad;
    private WzAdvCountDownView cpb_wzcountdown;
    private OnWzAdvStartPagerListener mListener;
    private String mUrl;
    private String mTitle;
    private boolean isItemClicked = false;

    public WzAdvStartPager(Context context) {
        this(context, null);
    }

    public WzAdvStartPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.startpager_wzadv, this);
        initView();

    }

    private void initView() {
        rl_wzad_item = findViewById(R.id.rl_wzad_item);
        iv_wzad = findViewById(R.id.iv_wzad);
        cpb_wzcountdown = findViewById(R.id.cpb_wzcountdown);
    }

    public void setData(String url, String imageUrl, String title, int mapId, OnWzAdvStartPagerListener listener) {
        this.mUrl = url;
        this.mListener = listener;
        this.mTitle = title;
        RequestOptions options = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(imageUrl)
                .apply(options)
                .into(iv_wzad);
        if (!TextUtils.isEmpty(mUrl)) {
            cpb_wzcountdown.setVisibility(VISIBLE);
        } else {
            cpb_wzcountdown.setVisibility(GONE);
        }
        cpb_wzcountdown.startCountDown();
        cpb_wzcountdown.setOnCountDownFinishListener(new WzAdvCountDownView.OnCountDownFinishListener() {
            @Override
            public void countDownFinished() {
                if (mListener != null && !isItemClicked) {
                    mListener.onCountDownViewFinish();
                }
            }
        });
        if (mListener != null) {
            mListener.onItemShow(mapId);
        }
        initEvent();
    }

    private void initEvent() {
        // 跳过广告
        cpb_wzcountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCountDownViewClick();
                }
            }
        });
        iv_wzad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mUrl)) {
                    if (mListener != null) {
                        isItemClicked = true;
                        mListener.onItemClick(mUrl, mTitle);
                    }
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isItemClicked = true;
        mListener = null;
    }
}
