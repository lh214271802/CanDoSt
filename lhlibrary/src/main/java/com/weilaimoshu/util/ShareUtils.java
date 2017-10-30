package com.weilaimoshu.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.weilaimoshu.api.Constants;

import java.io.File;
import java.util.concurrent.ExecutionException;


/**
 * Created by liaohui on 2017/7/26.
 */

public class ShareUtils {
//    private OnekeyShare onekeyShare;

    /**
     * 分享的内容的点击链接
     */
    private String downLoadUrl = "";
    /**
     * 分享的内容的Title
     */
    private String shareTitle = "";
    /**
     * 分享的文本内容内容
     */
    private String shareDescription = "";
    /**
     * 分享的图片
     */
    private String shareImageUrl = "";

    public void showShare(final Context mContext, String resourcesId, int type) {
  /*      NetClient.getInstance().getShareUrl(resourcesId, type, new ResponseListener() {

            @Override
            public void onFaile(String str) {
                ToastUtils.showShort(str);
            }

            @Override
            public void onSuccess(Object object) {
                GetShareUrlResponse response = (GetShareUrlResponse) object;
                showShareDialog(mContext, response);
            }

            @Override
            public void onReLogin() {

            }
        });*/
    }
/*

    public void showShareDialog(Context mContext, GetShareUrlResponse response) {
        GetShareUrlResponse.DataBean data = response.getData();
        shareDescription = data.getDescribe();
        shareImageUrl = data.getHead_img();
        shareTitle = data.getTitle();
        downLoadUrl = data.getUrl();
        showShare(mContext);
    }*/

    private Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    private final void showShare(final Context mContext) {
/*        if (onekeyShare == null) {
            onekeyShare = new OnekeyShare();
            //添加更多分享平台的按钮
            final Bitmap bitmap = getBitmapFromDrawable(mContext, R.drawable.ic_more_select);
            onekeyShare.setCustomerLogo(bitmap, "更多分享", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    defaultShareText(mContext, shareTitle, shareDescription, downLoadUrl);
//                defaultShareImage(mContext, shareImageUrl, 0);
                }
            });
        }
       *//* onekeyShare.setSilent(false);*//*
        //关闭sso授权
        onekeyShare.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        onekeyShare.setTitle(shareTitle);
        *//** titleUrl是标题的网络链接，仅在人人网和QQ使用，否则可以不提供 *//*
        onekeyShare.setTitleUrl(downLoadUrl);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //onekeyShare.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //分享网络图片，微信好友,QQ空间，新浪微博支持
        onekeyShare.setImageUrl(shareImageUrl);
//              onekeyShare.setVideoUrl("http://player.letvcdn.com/lc07_p/201610/12/15/15/15/newplayer/LetvPlayer.swf?newlist=1&showdock=0");//设置视频地址
        onekeyShare.setUrl(downLoadUrl); //微信不绕过审核分享链接
//              onekeyShare.setMusicUrl("http://192.168.0.115/wap/1.mp3");//微信分享的音乐地址
        onekeyShare.setComment("小伙伴们,来和我一起书写未来魔书吧！"); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
        onekeyShare.setSite("未来魔书");  //仅限QZone分享完之后返回应用时提示框上显示的名称
        onekeyShare.setSiteUrl(downLoadUrl);//仅限QZone分享参数
        onekeyShare.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                // text是分享文本，所有平台都需要这个字段
                if (Wechat.NAME.equals(platform.getName()) ||
                        WechatMoments.NAME.equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }

                if (QQ.NAME.equals(platform.getName())) {
                    paramsToShare.setTitleUrl(downLoadUrl);
                }
                if (SinaWeibo.NAME.equals(platform.getName())) {
                    paramsToShare.setText(ShareUtils.this.shareDescription + "\n" + downLoadUrl);
                } else {
                    paramsToShare.setText(ShareUtils.this.shareDescription);
                }
            }
        });

     *//*   // 将快捷分享的操作结果将通过OneKeyShareCallback回调
        onekeyShare.setCallback(new PlatformActionListener() {

            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });*//*
        // 启动分享GUI
        onekeyShare.show(mContext);*/
    }

    /**
     * 调用系统自带的分享，分享链接和文字
     */
    public static void defaultShareText(Context context, final String title, final String shareDescription, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享 " + title);
        intent.putExtra(Intent.EXTRA_TEXT, shareDescription + " " + url + "\n" + "分享来自——未来魔书");
        context.startActivity(Intent.createChooser(intent, "选择分享"));
    }

    /**
     * 分享图片，图片为网络图片
     *
     * @param scene 0表示无平台，1为微信朋友，2为微信朋友圈,3为QQ
     */
    public static void defaultShareImage(final Context context, final String shareImageUrl, final int scene) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = Glide.with(context)
                            .load(shareImageUrl.trim())
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    final String extDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + File.separator + Constants.APP_NAME + File.separator + System.currentTimeMillis() + ".jpg";
                    if (ImageUtils.save(bitmap, extDir, Bitmap.CompressFormat.JPEG, false)) {//保存成功
                        Uri broadUri = Uri.fromFile(new File(extDir));
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, broadUri));
                        defaultShareImage(extDir, context, scene);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 分享图片，图片为本地图片
     *
     * @param scene 0表示无平台，1为微信朋友，2为微信朋友圈,3为QQ
     */
    public static void defaultShareImage(String shareImageUrl, Context context, int scene) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            Uri uri = FileProvider.getUriForFile(context, "com.weilaimoshu.provider", new File(shareImageUrl));
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //可以选择定向分享到的平台
            if (scene != 0) {
                shareIntent.setClassName(scene == 1 || scene == 2 ? "com.tencent.mm" : scene == 3 ? "com.tencent.mobileqq" : "", scene == 1 ?
                        "com.tencent.mm.ui.tools.ShareToTimeLineUI"
                        : scene == 2 ? "com.tencent.mm.ui.tools.ShareImgUI" : scene == 3 ? "com.tencent.mobileqq.activity.JumpActivity" : "");
                //或者如下:
                /*ComponentName componentName = new ComponentName(scene == 1 || scene == 2 ? "com.tencent.mm" : scene == 3 ? "com.tencent.mobileqq" : "",  scene == 1 ?
                        "com.tencent.mm.ui.tools.ShareToTimeLineUI"
                        : scene == 2 ? "com.tencent.mm.ui.tools.ShareImgUI" : scene == 3 ? "com.tencent.mobileqq.activity.JumpActivity":"");
                shareIntent.setComponent(componentName);*/
            }
            context.startActivity(Intent.createChooser(shareIntent, "分享图片"));
        } catch (Exception e) {
            e.printStackTrace();
            if (scene == 1 || scene == 2) {
                ToastUtils.showShort("请安装微信");
            } else if (scene == 3) {
                ToastUtils.showShort("请安装QQ");
            }
        }
    }
}
