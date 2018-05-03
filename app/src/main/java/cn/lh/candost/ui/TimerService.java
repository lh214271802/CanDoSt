package cn.lh.candost.ui;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.lh.candost.R;

/**
 * @author liaohui
 * @date 2018/5/3
 */
public class TimerService extends Service {


    private Timer timer;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateViews();
            }
        }, 0, 100);
    }

    private void updateViews() {
        String time = sdf.format(new Date());
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_desktop_widget);
        remoteViews.setTextViewText(R.id.tv_time, time);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName c = new ComponentName(getApplicationContext(),WidgetProvider.class);
        appWidgetManager.updateAppWidget(c,remoteViews);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
