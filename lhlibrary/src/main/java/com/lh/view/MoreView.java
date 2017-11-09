package com.lh.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lh.R;

/**
 * Created by liaohui on 2017/9/29.
 */

public class MoreView extends FrameLayout {
    private ImageView drawableLeft;
    private ImageView drawableRight;
    private TextView moreText;

    public MoreView(@NonNull Context context) {
        this(context, null);
    }

    public MoreView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MoreView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.diy_item_more_view, this, true);
        drawableLeft = (ImageView) findViewById(R.id.drawable_left);
        drawableRight = (ImageView) findViewById(R.id.drawable_right);
        moreText = (TextView) findViewById(R.id.more_text);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MoreViewStyle, 0, 0);
            int leftDrawable = a.getResourceId(R.styleable.MoreViewStyle_drawable_left, 0);
            int rightDrawable = a.getResourceId(R.styleable.MoreViewStyle_drawable_right, 0);
            String title = a.getString(R.styleable.MoreViewStyle_text);
            setViewRes(leftDrawable, rightDrawable, title);
            a.recycle();
        }
    }

    public void setViewRes(int leftRes, int rightRes, String title) {
        if (leftRes != 0) drawableLeft.setImageResource(leftRes);
        if (rightRes != 0) drawableRight.setImageResource(rightRes);
        drawableRight.setVisibility(rightRes == 0 ? GONE : VISIBLE);
        if (!TextUtils.isEmpty(title)) moreText.setText(title);
    }
}
