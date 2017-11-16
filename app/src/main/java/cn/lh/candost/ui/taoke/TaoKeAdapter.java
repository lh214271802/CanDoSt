package cn.lh.candost.ui.taoke;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lh.base.adapter.MyBaseViewHolder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import cn.lh.candost.R;

/**
 * Created by liaohui on 2017/11/15.
 */

class TaoKeAdapter extends BaseQuickAdapter<TaoKeBean.DataBean, MyBaseViewHolder> {

    public TaoKeAdapter(@Nullable List<TaoKeBean.DataBean> data) {
        super(R.layout.item_taoke, data);
    }

    @Override
    protected void convert(MyBaseViewHolder helper, TaoKeBean.DataBean item) {
        double productRealPrice = Double.parseDouble(item.goods_price) > Double.parseDouble(item.coupon_apply_amount)
                ? (Double.parseDouble(item.goods_price) - Double.parseDouble(item.coupon_amount))
                : Double.parseDouble(item.goods_price);
        String realPrice = new DecimalFormat("#.0").format(productRealPrice);
        helper.setRoundImageUrl(R.id.product_image, item.goods_pic, R.mipmap.ic_product, 15)
                .setText(R.id.product_commission, new SpanUtils()
                        .append("" + (NumberFormat.getPercentInstance().format(item.commission_rate / Double.parseDouble(item.goods_price))))
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.price_red))
                        .append("\n佣金(￥" + item.commission_rate + ")")
                        .create())
                .setText(R.id.product_title, item.goods_short_title)
                .setText(R.id.product_coupon, "券" + item.coupon_amount)
                .setText(R.id.product_price, new SpanUtils().append("￥" + realPrice)
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.price_red))
                        .append("\n券后价")
                        .create())
                .setText(R.id.product_old_price, "原价：" + item.goods_price)
                .setText(R.id.product_desc, "总销量/" + item.goods_sale_num)
                .setText(R.id.product_dsr, new SpanUtils().append(item.dsr + "")
                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.price_red))
                        .append("\n评分")
                        .create());
    }
}
