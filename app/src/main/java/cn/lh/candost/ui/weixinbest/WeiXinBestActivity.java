package cn.lh.candost.ui.weixinbest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lh.api.AppApi;
import com.lh.api.rxobserver.RxObserver;
import com.lh.base.BaseBean;
import com.lh.base.activity.BaseRVActivity;
import com.lh.base.adapter.MyBaseViewHolder;
import com.lh.ui.common.web.CommonWebAcitivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.lh.R;

import java.util.ArrayList;

import cn.lh.candost.MyApiService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liaohui on 2017/11/14.
 */

public class WeiXinBestActivity extends BaseRVActivity<WeiXinBestBean.ListBean> {

    public static void startActivity(Context mContext) {
        mContext.startActivity(new Intent(mContext, WeiXinBestActivity.class));
    }

    @Override
    protected BaseQuickAdapter<WeiXinBestBean.ListBean, MyBaseViewHolder> getRvAdatper() {
        return new WeiXinBestAdapter(new ArrayList<WeiXinBestBean.ListBean>());
    }

    @Override
    protected View getToolbarLayout() {
        return getDefaultToolbar("开心一刻");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_bar_recycler_refresh_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initRefreshLayout(true, true);
        initRecyclerView(new GridLayoutManager(mContext, 2), null, R.layout.common_empty_view);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CommonWebAcitivity.startActivity(mContext, mAdapter.getItem(position).url.replace("\\", ""), mAdapter.getItem(position).title, true);
            }
        });
    }

    @Override
    protected void initDatas() {
        AppApi.getApiService(MyApiService.class)
                .getWeiXinData("http://v.juhe.cn/weixin/query/", page, pagesize, "7e166b862bbeec73b85974d2b2d8c229", "json")
                .compose(this.<BaseBean<WeiXinBestBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<BaseBean<WeiXinBestBean>>(this, RxObserver.ProgressType.isLoading) {
                    @Override
                    public void onNext(BaseBean<WeiXinBestBean> weiXinBestBeanBaseBean) {
                        if (onHandleBean(weiXinBestBeanBaseBean)) {
                            if (weiXinBestBeanBaseBean.result != null) {
                                if (weiXinBestBeanBaseBean.result.pno == 1) {
                                    mAdapter.replaceData(weiXinBestBeanBaseBean.result.list);
                                    smartRefreshLayout.finishRefresh();
                                } else if (weiXinBestBeanBaseBean.result.pno > 1) {
                                    mAdapter.addData(weiXinBestBeanBaseBean.result.list);
                                    smartRefreshLayout.finishLoadmore();
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        initDatas();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        initDatas();
    }
}
