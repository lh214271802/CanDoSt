package com.lh.ui.common.image.gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lh.R;
import com.lh.base.GlideUtils;

import java.util.List;


/**
 * Created by Administrator on 2017/2/26.
 */
public class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> imageSources;


    public ImagePagerAdapter(Context mContext, List<String> mImageSources) {
        this.mContext = mContext;
        this.imageSources = mImageSources;
    }

    @Override
    public int getCount() {
        return imageSources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.image_gallery_page, null);
        ImagePreviewView imageView = (ImagePreviewView) view.findViewById(R.id.iv_preview);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        Glide.with(mContext)
                .load(imageSources.get(position))
                .apply(GlideUtils.getNormalImageOptions().fitCenter())
                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .into(new DrawableImageViewTarget(imageView, true) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        progressBar.setVisibility(View.GONE);
                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
