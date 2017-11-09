package com.lh.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lh.R;
import com.lh.glide.GlideRoundTransform;
import com.lh.ui.common.image.gallery.ImageGalleryActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by JuQiu
 * on 16/8/26.
 * 类似朋友圈的图片布局
 */
public class TweetPicturesLayout extends ViewGroup implements View.OnClickListener {
    private static final int SINGLE_MAX_W = 120;
    private static final int SINGLE_MAX_H = 180;
    private static final int SINGLE_MIN_W = 34;
    private static final int SINGLE_MIN_H = 34;
    private String[] mImages;
    private float mVerticalSpacing;
    private float mHorizontalSpacing;
    private int mColumn;
    private int mMaxPictureSize;
    private List<String> allImagesList;

    public TweetPicturesLayout(Context context) {
        this(context, null);
    }

    public TweetPicturesLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TweetPicturesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TweetPicturesLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final Context context = getContext();
        final Resources resources = getResources();
        final float density = resources.getDisplayMetrics().density;

        int vSpace = (int) (4 * density);
        int hSpace = vSpace;

        if (attrs != null) {
            // Load attributes
            final TypedArray a = context.obtainStyledAttributes(
                    attrs, R.styleable.TweetPicturesLayout, defStyleAttr, defStyleRes);

            // Load clip touch corner radius
            vSpace = a.getDimensionPixelOffset(R.styleable.TweetPicturesLayout_verticalSpace, vSpace);
            hSpace = a.getDimensionPixelOffset(R.styleable.TweetPicturesLayout_horizontalSpace, hSpace);
            setColumn(a.getInt(R.styleable.TweetPicturesLayout_column, 3));
            setMaxPictureSize(a.getDimensionPixelOffset(R.styleable.TweetPicturesLayout_maxPictureSize, 0));
            a.recycle();
        }

        setVerticalSpacing(vSpace);
        setHorizontalSpacing(hSpace);
    }

    public void setHorizontalSpacing(float pixelSize) {
        mHorizontalSpacing = pixelSize;
    }

    public void setVerticalSpacing(float pixelSize) {
        mVerticalSpacing = pixelSize;
    }

    public void setColumn(int column) {
        if (column < 1)
            column = 1;
        if (column > 20)
            column = 20;
        mColumn = column;
    }

    public void setMaxPictureSize(int maxPictureSize) {
        if (maxPictureSize < 0)
            maxPictureSize = 0;
        mMaxPictureSize = maxPictureSize;
    }

    public void setImage(String[] images) {
        if (mImages == images)
            return;

        // 移除布局
        removeAllImage();

        // 过滤掉不合法的数据
        if (images != null) {
            List<String> isOkImages = new ArrayList<>();
            for (String image : images) {
                if (!TextUtils.isEmpty(image))
                    isOkImages.add(image);
            }
            images = isOkImages.toArray(new String[isOkImages.size()]);
        }

        // 赋值
        mImages = images;

        if (mImages != null && mImages.length > 0) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            RequestManager requestManager = Glide.with(getContext());
            for (int i = 0; i < mImages.length; i++) {
                String image = mImages[i];
                if (TextUtils.isEmpty(image))
                    continue;

                View view = inflater.inflate(R.layout.lay_tweet_image_item, this, false);
                view.setTag(i);
                view.setOnClickListener(this);

                addView(view);
                if (image.toLowerCase().endsWith("gif")) {
                    view.findViewById(R.id.iv_is_gif).setVisibility(VISIBLE);
                    GifRequestBuilder<String> builder = requestManager.load(image)
                            .asGif()
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher);
                    builder.diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) view.findViewById(R.id.iv_picture));
                } else {
                    BitmapRequestBuilder builder = requestManager.load(image)
                            .asBitmap()
                            .transform(new CenterCrop(this.getContext()), new GlideRoundTransform(this.getContext(), 1))
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher);
                    ImageView iamgeView = (ImageView) view.findViewById(R.id.iv_picture);
                    builder.diskCacheStrategy(DiskCacheStrategy.ALL).into(iamgeView);
                }
            }
            // all do requestLayout
            if (getVisibility() == VISIBLE) {
                requestLayout();
            } else {
                setVisibility(View.VISIBLE);
            }
        } else {
            setVisibility(View.GONE);
        }
    }


    public void removeAllImage() {
        removeAllViews();
        mImages = null;
    }

    private int getMaxChildSize(int size) {
        if (mMaxPictureSize == 0)
            return size;
        else
            return Math.min(mMaxPictureSize, size);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int selfWidth = resolveSize(paddingLeft + paddingRight, widthMeasureSpec);
        int wantedHeight = paddingBottom + paddingTop;
        final int childCount = getChildCount();


        //noinspection StatementWithEmptyBody
        if (childCount == 0) {
            // Not have child we can only need padding size
        } else if (childCount == 1) {
            String image = mImages[0];
            if (!TextUtils.isEmpty(image)) {
                int imageW = 450;
                int imageH = 450;
                imageW = imageW <= 0 ? 100 : imageW;
                imageH = imageH <= 0 ? 100 : imageH;

                float density = getResources().getDisplayMetrics().density;
                // Get max width and height
                float maxContentW = Math.min(selfWidth - paddingRight - paddingLeft, density * SINGLE_MAX_W);
                float maxContentH = density * SINGLE_MAX_H;

                int childW, childH;

                float hToW = imageH / (float) imageW;
                if (hToW > (maxContentH / maxContentW)) {
                    childH = (int) maxContentH;
                    childW = (int) (maxContentH / hToW);
                } else {
                    childW = (int) maxContentW;
                    childH = (int) (maxContentW * hToW);
                }
                // Check the width and height below Min values
                int minW = (int) (SINGLE_MIN_W * density);
                if (childW < minW)
                    childW = minW;
                int minH = (int) (SINGLE_MIN_H * density);
                if (childH < minH)
                    childH = minH;

                View child = getChildAt(0);
                if (child != null) {
                    child.measure(MeasureSpec.makeMeasureSpec(childW, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(childH, MeasureSpec.EXACTLY));
                    wantedHeight += childH;
                }
            }
        } else {
            // Measure all child
            final float maxContentWidth = selfWidth - paddingRight - paddingLeft - mHorizontalSpacing * (mColumn - 1);
            // Get child size
            final int childSize = getMaxChildSize((int) (maxContentWidth / mColumn));

            for (int i = 0; i < childCount; ++i) {
                View childView = getChildAt(i);
                childView.measure(MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY));
            }

            int lines = (int) (childCount / (float) mColumn + 0.9);
            wantedHeight += (int) (lines * childSize + mVerticalSpacing * (lines - 1));
        }

        setMeasuredDimension(selfWidth, resolveSize(wantedHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        float childCount = getChildCount();
        if (childCount > 0) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();

            if (childCount == 1) {
                View childView = getChildAt(0);
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();
                childView.layout(paddingLeft, paddingTop, paddingLeft + childWidth, paddingTop + childHeight);
            } else {
                int mWidth = r - l;
                int paddingRight = getPaddingRight();

                int lineHeight = 0;
                int childLeft = paddingLeft;
                int childTop = paddingTop;

                for (int i = 0; i < childCount; ++i) {
                    View childView = getChildAt(i);

                    if (childView.getVisibility() == View.GONE) {
                        continue;
                    }

                    int childWidth = childView.getMeasuredWidth();
                    int childHeight = childView.getMeasuredHeight();

                    lineHeight = Math.max(childHeight, lineHeight);

                    if (childLeft + childWidth + paddingRight > mWidth) {
                        childLeft = paddingLeft;
                        childTop += mVerticalSpacing + lineHeight;
                        lineHeight = childHeight;
                    }
                    childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                    childLeft += childWidth + mHorizontalSpacing;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        String[] images = allImagesList.toArray(new String[allImagesList.size()]);
        if (images == null || images.length <= 0)
            return;

        Object obj = v.getTag();
        if (obj == null || !(obj instanceof Integer))
            return;

        int index = (int) obj;
        if (index < 0)
            index = 0;
        if (index >= images.length)
            index = images.length - 1;

        String image = images[index];
        if (TextUtils.isEmpty(image))
            return;
        List<String> paths = new ArrayList<>();
        for (String path : images) {
            paths.add(path);
        }
        if (paths == null || paths.size() <= 0)
            return;

        ImageGalleryActivity.startActivity(getContext(), index, paths, false);
    }

    public void setAllImagesList(List<String> allImagesList) {
        this.allImagesList = allImagesList;
    }
}
