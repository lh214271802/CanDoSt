package com.lh.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.lh.R;

/**
 * Created by fan on 2017/8/29.
 * 构造函数---->onMeasure-->onSizeChanged-->onLayout-->onDraw--->视图状态改变（用户操作）-->invalidate-->onDraw
 */

public class LhView extends View {

    private Paint paint;
    private Paint textPaint;

    public LhView(Context context) {
        this(context, null);
    }

    public LhView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LhView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LhView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(SizeUtils.sp2px(33));

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                postInvalidate();
            }
        });
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
     *
     * @see com.scwang.smartrefresh.layout.header.FalsifyHeader
     * getDefaultSize 和resolveSize区别
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
     * 会在你的View第一次指定大小后调用，在因某些原因改变大小后会再次调用
     * 该组件的大小被改变时回调该方法
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 该组件将要绘制他的内容时回调该方法进行绘制
     */
    private int fuck = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.red));
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.green));
        switch (fuck++ % 4) {
            case 0:
                //绘制椭圆
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawOval(0, 0, getWidth(), getHeight() / 2, paint);
                    canvas.drawText("椭圆", 0, 0, textPaint);
                }
                break;
            case 1:
                //绘制圆
                canvas.drawCircle(0, 0, getWidth(), paint);
                canvas.drawText("圆", 0, 0, textPaint);
                break;
            case 2:
                //绘制圆弧
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    canvas.drawArc(0, 0, 100f, 100f, 0, 90, true, paint);
//                }
                canvas.drawArc(new RectF(0, 0, getWidth(), getHeight()), 0, -90, true, paint);
                canvas.drawText("圆弧", 0, 0, textPaint);
                break;
            case 3:
                paint.setColor(ContextCompat.getColor(getContext(), R.color.yellow));
                canvas.drawArc(new RectF(0, 0, getWidth(), getHeight()), 0, 360, true, paint);
                paint.setColor(ContextCompat.getColor(getContext(), R.color.red));
                canvas.drawArc(new RectF(0, 0, getWidth(), getHeight()), 0, 90, true, paint);
                canvas.drawText("圆弧啊啊啊", 0, 0, textPaint);
                break;
        }
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
