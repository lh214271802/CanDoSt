package com.lh.ui.common.web;

import android.content.Context;
import android.text.TextUtils;

import com.lh.R;
import com.lh.base.fragment.BaseFragment;
import com.lh.view.progressweb.ProgressWebView;


/**
 * Created by Administrator on 2017/4/8.
 */

public class CommonWebFragment extends BaseFragment implements WebViewContract.View {

    ProgressWebView progressWebview;

    private static CommonWebFragment commonWebFragment;

    private static boolean haveProgress;
    private static String webUrl = "https://www.baidu.com";
    private WebViewContract.Operator mOperator;

    @Override
    public void onAttach(Context context) {
        this.mOperator = (WebViewContract.Operator) context;
        this.mOperator.setDataView(this);
        super.onAttach(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_webview;
    }

    @Override
    protected void initViews() {
        progressWebview =  (ProgressWebView)getView().findViewById(R.id.progress_webview);
        progressWebview.loadUrl(webUrl, haveProgress, new ProgressWebView.OnWebViewCallBack() {
            @Override
            public void onGetWebViewTitle(String title) {
                mOperator.setViewTitle(title);
            }
        });

    }

    @Override
    public void initDatas() {
    }

    public static CommonWebFragment newInstance(String url, boolean hasProgress) {
        if (commonWebFragment == null) {
            commonWebFragment = new CommonWebFragment();
        }
        haveProgress = hasProgress;
        if (!TextUtils.isEmpty(url)) {
            webUrl = url;
        }
        return commonWebFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressWebview != null)
            progressWebview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (progressWebview != null)
            progressWebview.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressWebview != null)
            progressWebview.onDestroy();
    }

    @Override
    public boolean goBack() {
        return progressWebview.goBack();
    }
}
