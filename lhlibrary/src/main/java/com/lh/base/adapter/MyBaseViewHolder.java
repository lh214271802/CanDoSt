package com.lh.base.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lh.R;
import com.lh.base.GlideUtils;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by liaohui on 2017/9/14.
 */

public class MyBaseViewHolder extends BaseViewHolder {
    protected Context mContext;

    public MyBaseViewHolder(View view) {
        super(view);
        mContext = view.getContext();
    }

    /**
     * 可复用布局使用Glide 唯一的要求是，对于任何可复用的 View 或 Target ，
     * 如果它们在之前的位置上，用 Glide 进行过加载操作，那么在新的位置上要去执行一个新的加载操作，或调用 clear() API 停止 Glide 的工作
     */
    public MyBaseViewHolder setImageUrl(int viewId, String imgUrl) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl).apply(GlideUtils.getNormalImageOptions().error(R.mipmap.ic_launcher).centerCrop()).into(view);
        return this;
    }

    public MyBaseViewHolder setImageUrl(int viewId, String imgUrl, int placeHolderRes) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl).apply(GlideUtils.getNormalImageOptions().error(R.mipmap.ic_launcher).placeholder(placeHolderRes).centerCrop()).into(view);
        return this;
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public MyBaseViewHolder loadIntoUseFitWidth(int viewId, String imageUrl, int placeHolderRes) {
        final ImageView imageView = getView(viewId);
        Glide.with(mContext)
                .load(imageUrl)
                .apply(GlideUtils.getNormalImageOptions()
                        .placeholder(placeHolderRes)
                        .error(R.mipmap.ic_launcher))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .into(imageView);
        return this;
    }

    public MyBaseViewHolder setCircleImageUrl(int viewId, String imgUrl, int placeHolderRes) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl).apply(GlideUtils.getNormalImageOptions().placeholder(placeHolderRes).circleCrop()).into(view);
        return this;
    }

    public MyBaseViewHolder setRoundImageUrl(int viewId, String imgUrl, int placeHolderRes) {
        return setRoundImageUrl(viewId, imgUrl, placeHolderRes, 4);
    }

    public MyBaseViewHolder setRoundImageUrl(int viewId, String imgUrl, int placeHolderRes, int radius) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl)
                .apply(GlideUtils.getNormalImageOptions().placeholder(placeHolderRes))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(view);
        return this;
    }

    public MyBaseViewHolder setSelected(int viewId, boolean isSelected) {
        View view = getView(viewId);
        view.setSelected(isSelected);
        return this;
    }

    public MyBaseViewHolder setEnable(int viewId, boolean isEnable) {
        View view = getView(viewId);
        view.setEnabled(isEnable);
        return this;
    }
}
