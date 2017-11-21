package com.lh;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lh.api.Constants;
import com.lh.util.SharedPreferencesUtil;
import com.lh.util.push.RomUtils;
import com.peng.one.push.OnePush;
import com.peng.one.push.core.OnOnePushRegisterListener;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by liaohui on 2017/7/19.
 */

public class FrameApplication extends Application {

    private static FrameApplication instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //因为引用的包过多，实现多包问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        Utils.init(this);
        CrashUtils.init();
        /*----------友盟统计--------------*/
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(true);
        initUMengPush();
        SharedPreferencesUtil.init(instance, Constants.APP_NAME, MODE_PRIVATE);
    }

    /**
     * 初始化推送
     */
    private void initUMengPush() {
        //初始化的时候，回调该方法，可以根据platformCode和当前系统的类型，进行注册
        //返回true，则使用该平台的推送，否者就不使用
        //只在主进程中注册(注意：umeng推送，除了在主进程中注册，还需要在channel中注册)
        String currentProcessName = getCurrentProcessName();
        if (BuildConfig.APPLICATION_ID.equals(currentProcessName) || BuildConfig.APPLICATION_ID.concat(":channel").equals(currentProcessName)) {
            //platformCode和platformName就是在<meta/>标签中，对应的"平台标识码"和平台名称
            OnePush.init(this, new OnOnePushRegisterListener() {
                @Override
                public boolean onRegisterPush(int platformCode, String platformName) {
                    boolean result = false;
                    if (RomUtils.isMiuiRom()) {
                        result = platformCode == 101;
                    } else if (RomUtils.isHuaweiRom()) {
                        result = platformCode == 102;
                    } else if (RomUtils.isFlymeRom()) {
                        result = platformCode == 105;
                    } else {
                        result = (platformCode == 104 || platformCode == 103);
                    }
                    LogUtils.e("fuck platformName " + platformName + "---platformCode " + platformCode);
                    return result;
                }
            });
            OnePush.register();
        }
    }

    /**
     * 获取当前进程名称
     *
     * @return processName
     */
    public String getCurrentProcessName() {
        int currentProcessId = Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == currentProcessId) {
                return runningAppProcess.processName;
            }
        }
        return null;
    }


    public static FrameApplication getInstance() {
        return instance;
    }
}
