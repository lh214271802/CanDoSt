package com.lh.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by fan on 2017/8/29.
 */

public class LhView extends View {
    public LhView(Context context) {
        super(context);
    }

    public LhView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LhView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LhView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 当应用从XML文件中加载该组件并利用它来构建界面之后，该方法将会被回调
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    /**
     * 用来检测View组件及其所包含的所有子组件的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 需要分配其子组件的位置，大小时，该方法就会被回调
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 该组件的大小被改变时回调该方法
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 该组件将要绘制他的内容时回调该方法进行绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 当某个键被按下时调用该方法
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 当某个键被松开时调用该方法
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 当发生轨迹球事件时触发该方法
     */
    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return super.onTrackballEvent(event);
    }

    /**
     * 当发生触摸屏事件时触发该方法
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 当组件焦点发生改变时触发该方法
     */
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    /**
     * 当包含该组件的窗口失去或得到焦点时触发该方法
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    /**
     * 把该组件放入某个窗口时触发该方法
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 把该组件从某个窗口上分离时触发该方法
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * 当包含该组件的窗口的可见性发生改变时触发该方法
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }
}
