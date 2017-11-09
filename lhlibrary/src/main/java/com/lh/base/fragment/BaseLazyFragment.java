package com.lh.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.shizhefei.fragment.LazyFragment;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.lh.base.BaseContract;
import com.lh.base.BaseUtils;
import com.lh.base.CommonDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by liaohui on 2017/9/29.
 */

public abstract class BaseLazyFragment extends LazyFragment implements FragmentLifecycleProvider, BaseContract.BaseView {
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    protected Context mContext;
    protected FragmentActivity activity;
    protected CommonDialog mCommonDialog;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
        mContext = getContext().getApplicationContext();
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this, getContentView());
        activity = getActivity();
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

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bindFragment(lifecycleSubject);
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    protected void onPauseLazy() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPauseLazy();
    }

    @Override
    protected void onFragmentStopLazy() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onFragmentStopLazy();
    }

    @Override
    protected void onDestroyViewLazy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyViewLazy();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
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
