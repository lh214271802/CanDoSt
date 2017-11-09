package com.lh.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.lh.R;
import com.lh.base.permission.PermissionBaseActivity;

/**
 * Created by liaohui on 2017/9/22.
 */

public abstract class BaseRefreshActivity extends PermissionBaseActivity implements OnRefreshLoadmoreListener {

    protected SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smartRefreshLayout = findViewById(R.id.smart_refresh_layout);
    }

    /**
     * 初始化当前页面刷新控件
     */
    protected void initRefreshLayout(boolean refreshable, boolean loadmoreable) {
        initRefreshLayout(refreshable, loadmoreable, new ClassicsHeader(mContext), new ClassicsFooter(mContext));
    }

    protected void initRefreshLayout(boolean refreshable, boolean loadmoreable, RefreshHeader refreshHeader, RefreshFooter refreshFooter) {
        smartRefreshLayout.setEnableRefresh(refreshable);
        smartRefreshLayout.setEnableLoadmore(loadmoreable);
        if (loadmoreable) {
            smartRefreshLayout.autoLoadmore();
        }
        if (refreshable) {
            smartRefreshLayout.setRefreshHeader(refreshHeader);
        }
        if (loadmoreable) {
            smartRefreshLayout.setRefreshFooter(refreshFooter);
        }
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
    }

    @Override
    public void showRefresh(boolean isRefresh) {
        super.showRefresh(isRefresh);
        if (isRefresh) {
        }
        if (smartRefreshLayout.isRefreshing()) {

        }
    }
}
