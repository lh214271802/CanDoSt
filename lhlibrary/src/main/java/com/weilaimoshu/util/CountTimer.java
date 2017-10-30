package com.weilaimoshu.util;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/2/27.
 */

public class CountTimer extends CountDownTimer {
    public static final int TIME_COUNT = 60000;// 时间防止从119s开始显示（以倒计时120s为例子）
    private int normalDrable, timingDrable;// 未计时的文字颜色，计时期间的文字颜色
    private TextView textView;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public CountTimer(TextView textView) {
        this(TIME_COUNT, 1000);
        this.textView = textView;
    }

    /**
     * @param textView     计时的View
     * @param normalDrable 未计时的时候背景色
     * @param timingDrable 计时的时候背景色
     */
    public CountTimer(TextView textView, int normalDrable, int timingDrable) {
        this(textView);
        this.normalDrable = normalDrable;
        this.timingDrable = timingDrable;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setText(millisUntilFinished / 1000 + "秒后可重发");
        textView.setEnabled(false);
        textView.setClickable(false);
        if (timingDrable != 0) {
            textView.setBackgroundResource(timingDrable);
        }
    }

    @Override
    public void onFinish() {
        textView.setEnabled(true);
        textView.setClickable(true);
        textView.setText("重新获取验证码");
        if (normalDrable != 0) {
            textView.setBackgroundResource(normalDrable);
        }
    }

}
