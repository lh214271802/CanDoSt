package com.weilaimoshu.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weilaimoshu.R;
import com.weilaimoshu.glide.CropCircleTransformation;
import com.weilaimoshu.glide.RoundedCornersTransformation;

/**
 * Created by liaohui on 2017/9/14.
 */

public class MyBaseViewHolder extends BaseViewHolder {
    protected Context mContext;

    public MyBaseViewHolder(View view) {
        super(view);
        mContext = view.getContext();
    }

    public MyBaseViewHolder setImageUrl(int viewId, String imgUrl) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl).asBitmap().error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(view);
        return this;
    }

    public MyBaseViewHolder setImageUrl(int viewId, String imgUrl, int placeHolderRes) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl).asBitmap().placeholder(placeHolderRes).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(view);
        return this;
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public MyBaseViewHolder loadIntoUseFitWidth(int viewId, String imageUrl, int placeHolderRes) {
        final ImageView imageView = getView(viewId);
        Glide.with(mContext)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
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
                .placeholder(placeHolderRes)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
        return this;
    }

    public MyBaseViewHolder setCircleImageUrl(int viewId, String imgUrl, int placeHolderRes) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl).asBitmap().placeholder(placeHolderRes).transform(new CropCircleTransformation(mContext)).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
        return this;
    }

    public MyBaseViewHolder setRoundImageUrl(int viewId, String imgUrl, int placeHolderRes) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl).asBitmap().placeholder(placeHolderRes).transform(new CenterCrop(mContext),new RoundedCornersTransformation(mContext,4,0, RoundedCornersTransformation.CornerType.ALL)).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
        return this;
    }

    public MyBaseViewHolder setRoundImageUrl(int viewId, String imgUrl, int placeHolderRes, int radius) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(imgUrl).asBitmap().placeholder(placeHolderRes).transform(new CenterCrop(mContext),new RoundedCornersTransformation(mContext,radius,0, RoundedCornersTransformation.CornerType.ALL)).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
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
