package com.yoake.video_player.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.yoake.video_player.R;

import xyz.doikki.videoplayer.util.PlayerUtils;

/**
 * 悬浮窗控件（解决滑动冲突）
 * Created by Doikki on 2017/6/8.
 */

@SuppressLint("ViewConstructor")
public class FloatView extends FrameLayout {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    private int mDownRawX, mDownRawY;//手指按下时相对于屏幕的坐标
    private int mDownX, mDownY;//手指按下时相对于悬浮窗的坐标

    public FloatView(@NonNull Context context, int x, int y) {
        super(context);
        mDownX = x;
        mDownY = y;
        init();
    }


    private void init() {
        setBackgroundResource(R.drawable.v_player_shape_float_window_background);
        int padding = PlayerUtils.dp2px(getContext(), 1);
        setPadding(padding, padding, padding, padding);
        initWindow();
    }

    private void initWindow() {
        mWindowManager = PlayerUtils.getWindowManager(getContext());
        mParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        // 设置图片格式，效果为背景透明
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.windowAnimations = R.style.VideoPlayerFloatWindowAnimation;
        mParams.gravity = Gravity.START | Gravity.TOP; // 调整悬浮窗口至右下角
        // 设置悬浮窗口长宽数据
        int width = PlayerUtils.dp2px(getContext(), 160);
        mParams.width = width;
        mParams.height = width * 9 / 16;
        mParams.x = mDownX;
        mParams.y = mDownY;
    }

    /**
     * 添加至窗口
     */
    public boolean addToWindow() {
        if (mWindowManager != null) {
            if (!isAttachedToWindow()) {
                mWindowManager.addView(this, mParams);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 从窗口移除
     */
    public boolean removeFromWindow() {
        if (mWindowManager != null) {
            if (isAttachedToWindow()) {
                mWindowManager.removeViewImmediate(this);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                mDownRawX = (int) ev.getRawX();
                mDownRawY = (int) ev.getRawY();
                mDownX = (int) ev.getX();
                mDownY = (int) (ev.getY() + PlayerUtils.getStatusBarHeight(getContext()));
                break;
            case MotionEvent.ACTION_MOVE:
                float absDeltaX = Math.abs(ev.getRawX() - mDownRawX);
                float absDeltaY = Math.abs(ev.getRawY() - mDownRawY);
                intercepted = absDeltaX > ViewConfiguration.get(getContext()).getScaledTouchSlop() ||
                        absDeltaY > ViewConfiguration.get(getContext()).getScaledTouchSlop();
                break;
        }
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                mParams.x = x - mDownX;
                mParams.y = y - mDownY;
                mWindowManager.updateViewLayout(this, mParams);
                break;
        }
        return super.onTouchEvent(event);
    }
}