package com.yoake.tools.helper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class R2ViewHelper {
    /**
     * 获取activity的根view
     */
    public static View getActivityRoot(Activity activity) {
        return ((ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT)).getChildAt(0);
    }
}
