package com.lh.api.rxobserver;

import android.net.ParseException;
import android.support.annotation.IntDef;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.lh.base.BaseBean;
import com.lh.base.BaseContract;

import org.json.JSONException;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.ConnectException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.http2.ConnectionShutdownException;
import retrofit2.HttpException;


/**
 * Created by liaohui on 2017/9/20.
 */

public abstract class RxObserver<T> implements Observer<T> {

    /**
     * 进度条的类型
     */
    private final int progressType;
    protected BaseContract.BaseView view;

    /**
     * @param progressType 进度条样式
     */
    public <M extends BaseContract.BaseView> RxObserver(M view, @ProgressType int progressType) {
        this.view = view;
        this.progressType = progressType;
        handleProgress(progressType, true);
    }

    private void handleProgress(@ProgressType int progressType, boolean isNeeded) {
        if (ProgressType.isProgress == progressType) {
            view.showProgress(isNeeded);
        } else if (ProgressType.isLoading == progressType) {
            view.showRefresh(isNeeded);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {
        handleProgress(progressType, false);
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e(e);
        handleProgress(progressType, false);
        String errorMessage = "";
        if (e instanceof ConnectionShutdownException || e instanceof RouteException || e instanceof HttpException || e instanceof ConnectException) {
            errorMessage = "Σ( ° △ °|||)︴网络好像不给力……先喝杯茶吧~\n";
        } else if (e instanceof JsonIOException
                || e instanceof JsonSyntaxException
                || e instanceof JSONException
                || e instanceof JsonParseException
                || e instanceof ParseException) {
            errorMessage = "数据解析异常...";
        } else if (e instanceof IOException) {
            errorMessage = "抱歉，服务器好像出了一点问题，请稍后再试吧⁄(⁄ ⁄•⁄ω⁄•⁄ ⁄)⁄";
        } else if (e instanceof Exception) {
            errorMessage = "未知错误";
        }
        ToastUtils.showShort(errorMessage);
    }

    public boolean onHandleBean(BaseBean baseBean) {
        if ("0".equals(baseBean.error_code)) {
            return true;
        } else if ("1000".equals(baseBean.error_code)) {
            view.goLogin();
        } else {
            onOtherSitutation(baseBean);
        }
        view.showToast(baseBean.msg);
        return false;
    }

    protected void onOtherSitutation(BaseBean baseBean) {

    }


    @IntDef({
            ProgressType.isLoading,
            ProgressType.isProgress,
            ProgressType.none
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProgressType {
        int isProgress = 1;
        int isLoading = 2;
        int none = 3;
    }
}
