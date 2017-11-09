package com.lh.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.lh.base.BaseContract;
import com.lh.base.BaseUtils;
import com.lh.base.CommonDialog;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liaohui on 2017/9/12.
 */

public abstract class BaseFragment extends RxFragment implements BaseContract.BaseView {

    protected View contentView;
    protected LayoutInflater inflater;
    protected FragmentActivity activity;
    protected Context mContext;
    private Unbinder unbinder;
    protected CommonDialog mCommonDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(getLayoutId(), container, false);
        }
        activity = getActivity();
        mContext = activity;
        this.inflater = inflater;
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initDatas();
    }

    /**
     * Fragment的布局ID
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

    @Override
    public void showError(Throwable e, String errorMsg) {

    }

    @Override
    public void showToast(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void goLogin() {
        BaseUtils.clearLoginInfo(mContext,true);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissDialog();
        unbinder.unbind();
        contentView = null;
        inflater = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnclicks(View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    protected void showDialog() {
        showDialog("加载中...");
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

}
