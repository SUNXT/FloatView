package com.sun.floatview.core;

import android.content.Context;

/**
 * @author sxt
 */
public class FloatViewManager {

    private static FloatViewManager sInstance;
    private IconFloatView mFloatView;


    private FloatViewManager(Context context){
        mFloatView = new IconFloatView(context);

    }

    public static FloatViewManager getInstance(Context context){
        if (sInstance == null){
            sInstance = new FloatViewManager(context.getApplicationContext());
        }
        return sInstance;
    }

    public void showFloatView(){
        mFloatView.showOnLeft();
    }

    public void hideFloatView(){
        mFloatView.hide();
    }

    public void destroyView(){
        mFloatView.onDestroy();
    }

}
