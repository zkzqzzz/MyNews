package com.demo.zk.mynews.utils.ijkplayer.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.WindowManager;

/**
 * ClassName: ViewUtil<p>
 * Fuction: 处理屏幕啥的工具<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class ViewUtil {

    public static void rotateScreen(Activity activity) {
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public static void setFullScreen(Activity activity, boolean full) {
        if (full) {
            setFullScreen(activity);
        } else {
            quitFullScreen(activity);
        }
    }

    public static void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void quitFullScreen(Activity activity) {
        final WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


}
