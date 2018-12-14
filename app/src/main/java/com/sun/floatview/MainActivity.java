package com.sun.floatview;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sun.floatview.core.FloatViewManager;
import com.sun.floatview.core.IconFloatView;

/**
 * @author sxt
 */
public class MainActivity extends AppCompatActivity {

    private IconFloatView mFloatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFloatView = new IconFloatView(this);
        findViewById(R.id.btn_show_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
                    }
                }
                FloatViewManager.getInstance(getApplicationContext()).showFloatView();
            }
        });
        findViewById(R.id.btn_new_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        FloatViewManager.getInstance(getApplicationContext()).hideFloatView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFloatView.onDestroy();
    }
}
