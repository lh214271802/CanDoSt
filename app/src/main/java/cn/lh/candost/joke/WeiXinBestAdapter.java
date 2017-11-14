package cn.lh.candost.joke;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lh.base.adapter.MyBaseViewHolder;

import java.util.List;

import cn.lh.candost.R;

/**
 * Created by liaohui on 2017/11/14.
 */

class WeiXinBestAdapter extends BaseQuickAdapter<WeiXinBestBean.ListBean, MyBaseViewHolder> {
    public WeiXinBestAdapter(@Nullable List<WeiXinBestBean.ListBean> data) {
        super(R.layout.item_weinxin_best, data);
    }

    @Override
    protected void convert(MyBaseViewHolder helper, WeiXinBestBean.ListBean item) {
        helper.setRoundImageUrl(R.id.image, item.firstImg, R.mipmap.ic_split_graph, 6)
                .setText(R.id.title, item.title)
                .setText(R.id.desc, item.source);
    }
}
