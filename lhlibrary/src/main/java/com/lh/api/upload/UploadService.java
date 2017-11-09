package com.lh.api.upload;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lh.R;
import com.lh.api.AppApi;
import com.lh.api.dowload.DownloadBean;
import com.lh.api.dowload.DownloadService;
import com.lh.api.dowload.SizeFormatUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liaohui on 2017/11/9.
 */

public class UploadService extends IntentService {

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;


    private static String upUrl = "";//上传链接
    private static String serviceTitle = "";//通知栏大标题
    private static String serviceText = "";//通知栏小标题
    private static Map<String, String> upParams;//上传的参数
    private static String upFile;//上传的文件
    private Subscription subscription;

    public UploadService(String name) {
        super("upload");
    }


    public static void startService(Context context, String uploadUrl, String contentTitle, String contentText, String filePath, Map<String, String> uploadPara) {
        if (TextUtils.isEmpty(filePath) || !FileUtils.isFileExists(filePath)) {
            ToastUtils.showShort("文件不存在！");
            return;
        }
        context.startService(new Intent(context, DownloadService.class));
        serviceTitle = contentTitle;
        serviceText = contentText;
        upUrl = uploadUrl;
        upParams = uploadPara;
        upFile = filePath;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(serviceTitle)
                .setContentText(serviceText)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
        upload();
    }

    private void sendIntent(UploadBean downloadBean) {
        EventBus.getDefault().post(downloadBean);//下载进度发送
    }

    private void upload() {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(new File(upFile), new ProgressListener() {
            @Override
            public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
                sendIntent(new UploadBean(hasWrittenLen, totalLen, hasFinish));
                notificationBuilder.setProgress(100, (int)(hasWrittenLen / totalLen), false);
                notificationBuilder.setContentText(
                        SizeFormatUtils.getDataSize(hasWrittenLen) + "/" +
                                SizeFormatUtils.getDataSize(totalLen));
                notificationManager.notify(0, notificationBuilder.build());
            }
        });
        requestBodyMap.put("file\"; filename=\"" + upFile.substring(upFile.lastIndexOf(File.separator)), fileRequestBody);
        subscription = AppApi.getApiService().uploadFileInfo(upParams, requestBodyMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e);
                    }

                    @Override
                    public void onNext(ResponseBody s) {
                        try {
                            LogUtils.e("---the next string is --" + s.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}
