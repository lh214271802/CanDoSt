package com.lh.base.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lh.R;
import com.lh.base.adapter.MyBaseViewHolder;
import com.lh.view.DividerDecoration;
import com.lh.view.SupportGridItemDecoration;

/**
 * Created by liaohui on 2017/9/13.
 */

public abstract class BaseLazyRVFragment<T> extends BaseLazyRefreshFragment {

    protected RecyclerView recyclerView;
    //    protected BaseRVAdapter<T> mAdapter;
    protected BaseQuickAdapter<T, MyBaseViewHolder> mAdapter;
    protected int page = 0;
    protected int pagesize = 9;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        if (recyclerView == null) {
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        }
    }

    /**
     * 初始化当前页面RecyclerView及其数据
     */
    protected void initRecyclerView() {
        initRecyclerView(new LinearLayoutManager(mContext), new DividerDecoration(ContextCompat.getColor(mContext, R.color.whiteDF), 1, 0, 0), R.layout.common_empty_layout);
    }


    protected void initRecyclerView(RecyclerView.LayoutManager manager, RecyclerView.ItemDecoration decoration, int emptyLayoutId) {
        recyclerView.setLayoutManager(manager);
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        if (mAdapter == null) {
            mAdapter = getRvAdatper();
            mAdapter.bindToRecyclerView(recyclerView);
            mAdapter.setEmptyView(emptyLayoutId);
        }
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * 获得当前列表页面适配器
     */
    protected abstract BaseQuickAdapter<T, MyBaseViewHolder> getRvAdatper();
}
