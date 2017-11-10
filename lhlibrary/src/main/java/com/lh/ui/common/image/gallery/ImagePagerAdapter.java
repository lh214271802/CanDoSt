package com.lh.ui.common.image.gallery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lh.R;

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
//                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .animate(R.anim.alpha_toshow)// PICTURE
//                .placeholder(R.color.gray_ba)
                .fitCenter()
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        progressBar.setVisibility(View.GONE);
                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) mContext).finish();
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
