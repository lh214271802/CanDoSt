package com.lh.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lh.R;
import com.lh.glide.CropCircleTransformation;
import com.lh.glide.RoundedCornersTransformation;
import com.lh.ui.bean.UserInfoBean;
import com.lh.util.SharedPreferencesUtil;

import java.io.UnsupportedEncodingException;


/**
 * Created by liaohui on 2017/9/12.
 */

public class BaseUtils {
    /**
     * 去登录
     */
    public static void goLogin() {
    }

    /**
     * 清除用户信息
     */
    public static void clearLoginInfo(boolean toLogin) {
    }

    /**
     * 得到用户信息
     */

    public static UserInfoBean getUserInfo() {
        return SharedPreferencesUtil.getInstance().getObject("UserInfo", UserInfoBean.class);
    }

    public static boolean isUserLogin() {
        return getUserInfo() != null;
    }

    /**
     * 保存用户信息
     */

    public static void setUserInfo(UserInfoBean userInfoBean) {
        SharedPreferencesUtil.getInstance().putObject("UserInfo", userInfoBean);
    }

    /**
     * 加载图片的时候同时发生其他的操作,需要重写GlideDrawableImageViewTarget中的onLoadStarted、onLoadFailed、onResourceReady三个方法
     */
    public static void setViewImageWithHandle(final Context mContext, String url, GlideDrawableImageViewTarget target) {
        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(R.anim.start_fullscreen)
                .placeholder(R.mipmap.ic_launcher)
                .fitCenter()
                .into(target);
    }

    public static void setViewImage(final Context mContext, ImageView imageView, String url) {
        setViewImage(mContext, imageView, url, R.mipmap.ic_launcher, R.mipmap.ic_launcher, false);
    }

    public static void setViewImage(final Context mContext, ImageView imageView, String url, int placeHolder, int errorHolder, boolean isCircle) {
        BitmapRequestBuilder<String, Bitmap> drawableRequestBuilder = Glide.with(mContext)
                .load(url).asBitmap();
        if (placeHolder != 0) {
            drawableRequestBuilder.placeholder(placeHolder);
        }

        if (errorHolder != 0) {
            drawableRequestBuilder.error(errorHolder);
        }
        if (isCircle) {
            drawableRequestBuilder.transform(new CenterCrop(mContext), new CropCircleTransformation(mContext));
        }
        drawableRequestBuilder
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void setRoundImage(final Context mContext, ImageView imageView, String url) {
        setRoundImage(mContext, imageView, url, R.mipmap.ic_launcher, R.mipmap.ic_launcher, 4);
    }

    public static void setRoundImage(final Context mContext, ImageView imageView, String url, int placeHolder, int errorHolder, int radius) {
        BitmapRequestBuilder<String, Bitmap> drawableRequestBuilder = Glide.with(mContext)
                .load(url).asBitmap();
        if (placeHolder != 0) {
            drawableRequestBuilder.placeholder(placeHolder);
        }

        if (errorHolder != 0) {
            drawableRequestBuilder.error(errorHolder);
        }
        drawableRequestBuilder.transform(new RoundedCornersTransformation(mContext,radius,0, RoundedCornersTransformation.CornerType.ALL));
        drawableRequestBuilder
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static String getViewText(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static String encodePwd(String passsword, String mobile) {
        String pwd = null;
        try {
            pwd = android.util.Base64.encodeToString((android.util.Base64.encodeToString(passsword.getBytes("UTF-8"), Base64.NO_WRAP) + mobile).getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return pwd;
    }
}
