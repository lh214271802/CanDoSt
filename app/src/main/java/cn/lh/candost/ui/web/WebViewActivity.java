package cn.lh.candost.ui.web;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lh.base.activity.BaseActivity;

import cn.lh.candost.R;

/**
 * Created by liaohui on 2018/1/29.
 */

public class WebViewActivity extends BaseActivity {
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, WebViewActivity.class));
    }

    @Override
    protected View getToolbarLayout() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initViews() {
        WebView mWebView = findViewById(R.id.web_view);
        mWebView.loadUrl("http://api.viimoo.cn/api_2_0_0_3/html/app/informations.html?uuid=1125901E-B5DE-522C-C68D-A793EB253A27&authsign=");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); //设置适应Html5的一些方法
    }

    @Override
    protected void initDatas() {

    }
}
