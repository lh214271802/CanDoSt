package com.lh.ui.common.video;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.lh.R;
import com.lh.base.adapter.MyBaseViewHolder;
import com.lh.ui.common.image.folder.ImageLoaderListener;

import java.util.List;


/**
 * Created by Administrator on 2017/3/9.
 */
public class VideoAdapter extends BaseMultiItemQuickAdapter<Video, MyBaseViewHolder> {
    private ImageLoaderListener loader;
    private boolean isSingleSelect;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public VideoAdapter(List<Video> data, ImageLoaderListener loader) {
        super(data);
        this.loader = loader;
        addItemType(Video.TYPE_ONE, R.layout.item_list_cam);
        addItemType(Video.TYPE_TH, R.layout.item_list_image);
    }


    public void setSingleSelect(boolean singleSelect) {
        isSingleSelect = singleSelect;
    }

    public void updateItem(int position) {
        if (getItemCount() > position) {
            notifyItemChanged(position);
        }
    }

    @Override
    protected void convert(MyBaseViewHolder helper, Video item) {
        switch (helper.getItemViewType()) {
            case Video.TYPE_ONE:
                helper.setImageResource(R.id.image_view, R.mipmap.ic_camera);
                break;
            case Video.TYPE_TH:
                if (item.getId() != 0) {
                    ImageView mImageView = helper.getView(R.id.iv_image);
                    ImageView mCheckView = helper.getView(R.id.cb_selected);
                    View mMaskView = helper.getView(R.id.lay_mask);
                    helper.setVisible(R.id.iv_is_gif, item.getPath().toLowerCase().endsWith("gif"));
                    mCheckView.setSelected(item.isSelect());
                    mMaskView.setVisibility(item.isSelect() ? View.VISIBLE : View.GONE);
                    loader.displayImage(mImageView, item.getPath());
                    mCheckView.setVisibility(isSingleSelect ? View.GONE : View.VISIBLE);
                }
                break;
        }
    }
}
