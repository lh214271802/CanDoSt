package com.weilaimoshu.ui.common.image.folder;


import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.weilaimoshu.R;
import com.weilaimoshu.base.adapter.MyBaseViewHolder;
import com.weilaimoshu.ui.common.image.bean.Image;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */
public class ImageAdapter extends BaseMultiItemQuickAdapter<Image,MyBaseViewHolder> {
    private ImageLoaderListener loader;
    private boolean isSingleSelect;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ImageAdapter(List<Image> data, ImageLoaderListener loader) {
        super(data);
        this.loader = loader;
        addItemType(Image.TYPE_ONE,R.layout.item_list_cam);
        addItemType(Image.TYPE_TH,R.layout.item_list_image);
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
    protected void convert(MyBaseViewHolder helper, Image item) {
        switch (helper.getItemViewType()) {
            case Image.TYPE_ONE:
                helper.setImageResource(R.id.image_view,R.drawable.ic_list_take_photo);
                break;
            case Image.TYPE_TH:
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
