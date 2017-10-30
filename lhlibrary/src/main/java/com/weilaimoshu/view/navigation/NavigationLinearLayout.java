package com.weilaimoshu.view.navigation;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.weilaimoshu.R;

/**
 * Created by liaohui on 2017/9/15.
 * 带边框的LinearLayout
 */

public class NavigationLinearLayout extends LinearLayout {

    private float LEFTBOEDER = 0f;
    private float TOPBOEDER = 0f;
    private float RIGHTBOEDER = 0f;
    private float BOTTOMBOEDER = 0f;

    public NavigationLinearLayout(Context context) {
        super(context);
        init();
    }

    public NavigationLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NavigationLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setBorderLine(float left, float top, float right, float bottom) {
        this.LEFTBOEDER = left;
        this.TOPBOEDER = top;
        this.RIGHTBOEDER = right;
        this.BOTTOMBOEDER = bottom;
    }

    private void init() {
        //设置线条
        ShapeDrawable lineDrawable = new ShapeDrawable(new BorderShape(new RectF(LEFTBOEDER, TOPBOEDER, RIGHTBOEDER, BOTTOMBOEDER)));
        lineDrawable.getPaint().setColor(getResources().getColor(R.color.list_divider_color));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                new ColorDrawable(getResources().getColor(R.color.white)),
                lineDrawable
        });
        this.setBackground(layerDrawable);
    }
}
