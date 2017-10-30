package com.weilaimoshu.ui.common.web;

/**
 * WebView界面建立契约关系
 * Created by liaohui on 2017/9/30.
 */

public interface WebViewContract {
    interface Operator {
        void setDataView(View view);

        void setViewTitle(String title);
    }

    interface View {
        boolean goBack();
    }

}
