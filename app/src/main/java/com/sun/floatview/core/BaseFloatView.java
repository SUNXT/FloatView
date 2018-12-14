package com.sun.floatview.core;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 悬浮球基类
 * @author sxt
 */
public abstract class BaseFloatView {

    private String TAG = "FloatView";

    protected Context mContext;

    protected WindowManager mWindowManager;
    protected WindowManager.LayoutParams mViewParams;
    protected View mView;

    protected boolean isShowing;
    private boolean mTouchable = true;


    /**
     * 创建显示的view
     * @return
     */
    protected abstract View createView();

    /**
     * 创建
     * @param view
     */
    protected abstract void onCreate(View view);

    /**
     * 供子类设置子类触摸行为
     * @return
     */
    protected abstract View.OnTouchListener createTouchListener();

    public BaseFloatView(Context context){
        mContext = context.getApplicationContext();
        init();
    }

    private void init(){
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mView = createView();
        addDefaultViewTouchEvent();
        initViewParams();
        onCreate(mView);
    }

    /**
     * 添加默认的View的触摸事件
     * 具体子类可以在onCreate中view进行添加触摸事件
     */
    private void addDefaultViewTouchEvent(){
        final View.OnTouchListener subClassOnTouchListener = createTouchListener();
        if (mView != null){
            mView.setOnTouchListener(new View.OnTouchListener() {
                private float x;
                private float y;
                private float xMove;
                private float yMove;
                private float xDown;
                private float yDown;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!mTouchable){
                        Log.d(TAG, "The float view is not touchable!");
                        return false;
                    }
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            Log.d(TAG, "Touch event action down!");
                            x = event.getRawX();
                            y = event.getRawY();
                            xDown = x;
                            yDown = y;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.d(TAG, "Touch event action move!");
                            xMove = event.getRawX() - x;
                            yMove = event.getRawY() - y;
                            x = event.getRawX();
                            y = event.getRawY();
                            updatePosition((int) (mViewParams.x + xMove), (int) (mViewParams.y + yMove));
                            break;
                        case MotionEvent.ACTION_UP:
                            boolean isClick = Math.abs(Math.abs(xDown) - Math.abs(event.getRawX())) < 12
                                    && Math.abs(Math.abs(yDown) - Math.abs(event.getRawY())) < 12;
                            if (isClick){
                                v.performClick();
                            }
                            Log.d(TAG, "Touch event action up!");
                            break;
                            default:
                                break;
                    }
                    if (subClassOnTouchListener != null){
                        subClassOnTouchListener.onTouch(v, event);
                    }
                    return true;
                }
            });
        }
    }

    /**
     * 初始化View的参数
     */
    protected void initViewParams(){
        if (mViewParams == null){
            mViewParams = new WindowManager.LayoutParams();
            // 设置window type
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mViewParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }else {
                mViewParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            // 设置图片格式，背景透明
            mViewParams.format = PixelFormat.RGBA_8888;
            // 设置标识
            mViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            // 设置宽高
            mViewParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mViewParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            // 设置gravity
            mViewParams.gravity = Gravity.LEFT | Gravity.TOP;
        }

    }

    /**
     * 获取子View
     * @param id
     * @param <V>
     * @return
     */
    public final <V extends View> V findViewById(int id){
        if (mView != null){
            return mView.findViewById(id);
        }
        return null;
    }

    /**
     * 显示悬浮球
     * @param x
     * @param y
     */
    public void show(int x, int y){
        if (isShowing){
            Log.d(TAG, "The float view is showing.");
            return;
        }

        mViewParams.x = x;
        mViewParams.y = y;
        mWindowManager.addView(mView, mViewParams);
        isShowing = true;

    }

    /**
     * 隐藏悬浮球
     */
    public void hide(){
        if (!isShowing){
            Log.d(TAG, "The float view is not showing. ");
            return;
        }

        if (mView != null){
            mWindowManager.removeView(mView);
            isShowing = false;
        }
    }

    /**
     * 销毁悬浮球，释放资源
     */
    public void onDestroy(){
        hide();
        mView = null;
        mContext = null;
    }

    /**
     * 移动view
     * @param x
     * @param y
     */
    public void updatePosition(int x, int y){
        mViewParams.x = x;
        mViewParams.y = y;
        mWindowManager.updateViewLayout(mView, mViewParams);
    }

    /**
     * 设置悬浮球是否可以触摸
     * @param touchable
     */
    protected void setTouchable(boolean touchable){
        mTouchable = touchable;
    }

}
