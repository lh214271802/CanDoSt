package cn.lh.candost.ui.taoke;

import com.lh.api.AppApi;
import com.lh.api.rxobserver.RxObserver;
import com.lh.base.RxPresenter;
import com.trello.rxlifecycle2.LifecycleTransformer;

import cn.lh.candost.MyApiService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liaohui on 2018/3/13.
 */

public class TaoKePresenter extends RxPresenter<TaoKeContract.View> implements TaoKeContract.Presenter<TaoKeContract.View> {


    @Override
    public void getTaokeData(String url, int page, String key, LifecycleTransformer<TaoKeBean> lifecycleTransformer) {
        AppApi.getApiService(MyApiService.class)
                .getTaokeData("http://api.taokezhushou.com/api/v1/all", page, "2ee7d41d3a2b3711")
                .compose(lifecycleTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<TaoKeBean>(mView, RxObserver.ProgressType.isLoading) {
                    @Override
                    public void onNext(TaoKeBean taoKeBean) {
                        mView.setDatas(taoKeBean);
                    }
                });
    }
}
