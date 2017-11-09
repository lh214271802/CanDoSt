package com.lh.ui.common.image.folder;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lh.R;
import com.lh.base.adapter.MyBaseViewHolder;
import com.lh.ui.common.image.bean.ImageFolder;

import java.util.List;


/**
 * Created by huanghaibin_dev
 * on 2016/7/13.
 * <p>
 * Changed by qiujuer
 * on 2016/09/01
 */

public class ImageFolderAdapter extends BaseQuickAdapter<ImageFolder, MyBaseViewHolder> {
    private ImageLoaderListener loader;

    public ImageFolderAdapter(ImageLoaderListener loader) {
        super(R.layout.item_list_folder);
        this.loader = loader;
    }


    public void setLoader(ImageLoaderListener loader) {
        this.loader = loader;
    }

    public final void resetItem(List<ImageFolder> items) {
        if (items != null) {
            setNewData(null);
            addData(items);
        }
    }

    @Override
    protected void convert(MyBaseViewHolder helper, ImageFolder item) {
        helper.setText(R.id.tv_folder_name, item.getName())
                .setText(R.id.tv_size, item.getName());
        ImageView iv_image = helper.getView(R.id.iv_folder);
        if (loader != null) {
            loader.displayImage(iv_image, item.getAlbumPath());
        }
    }
}
