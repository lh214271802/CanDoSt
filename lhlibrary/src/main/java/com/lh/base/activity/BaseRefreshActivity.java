package com.lh.base.activity;

import com.lh.R;
import com.lh.base.permission.PermissionBaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

/**
 * Created by liaohui on 2017/9/22.
 */

public abstract class BaseRefreshActivity extends PermissionBaseActivity implements OnRefreshLoadmoreListener {

    protected SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void initViews() {
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
        smartRefreshLayout.setEnableAutoLoadmore(loadmoreable);
        if (refreshable) {
            smartRefreshLayout.autoRefresh();
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
