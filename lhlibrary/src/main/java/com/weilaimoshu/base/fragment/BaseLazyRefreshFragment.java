package com.weilaimoshu.base.fragment;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.weilaimoshu.R;

import butterknife.BindView;

/**
 * Created by liaohui on 2017/9/29.
 */

public abstract class BaseLazyRefreshFragment extends BaseLazyFragment implements OnRefreshLoadmoreListener {

    @BindView(R.id.smart_refresh_layout)
    protected SmartRefreshLayout smartRefreshLayout;


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
}
