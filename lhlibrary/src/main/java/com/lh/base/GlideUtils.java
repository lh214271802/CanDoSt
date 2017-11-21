package com.lh.base;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by liaohui on 2017/11/21.
 */

public class GlideUtils {
    /*http://blog.csdn.net/u013005791/article/details/74532091#7-transitions动画*/
    public static RequestOptions getNormalImageOptions() {

        return new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop();
    }

}
