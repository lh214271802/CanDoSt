package com.weilaimoshu.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.weilaimoshu.base.adapter.MyBaseViewHolder;

import java.util.List;

/**
 * Created by liaohui on 2017/9/11.
 */

public abstract class BaseRVAdapter<T> extends BaseQuickAdapter<T, MyBaseViewHolder> {
    public BaseRVAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseRVAdapter(@Nullable List<T> data) {
        super(data);
    }

    public BaseRVAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }
}
