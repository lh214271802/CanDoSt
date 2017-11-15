package cn.lh.candost.ui.taoke;

import java.util.List;

/**
 * Created by liaohui on 2017/11/15.
 */

public class TaoKeBean {

    public int code;
    public String msg;
    public String api_version;
    public String api_name;
    public int total;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * goods_id : 558815639113
         * goods_pic : https://img.alicdn.com/imgextra/i2/1984359503/TB16SEsdMn.PuJjSZFkXXc_lpXa_!!0-item_pic.jpg
         * goods_long_pic : http://acdn.taokezhushou.com/taokezhushou-long-pic/75d407364d304cb96c3ae05f6027354c.jpg
         * goods_title : 安全裤2条装防走光女士内裤打底裤短裤冰丝无痕夏薄款保险安全裤
         * goods_short_title : 女士内裤打底裤短裤冰丝2条
         * goods_intro : 2条装！宽松舒适，性感可爱，优质亲肤面料，弹性好，不紧绷，贴身无痕，纯色设计，使安全裤时尚又不单调。
         * goods_cate_id : 1
         * goods_price : 29.90
         * goods_sale_num : 244
         * commission_rate : 20.5
         * seller_id : 1984359503
         * coupon_id : 3e35922b5ba5463694db5318c23db8ea
         * coupon_apply_amount : 29.00
         * coupon_amount : 5.00
         * coupon_start_time : 2017-11-09 00:00:00
         * coupon_end_time : 2017-11-16 23:59:59
         * is_tmall : 1
         * juhuasuan : 0
         * taoqianggou : 0
         * yunfeixian : 1
         * jinpai : 0
         * jiyoujia : 0
         * haitao : 0
         * dsr : 4.8
         */

        public String goods_id;
        public String goods_pic;
        public String goods_long_pic;
        public String goods_title;
        public String goods_short_title;
        public String goods_intro;
        public int goods_cate_id;
        public String goods_price;
        public int goods_sale_num;
        public double commission_rate;
        public String seller_id;
        public String coupon_id;
        public String coupon_apply_amount;
        public String coupon_amount;
        public String coupon_start_time;
        public String coupon_end_time;
        public int is_tmall;
        public int juhuasuan;
        public int taoqianggou;
        public int yunfeixian;
        public int jinpai;
        public int jiyoujia;
        public int haitao;
        public double dsr;
    }
}
