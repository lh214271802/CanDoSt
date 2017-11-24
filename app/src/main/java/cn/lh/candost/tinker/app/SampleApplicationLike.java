package cn.lh.candost.tinker.app;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lh.FrameApplication;
import com.lh.api.Constants;
import com.lh.util.SharedPreferencesUtil;
import com.lh.util.push.RomUtils;
import com.peng.one.push.OnePush;
import com.peng.one.push.core.OnOnePushRegisterListener;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.lh.candost.BuildConfig;
import cn.lh.candost.tinker.Log.MyLogImp;
import cn.lh.candost.tinker.util.SampleApplicationContext;
import cn.lh.candost.tinker.util.TinkerManager;

/**
 * because you can not use any other class in your application, we need to
 * move your implement of Application to {@link ApplicationLifeCycle}
 * As Application, all its direct reference class should be in the main dex.
 * <p>
 * We use tinker-android-anno to make sure all your classes can be patched.
 * <p>
 * application: if it is start with '.', we will add SampleApplicationLifeCycle's package name
 * <p>
 * flags:
 * TINKER_ENABLE_ALL: support dex, lib and resource
 * TINKER_DEX_MASK: just support dex
 * TINKER_NATIVE_LIBRARY_MASK: just support lib
 * TINKER_RESOURCE_MASK: just support resource
 * <p>
 * loaderClass: define the tinker loader class, we can just use the default TinkerLoader
 * <p>
 * loadVerifyFlag: whether check files' md5 on the load time, defualt it is false.
 * <p>
 * Created by zhangshaowen on 16/3/17.
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "cn.lh.candost.ui.MyApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class SampleApplicationLike extends DefaultApplicationLike {
    private static final String TAG = "Tinker.SampleApplicationLike";
    public static SampleApplicationLike sampleApplication;

    public SampleApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                                 long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!其原理是分包架构，所以在加载初要加载其余的分包
        MultiDex.install(base);
        SampleApplicationContext.application = getApplication();
        SampleApplicationContext.context = getApplication();
        // Tinker管理类，保存当前对象
        TinkerManager.setTinkerApplicationLike(this);
        //崩溃保护
        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed,是否重试
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log,Log 实现，打印加载补丁的信息
        TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex,运行Tinker ，通过Tinker添加一些基本配置
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        // 生命周期，默认配置
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sampleApplication = this;
        //将我们自己的MyApplication中的所有逻辑放在这里，例如初始化一些第三方
        Utils.init(getApplication());
        CrashUtils.init();
        /*----------友盟统计--------------*/
        MobclickAgent.setScenarioType(getApplication(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(true);
        initUMengPush();
        SharedPreferencesUtil.init(getApplication(), Constants.APP_NAME, getApplication().MODE_PRIVATE);

    }

    /**
     * 获取SampleApplication实例
     *
     * @return
     */
    public static SampleApplicationLike getSampleApplication() {
        return sampleApplication;
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
            OnePush.init(getApplication(), new OnOnePushRegisterListener() {
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
//                    return result;
                    return true;
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
        ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == currentProcessId) {
                return runningAppProcess.processName;
            }
        }
        return null;
    }
}