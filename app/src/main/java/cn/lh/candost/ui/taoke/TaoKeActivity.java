package cn.lh.candost.ui.taoke;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lh.base.activity.BaseRVActivity;
import com.lh.base.adapter.MyBaseViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;

/**
 * Created by liaohui on 2017/11/15.
 */

public class TaoKeActivity extends BaseRVActivity<TaoKeBean.DataBean> implements TaoKeContract.View {

    private TaoKePresenter presenter;

    public static void startActivity(Context mContext) {
        mContext.startActivity(new Intent(mContext, TaoKeActivity.class));
    }

    @Override
    protected BaseQuickAdapter<TaoKeBean.DataBean, MyBaseViewHolder> getRvAdatper() {
        return new TaoKeAdapter(new ArrayList<TaoKeBean.DataBean>());
    }

    @Override
    protected View getToolbarLayout() {
        return getDefaultToolbar("淘啊淘");
    }

    @Override
    protected int getLayoutId() {
        return com.lh.R.layout.common_bar_recycler_refresh_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        presenter = new TaoKePresenter();
        presenter.attachView(this);
        initRefreshLayout(true, true);
        initRecyclerView(new GridLayoutManager(mContext, 2), null, com.lh.R.layout.common_empty_view);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        getListDatas();
    }

    private void getListDatas() {
        presenter.getTaokeData("http://api.taokezhushou.com/api/v1/all", page, "2ee7d41d3a2b3711", bindToLifecycle());

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        getListDatas();
    }

    @Override
    public void setDatas(TaoKeBean taoKeBean) {
        if (200 == taoKeBean.code) {
            if (page == 1) {
                mAdapter.replaceData(taoKeBean.data);
                smartRefreshLayout.finishRefresh();
            } else {
                mAdapter.addData(taoKeBean.data);
                smartRefreshLayout.finishLoadmore();
            }
        } else {
            ToastUtils.showShort("Σ( ° △ °|||)︴网络好像不给力……先喝杯茶吧~\n");
        }
    }
}
