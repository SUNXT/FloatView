package com.sun.floatview.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.sun.floatview.R;

/**
 * @author sxt
 */
public class IconFloatView extends BaseFloatView {

    private String TAG = getClass().getSimpleName();

    private int mViewWidth;
    private int mViewHeight;

    /**
     * 悬浮窗view
     */
    private FrameLayout mContentView;
    private ImageView mCenterView;
    private ImageView mRedPointView;

    private FrameLayout.LayoutParams mRedPointViewParams;

    private PopupWindow mPopupWindow;
    private boolean isFloatOnLeft = true;

    public IconFloatView(Context context){
        super(context);
    }

    @Override
    protected View createView() {
        mContentView = new FrameLayout(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContentView.setLayoutParams(params);
        mCenterView = new ImageView(mContext);
        mCenterView.setImageResource(R.drawable.sy37_wm_img_move);
        mRedPointView = new ImageView(mContext);
        mRedPointView.setImageResource(R.drawable.sy37_icon_pop_red);
        mRedPointViewParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRedPointViewParams.gravity = Gravity.RIGHT | Gravity.TOP;
        mContentView.addView(mCenterView, params);
        mContentView.addView(mRedPointView, mRedPointViewParams);
        return mContentView;
    }

    @Override
    protected void onCreate(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击悬浮球" + mPopupWindow.isShowing(), Toast.LENGTH_SHORT).show();
                if (mPopupWindow.isShowing()){
                    Log.d(TAG, "隐藏悬浮球");
                    mPopupWindow.dismiss();
                    setTouchable(true);
                }else {
                    Log.d(TAG, "点击显示");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        mPopupWindow.setAttachedInDecor(false);
                    }
                    if (isFloatOnLeft){
                        mPopupWindow.setAnimationStyle(R.style.PopupWindowAnimStyleLeftIn);
                        mPopupWindow.showAtLocation(v, Gravity.LEFT | Gravity.CENTER, DensityUtil.dip2px(mContext, 50f), 0);
                    }else {
                        mPopupWindow.setAnimationStyle(R.style.PopupWindowAnimStyleRightIn);
                        mPopupWindow.showAtLocation(v, Gravity.RIGHT | Gravity.CENTER, DensityUtil.dip2px(mContext, 50f), 0);
                    }
                    setTouchable(false);
                }
            }
        });
        view.measure(0, 0);
        mViewWidth = view.getMeasuredWidth();
        mViewHeight = view.getMeasuredHeight();
        // 初始化弹出菜单
        LinearLayout popView = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = DensityUtil.dip2px(mContext, 10);
        layoutParams.rightMargin = DensityUtil.dip2px(mContext, 10);
        layoutParams.gravity = Gravity.CENTER;
        popView.setBackgroundResource(R.drawable.sy37_pop_bg);
        popView.setLayoutParams(layoutParams);
        PopItemView popItemView = new PopItemView(mContext);
        popItemView.setText(R.string.pop_user);
        popItemView.setIconViewRes(R.drawable.sy37_icon_pop_user);
        popItemView.showRedPoint();
        popView.addView(popItemView);
        popItemView = new PopItemView(mContext);
        popItemView.setText("礼包");
        popItemView.setIconViewRes(R.drawable.sy37_icon_pop_card);
        popItemView.showRedPoint();
        popView.addView(popItemView);
        mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(false);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected View.OnTouchListener createTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
                        int screenCenter = (screenWidth - mViewWidth) / 2;
                        Log.d(TAG, "进行贴边处理");
                        if (event.getRawX() < screenCenter){
                            Log.d(TAG,"往左边贴边");
                            isFloatOnLeft = true;
                            updatePosition(0, mViewParams.y);
                        }else {
                            Log.d(TAG, "往右边贴边");
                            isFloatOnLeft = false;
                            updatePosition(screenWidth, mViewParams.y);
                        }
                        break;
                        default:break;
                }
                return false;
            }
        };
    }

    /**
     * 在屏幕左边显示
     */
    public void showOnLeft(){
        isFloatOnLeft = true;
        show(0, (mWindowManager.getDefaultDisplay().getHeight() - mViewHeight) / 2);
    }

    public void showOnRight(){
        isFloatOnLeft = false;
        show(mWindowManager.getDefaultDisplay().getWidth(), (mWindowManager.getDefaultDisplay().getHeight() - mViewHeight) / 2);
    }

}
