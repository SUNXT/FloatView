package com.sun.floatview.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun.floatview.R;

public class PopItemView extends FrameLayout {

    private String Tag = getClass().getSimpleName();

    private Context mContext;

    private ImageView mIvIcon;
    private TextView mTvText;
    private ImageView mRedPoint;
    private FrameLayout.LayoutParams mRedPointParams;

    private boolean isShowRedPoint;

    public PopItemView(@NonNull Context context){
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.pop_window_item, this, true);
        mIvIcon = findViewById(R.id.iv_item_icon);
        mTvText = findViewById(R.id.tv_item_text);
    }

    /**
     * 显示红点
     */
    public void showRedPoint(){
        if (isShowRedPoint){
            Log.e(Tag,  "The red point had showed. ");
            return;
        }

        if (mRedPoint == null){
            mRedPoint = new ImageView(mContext);
            mRedPoint.setImageResource(R.drawable.sy37_icon_pop_red);
            mRedPointParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mRedPointParams.gravity = Gravity.RIGHT | Gravity.TOP;
            mRedPointParams.rightMargin = DensityUtil.dip2px(mContext, 8);
            mRedPointParams.bottomMargin = DensityUtil.dip2px(mContext, -7);
        }
        addView(mRedPoint, mRedPointParams);
        isShowRedPoint = true;
    }

    /**
     * 隐藏红点
     */
    public void hideRedPoint(){
        if (!isShowRedPoint){
            Log.e(Tag, "The red point not showing. ");
            return;
        }

        removeView(mRedPoint);
        isShowRedPoint = false;
    }

    public void setIconViewRes(int resId){
        mIvIcon.setImageResource(resId);
    }

    public void setText(int resId){
        mTvText.setText(resId);
    }

    public void setText(String text){
        mTvText.setText(text);
    }

}
