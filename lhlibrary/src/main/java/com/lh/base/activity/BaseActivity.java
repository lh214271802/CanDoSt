package com.lh.base.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.lh.BuildConfig;
import com.lh.R;
import com.lh.base.BaseContract;
import com.lh.base.BaseUtils;
import com.lh.base.CommonDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liaohui on 2017/9/8.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements BaseContract.BaseView {

    protected Toolbar mCommonToolbar;

    protected Context mContext;

    protected CommonDialog mCommonDialog;

    private Unbinder unbinder;
    /**
     * 当前Activity渲染的视图View
     */
    protected View contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseView(getLayoutId());
        mContext = this;
        unbinder = ButterKnife.bind(this);
        mCommonToolbar = (Toolbar) findViewById(R.id.common_toolbar);
        if (mCommonToolbar != null) {
            mCommonToolbar.removeAllViews();
            mCommonToolbar.addView(getToolbarLayout(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            setSupportActionBar(mCommonToolbar);
        }
        initViews();
        initDatas();
           /*友盟的配置：设置是否对日志信息进行加密, 默认false(不加密)*/
        if (!BuildConfig.DEBUG) {
            //umeng analytics
            MobclickAgent.openActivityDurationTrack(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
            } else {
                AnalyticsConfig.sEncrypt = true;//6.0.0版本以前
            }
        }
    }

    protected void setBaseView(@LayoutRes int layoutId) {
        setContentView(contentView = LayoutInflater.from(this).inflate(layoutId, null));
    }

    /**
     * 为了可以保存额外更多的数据到saved instance state。
     * 在Activity的生命周期里面存在一个额外的回调函数，
     * 你必须重写这个函数。该回调函数并没有在前面课程的图片示例中显示。
     * 这个方法是onSaveInstanceState() ，
     * 当用户离开Activity时，系统会调用它。
     * 当系统调用这个函数时，系统会在Activity被异常Destory时传递 Bundle 对象，
     * 这样我们就可以增加额外的信息到Bundle中并保存到系统中。
     * 若系统在Activity被Destory之后想重新创建这个Activity实例时，
     * 之前的Bundle对象会(系统)被传递到你我们activity的onRestoreInstanceState()方法与 onCreate() 方法中。
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    public void showError(Throwable e, String errorMsg) {

    }

    @Override
    public void showToast(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void goLogin() {
        BaseUtils.clearLoginInfo(mContext, true);
    }

    @Override
    public void showProgress(boolean isLoading) {
        if (isLoading) {
            showDialog();
        } else {
            dismissDialog();
        }
    }

    @Override
    public void showRefresh(boolean isRefresh) {

    }

    /**
     * 初始化当前页面的toolbar布局
     */
    protected abstract View getToolbarLayout();

    /**
     * Activity的布局ID
     */
    protected abstract
    @LayoutRes
    int getLayoutId();

    /**
     * 初始化当前页面的控件
     */
    protected abstract void initViews();

    /**
     * 初始化当前页面的数据
     */
    protected abstract void initDatas();


    //TODO 关于数据状态保存相关-->http://blog.csdn.net/liubin8095/article/details/9328563
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        unbinder.unbind();
        System.gc();
    }

    public void setOnclicks(View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    protected void showDialog() {
        showDialog("请求中...");
    }

    protected void showDialog(String message) {
        mCommonDialog = getDialog(message);
        mCommonDialog.show();
    }

    protected CommonDialog getDialog(String message) {
        return mCommonDialog != null ? mCommonDialog : CommonDialog.getInstance(mContext, message);
    }

    protected void dismissDialog() {
        if (mCommonDialog != null) {
            mCommonDialog.dismiss();
            mCommonDialog = null;
        }
    }

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    protected View getDefaultToolbar(String title, int rightIcon, View.OnClickListener leftListener) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.common_toolbar_title, null);
        ImageView toolbarLeft = view.findViewById(R.id.toolbar_left);
        ImageView toolbarRight = view.findViewById(R.id.toolbar_right);
        TextView toolbarTitle = view.findViewById(R.id.toolbar_title);

        if (leftListener != null) {
            toolbarLeft.setOnClickListener(leftListener);
        }
        toolbarRight.setVisibility(rightIcon != 0 ? View.VISIBLE : View.GONE);
        if (rightIcon != 0) {
            toolbarRight.setImageResource(rightIcon);
        }
        toolbarLeft.setImageResource(R.drawable.img_back_black);
        toolbarTitle.setText(title);
        return view;
    }

    protected View getDefaultToolbar(String title) {
        return getDefaultToolbar(title, 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = view.getId();
                if (i == R.id.toolbar_left) {
                    finish();

                }
            }
        });
    }
}
