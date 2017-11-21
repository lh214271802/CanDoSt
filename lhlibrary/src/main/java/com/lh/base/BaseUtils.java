package com.lh.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.lh.R;
import com.lh.ui.bean.UserInfoBean;
import com.lh.util.SharedPreferencesUtil;

import java.io.UnsupportedEncodingException;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


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
    public static void clearLoginInfo(Context context, boolean toLogin) {
        SharedPreferencesUtil.getInstance().remove("UserInfo");
        if (toLogin) {//TODO tologin
//            context.startActivity(new Intent(context,LoginAcitivity.class));
        }
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
    public static void setViewImageWithHandle(final Context mContext, String url, ImageViewTarget<Drawable> target) {
        Glide.with(mContext)
                .load(url).apply(GlideUtils.getNormalImageOptions()
                .placeholder(R.mipmap.ic_launcher)
                .fallback(R.mipmap.ic_launcher)
                .fitCenter())
                .into(target);
    }

    public static void setViewImage(final Context mContext, ImageView imageView, String url) {
        setViewImage(mContext, imageView, url, R.mipmap.ic_launcher, R.mipmap.ic_launcher, false);
    }

    @SuppressLint("CheckResult")
    public static void setViewImage(final Context mContext, ImageView imageView, String url, int placeHolder, int errorHolder, boolean isCircle) {
        RequestOptions requestOptions = GlideUtils.getNormalImageOptions();
        RequestBuilder<Drawable> drawableRequestBuilder = Glide.with(mContext)
                .load(url);
        if (placeHolder != 0) {
            requestOptions.placeholder(placeHolder);
        }
        if (errorHolder != 0) {
            requestOptions.error(errorHolder);
        }
        if (isCircle) {
            requestOptions.circleCrop();
        }
        drawableRequestBuilder
                .apply(requestOptions)
                .into(imageView);
    }

    public static void setRoundImage(final Context mContext, ImageView imageView, String url) {
        setRoundImage(mContext, imageView, url, R.mipmap.ic_launcher, R.mipmap.ic_launcher, 4);
    }

    @SuppressLint("CheckResult")
    public static void setRoundImage(final Context mContext, ImageView imageView, String url, int placeHolder, int errorHolder, int radius) {
        RequestBuilder<Drawable> drawableRequestBuilder = Glide.with(mContext)
                .load(url);
        RequestOptions requestOptions = GlideUtils.getNormalImageOptions();
        if (placeHolder != 0) {
            requestOptions.placeholder(placeHolder);
        }

        if (errorHolder != 0) {
            requestOptions.error(errorHolder);
        }
        requestOptions.transform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL));
        drawableRequestBuilder
                .apply(requestOptions)
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
