package cn.lh.candost.ui.taoke;

import com.lh.base.BaseContract;
import com.trello.rxlifecycle2.LifecycleTransformer;

public interface TaoKeContract {
    interface View extends BaseContract.BaseView {

        void setDatas(TaoKeBean taoKeBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void getTaokeData(String url, int page, String key, LifecycleTransformer<TaoKeBean> lifecycleTransformer);
    }
}