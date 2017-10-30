package com.weilaimoshu.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldoublem.loadingviewlib.LVCircularCD;
import com.weilaimoshu.R;


/**
 * Created by liaohui on 2017/9/8.
 */

public class CommonDialog extends Dialog {

    private static String loadText;
    private LVCircularCD fuckCD;
    private TextView loadingMsg;

    public CommonDialog(@NonNull Context context) {
        super(context);
    }

    public CommonDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CommonDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static CommonDialog getInstance(Context context, String loadingText) {
        LinearLayout v = (LinearLayout) View.inflate(context, R.layout.common_progress_view, null);
        CommonDialog dialog = new CommonDialog(context, R.style.loading_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(v,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );
        loadText = loadingText;
        return dialog;
    }

    @Override
    public void show() {
        super.show();
        loadingMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
        fuckCD = (LVCircularCD) this.findViewById(R.id.fuck_cd);
        fuckCD.setViewColor(Color.parseColor("#ff0000"));
        loadingMsg.setText(loadText);
        if (fuckCD != null) {
            fuckCD.startAnim();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (fuckCD != null) {
            fuckCD.stopAnim();
        }
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {//点击返回键时消失
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
