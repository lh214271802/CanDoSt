package com.lh.view.popwindow;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by liaohui on 2017/12/26.
 */

public class CommonPopupWindow extends PopupWindow {

    private CommonPopupWindow() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    private CommonPopupWindow(View view, int width, int height) {
        super(view, width, height);
    }

    /**
     * 获取建造者
     *
     * @return {@link Builder}
     */
    public static Builder getBuilder(Activity activity, @LayoutRes int layoutId, CallBack callBack) {
        return new Builder(activity, layoutId, callBack);
    }

    public static class Builder {
        private CallBack callBack;
        private CommonPopupWindow popupWindow;
        private Activity activity;
        private View contentView;

        public Builder(Activity activity, @LayoutRes int layoutId, CallBack callBack) {
            this.activity = activity;
            popupWindow = new CommonPopupWindow(LayoutInflater.from(activity).inflate(layoutId, null),
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            // content
            contentView = popupWindow.getContentView();
            this.callBack = callBack;
        }

        public Builder setAnimationStyle(int animationStyle) {
            if (animationStyle != 0) {
                popupWindow.setAnimationStyle(animationStyle);
            }
            return this;
        }

        public Builder setViewOnclickListener(int... viewIds) {
            if (viewIds != null) {
                for (int i = 0; i < viewIds.length; i++) {
                    contentView.findViewById(viewIds[i]).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (callBack != null) {
                                callBack.onViewClick(v.getId());
                            }
                        }
                    });
                }
            }
            return this;
        }

        public Builder setText(@IdRes int viewId, CharSequence charSequence) {
            TextView textView = contentView.findViewById(viewId);
            textView.setText(charSequence);
            return this;
        }

        private CommonPopupWindow build() {
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            contentView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    if (activity != null) {
                        //设置窗口背景色
                        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                        lp.alpha = 0.6f;
                        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        activity.getWindow().setAttributes(lp);
                    }
                    if (callBack != null) {
                        callBack.onShow();
                    }
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    if (activity != null) {
                        //设置窗口背景色
                        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                        lp.alpha = 1f;
                        //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        activity.getWindow().setAttributes(lp);
                    }
                    if (callBack != null) {
                        callBack.onDismiss();
                    }
                }
            });
            return popupWindow;
        }
    }

    public interface CallBack {
        void onDismiss();

        void onShow();

        void onViewClick(int viewId);
    }
}
