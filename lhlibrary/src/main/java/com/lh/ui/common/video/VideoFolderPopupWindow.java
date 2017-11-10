package com.lh.ui.common.video;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lh.R;


/**
 * 视频选择器菜单选择界面
 */
public class VideoFolderPopupWindow extends PopupWindow implements
        View.OnAttachStateChangeListener, BaseQuickAdapter.OnItemClickListener {
    private VideoFolderAdapter mAdapter;
    private RecyclerView mFolderView;
    private Callback mCallback;

    public VideoFolderPopupWindow(Context context, Callback callback) {
        super(LayoutInflater.from(context).inflate(R.layout.popup_window_folder, null),
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mCallback = callback;

        // init
        setAnimationStyle(R.style.popup_anim_style_alpha);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setFocusable(true);

        // content
        View content = getContentView();
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        content.addOnAttachStateChangeListener(this);

        mFolderView = (RecyclerView) content.findViewById(R.id.rv_popup_folder);
        mFolderView.setLayoutManager(new LinearLayoutManager(context));

    }

    public void setAdapter(VideoFolderAdapter adapter) {
        this.mAdapter = adapter;
        mFolderView.setAdapter(adapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        final Callback callback = mCallback;
        if (callback != null)
            callback.onShow();
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        final Callback callback = mCallback;
        if (callback != null)
            callback.onDismiss();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        final Callback callback = mCallback;
        if (callback != null)
            callback.onSelect(this, mAdapter.getItem(position));
    }

    public interface Callback {
        void onSelect(VideoFolderPopupWindow popupWindow, VideoFolder model);

        void onDismiss();

        void onShow();
    }
}
