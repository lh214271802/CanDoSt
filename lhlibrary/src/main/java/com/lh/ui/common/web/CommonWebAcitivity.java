package com.lh.ui.common.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.lh.R;
import com.lh.base.activity.BaseActivity;
import com.lh.util.StatusBarUtil;

/**
 * Created by liaohui on 2017/9/22.
 */

public class CommonWebAcitivity extends BaseActivity implements WebViewContract.Operator {

    FrameLayout container;

    private static String WB_URL = "WB_URL";
    private static String CAN_GOBACK = "CAN_BACK";
    private static String TOOLBAR_TITLE = "toolbar_title";
    private WebViewContract.View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container =  findViewById(R.id.container);
    }

    @Override
    public void setDataView(WebViewContract.View view) {
        mView = view;
    }

    @Override
    public void setViewTitle(String title) {
        ((TextView) mCommonToolbar.findViewById(R.id.toolbar_title)).setText(title);
    }

    public static void startActivity(Context context, String url, String toolbarTitle, boolean canBack) {
        context.startActivity(new Intent(context, CommonWebAcitivity.class)
                .putExtra(WB_URL, url)
                .putExtra(CAN_GOBACK, canBack)
                .putExtra(TOOLBAR_TITLE, toolbarTitle));
    }

    @Override
    protected View getToolbarLayout() {
        return getDefaultToolbar(getIntent().getStringExtra(TOOLBAR_TITLE), 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mView.goBack()) finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_container;
    }

    @Override
    protected void initViews() {
        BarUtils.addMarginTopEqualStatusBarHeight(mCommonToolbar);
        StatusBarUtil.StatusBarLightMode(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        FragmentUtils.addFragment(getSupportFragmentManager(), CommonWebFragment.newInstance(getIntent().getStringExtra(WB_URL), true), R.id.container);
    }

    @Override
    protected void initDatas() {

    }

}
