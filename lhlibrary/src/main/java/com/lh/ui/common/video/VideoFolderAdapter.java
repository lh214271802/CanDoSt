package com.lh.ui.common.video;


import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lh.R;
import com.lh.base.adapter.MyBaseViewHolder;
import com.lh.ui.common.image.folder.ImageLoaderListener;

import java.util.List;

/**
 * Created by huanghaibin_dev
 * on 2016/7/13.
 * <p>
 * Changed by qiujuer
 * on 2016/09/01
 */

public class VideoFolderAdapter extends BaseQuickAdapter<VideoFolder, MyBaseViewHolder> {
    private ImageLoaderListener loader;

    public VideoFolderAdapter(@LayoutRes int layoutResId, ImageLoaderListener loader) {
        super(layoutResId);
        this.loader = loader;
    }

    public final void resetItem(List<VideoFolder> items) {
        if (items != null) {
            setNewData(null);
            addData(items);
        }
    }

    @Override
    protected void convert(MyBaseViewHolder helper, VideoFolder item) {
        helper.setText(R.id.tv_folder_name, item.getName())
                .setText(R.id.tv_size, item.getName());
        ImageView iv_Video = helper.getView(R.id.iv_folder);
        if (loader != null) {
            loader.displayImage(iv_Video, item.getAlbumPath());
        }
    }
}
