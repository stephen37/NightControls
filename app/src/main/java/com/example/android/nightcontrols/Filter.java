package com.example.android.nightcontrols;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by stephen on 28/12/14.
 */
public class Filter extends Service {
    LinearLayout layout;
    SharedUserPreferences shared;
    public static int STATE;
    public static final int INACTIVE = 0;
    public static final int ACTIVE = 0;

    static {
        STATE = INACTIVE;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        layout = new LinearLayout(this);
        layout.setHapticFeedbackEnabled(true);
        shared = new SharedUserPreferences(this);
        layout.setBackgroundColor(shared.getColor());

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT

        );
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(layout, params);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        layout.setBackgroundColor(shared.getColor());
//        layout.setBackgroundColor(getResources().getColor(R.color.test));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (layout != null) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            manager.removeView(layout);
        }
    }

}
