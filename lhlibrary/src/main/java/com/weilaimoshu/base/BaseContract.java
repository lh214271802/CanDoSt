package com.weilaimoshu.base;

/**
 * Created by liaohui on 2017/9/12.
 */

public interface BaseContract {

    interface BasePresenter<T> {

        void attachView(T view);

        void detachView();
    }

    interface BaseView {

        void showError(Throwable e,String errorMsg);

        void showToast(String message);

        void goLogin();

        void showProgress(boolean isLoading);

        void showRefresh(boolean isRefresh);
    }
}
