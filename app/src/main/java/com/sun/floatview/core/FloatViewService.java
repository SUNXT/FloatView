package com.sun.floatview.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 悬浮球服务
 * @author sxt
 */
public class FloatViewService extends Service {
    public FloatViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
