package com.weilaimoshu.ui.common.image;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.weilaimoshu.R;
import com.weilaimoshu.api.Constants;
import com.weilaimoshu.base.permission.PermissionBaseActivity;
import com.weilaimoshu.base.permission.PermissionBean;
import com.weilaimoshu.ui.common.image.gallery.ImagePagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 图片预览Activity
 */
public class ImageGalleryActivity extends PermissionBaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_image)
    ViewPager mImagePager;
    @BindView(R.id.tv_index)
    TextView mIndexText;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    private int mCurPosition;

    private List<String> mImageSources;
    private static String click_position = "CLICK_POSITION";
    private static String picture_list = "PICTURE_LIST";
    private static String can_download = "CAN_DOWNLOAD";
    private ImagePagerAdapter imagePagerAdapter;

    public static void startActivity(Context context, int position, List<String> mList, boolean canDownload) {
        context.startActivity(new Intent(context, ImageGalleryActivity.class)
                .putExtra(click_position, position)
                .putExtra(can_download, canDownload)
                .putStringArrayListExtra(picture_list, (ArrayList<String>) mList));
    }

    @Override
    protected View getToolbarLayout() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_gallery;
    }

    @Override
    protected void initViews() {
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mCurPosition = getIntent().getIntExtra(click_position, 0);
        mImageSources = getIntent().getStringArrayListExtra(picture_list);
        ivSave.setVisibility(getIntent().getBooleanExtra(can_download, false) ? View.VISIBLE : View.GONE);
    }


    @Override
    public void initDatas() {
        mImagePager.addOnPageChangeListener(this);
        imagePagerAdapter = new ImagePagerAdapter(mContext, mImageSources);
        mImagePager.setAdapter(imagePagerAdapter);
        mImagePager.setCurrentItem(mCurPosition);
        onPageSelected(mCurPosition);
        if (mImageSources.size() == 1) {
            gone(mIndexText);
        }
    }

    @OnClick({R.id.iv_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_save:
                applyForPermissions(null, -1);
                break;
        }
    }

    @Override
    protected PermissionBean[] getNeedPermissions() {
        return new PermissionBean[]{PermissionBean.WSTORAGE};
    }

    @Override
    protected void successGetPermissions(PermissionBean[] permissionBeans, int applyCode) {
        saveCurrentPicture();
    }

    /**
     * 下载保存当前图片
     */
    private void saveCurrentPicture() {
        if (!SDCardUtils.isSDCardEnable()) {
            ToastUtils.showShort("没有外部存储");
            return;
        }
        final String path = mImageSources.get(mCurPosition);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(mContext)
                            .load(path)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    final String extDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + File.separator + Constants.APP_NAME + File.separator + System.currentTimeMillis() + ".jpg";
                    if (bitmap != null) {
                        if (ImageUtils.save(bitmap, extDir, Bitmap.CompressFormat.JPEG, true)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Uri uri = Uri.fromFile(new File(extDir));
                                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                                    ToastUtils.showShort("保存成功");
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurPosition = position;
        mIndexText.setText(String.format("%s/%s", (position + 1), mImageSources.size()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
