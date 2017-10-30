package com.weilaimoshu.base.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.weilaimoshu.R;
import com.weilaimoshu.base.adapter.MyBaseViewHolder;
import com.weilaimoshu.view.DividerDecoration;

import butterknife.BindView;

/**
 * Created by liaohui on 2017/9/11.
 */

public abstract class BaseRVActivity<T> extends BaseRefreshActivity implements OnRefreshLoadmoreListener {
    @BindView(R.id.recyclerview)
    protected RecyclerView recyclerView;

    //    protected BaseRVAdapter<T> mAdapter;
    protected BaseQuickAdapter<T, MyBaseViewHolder> mAdapter;
    protected int page = 0;
    protected int pagesize = 9;


    /**
     * 初始化当前页面RecyclerView及其数据
     */
    protected void initRecyclerView() {
        initRecyclerView(new LinearLayoutManager(mContext), R.layout.common_empty_layout);
    }

    protected void initRecyclerView(RecyclerView.LayoutManager manager, int emptyLayoutId) {
        recyclerView.setLayoutManager(manager);
        if (manager instanceof LinearLayoutManager) {
            recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(mContext, R.color.whiteDF), 1, 0, 0));
        }
        if (mAdapter == null) {
            mAdapter = getRvAdatper();
            mAdapter.bindToRecyclerView(recyclerView);
            mAdapter.setEmptyView(emptyLayoutId);
            //mAdapter.setLoadMoreView(loadMoreView);
        }
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 获得当前列表页面适配器
     */
    protected abstract BaseQuickAdapter<T, MyBaseViewHolder> getRvAdatper();
/*
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mAdapter.loadMoreEnd(false);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }

    public void loadSuccess() {
        mAdapter.loadMoreEnd(true);
        mAdapter.loadMoreComplete();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
    }

    public void loadFail() {
        mAdapter.loadMoreFail();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
    }*/
}
