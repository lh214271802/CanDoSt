package com.lh.api.dowload;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.lh.R;
import com.lh.util.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import rx.Subscriber;


/**
 * https://github.com/a1018875550/retrofit-download-file
 */
public class DownloadService extends IntentService {
    public static String APP_APK_PATH = "APP_APK_PATH";

    private static String APKURL = "APK_URL";
    private static String VERSION_CODE = "VERSION_CODE";
    private static String SERVICE_TITLE = "SERVICE_TITLE";
    private static String SERVICE_TEXT = "SERVICE_TEXT";

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;


    private String apkUrl = "";//下载链接
    private String versionCode = "";//下载apk最新版本号
    private String serviceTitle = "";//通知栏大标题
    private String serviceText = "";//通知栏小标题
    private File outputFile;//下载完成的文件名
    private int downloadCount = 0;//用于减少通知的次数，以免界面卡顿
    private long currentProgress;//用于减少通知的次数，以免界面卡顿
    private boolean downLoadSuccess = false;//下载是否成功

    public static void startService(Context context, String apkUrl, String versionCode, String contentTitle, String contentText) {
        context.startService(new Intent(context, DownloadService.class)
                .putExtra(APKURL, apkUrl)
                .putExtra(VERSION_CODE, versionCode)
                .putExtra(SERVICE_TITLE, contentTitle)
                .putExtra(SERVICE_TEXT, contentText));
    }

    public DownloadService() {
        super("download");//used for debug
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        apkUrl = intent.getStringExtra(APKURL);
        versionCode = intent.getStringExtra(VERSION_CODE);
        serviceTitle = intent.getStringExtra(SERVICE_TITLE);
        serviceText = intent.getStringExtra(SERVICE_TEXT);
        //下载的文件名
        outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS),
                "mixiupet" + versionCode + ".apk");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(serviceTitle)
                .setContentText(serviceText)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());

        download();
    }

    /**
     * 开始下载
     */
    private void download() {

        if (FileUtils.isFileExists(outputFile) && !TextUtils.isEmpty(SharedPreferencesUtil.getInstance().getString(APP_APK_PATH))) {
            LogUtils.e("version-----------> install");
            AppUtils.installApp(outputFile, "com.weilaimoshu.provider");
            downLoadSuccess = true;
            downloadCompleted();
            return;
        } else {
            DownloadProgressListener listener = new DownloadProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    //不频繁发送通知，防止通知栏下拉卡顿
                    long progress = (bytesRead * 100) / contentLength;
                    LogUtils.e("version----------->" + progress);
                    if (((downloadCount == 0) || progress > downloadCount) && progress % 2 == 0 && currentProgress != progress) {
                        currentProgress = progress;
                        DownloadBean download = new DownloadBean();
                        download.setTotalFileSize(contentLength);
                        download.setCurrentFileSize(bytesRead);
                        download.setProgress((int) (progress));

                        sendNotification(download);
                    }
                }
            };
            if (FileUtils.createFileByDeleteOldFile(outputFile)) {
                new DownloadAPI(listener).downloadAPK(apkUrl, outputFile, new Subscriber() {
                    @Override
                    public void onCompleted() {
                        downLoadSuccess = true;
                        downloadCompleted();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        downLoadSuccess = false;
                        downloadCompleted();
                        LogUtils.e(e);
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
            }
        }
    }

    /**
     * 下载完成
     */
    private void downloadCompleted() {
        DownloadBean downloadBean = new DownloadBean();
        downloadBean.setProgress(100);
        sendIntent(downloadBean);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle(serviceTitle);
        notificationBuilder.setContentText(downLoadSuccess ? "下载完成" : "下载失败");
        if (downLoadSuccess) {
            //点击通知之后需要跳转的页面
            Intent resultIntent = new Intent(this, DownloadService.class);
//            resultIntent.putExtra(Constants.APP_APK_PATH, outputFile);
/*            //使用TaskStackBuilder为“通知页面”设置返回关系
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            //为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);*/
            LogUtils.e("version --------->success");
            Uri localUri = Uri.fromFile(outputFile);
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            DownloadService.this.sendBroadcast(localIntent);//提示本机此文件已存在
            PendingIntent pIntent = PendingIntent.getService(DownloadService.this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pIntent);
            SharedPreferencesUtil.getInstance().putString(APP_APK_PATH, outputFile.getAbsolutePath());
            //TODO FUCK
            AppUtils.installApp(outputFile, "com.weilaimoshu.provider");

        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    /**
     * 下载中，通知栏变化
     *
     * @param downloadBean
     */
    private void sendNotification(DownloadBean downloadBean) {

        sendIntent(downloadBean);
        notificationBuilder.setProgress(100, downloadBean.getProgress(), false);
        notificationBuilder.setContentText(
                SizeFormatUtils.getDataSize(downloadBean.getCurrentFileSize()) + "/" +
                        SizeFormatUtils.getDataSize(downloadBean.getTotalFileSize()));
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(DownloadBean downloadBean) {
        EventBus.getDefault().post(downloadBean);//下载进度发送
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

}