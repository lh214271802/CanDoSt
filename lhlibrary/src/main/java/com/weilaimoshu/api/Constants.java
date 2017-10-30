package com.weilaimoshu.api;

import com.weilaimoshu.FrameApplication;

import java.io.File;

/**
 * Created by liaohui on 2017/7/20.
 */

public class Constants {

    /**
     * 缓存地址
     */
    public static String PATH_CACHE = FrameApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "weilaimoshu";
    public static String APP_NAME = "weilaimoshu";

}
