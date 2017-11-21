package com.lh.ui.common.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.lh.R;
import com.lh.base.GlideUtils;
import com.lh.base.activity.BaseActivity;
import com.lh.ui.common.image.folder.SelectOptions;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by haibin
 * on 2016/12/2.
 */

public class CropActivity extends BaseActivity implements View.OnClickListener {
    private CropLayout mCropLayout;
    private static SelectOptions mOption;
    private String imageUrl;

    public static void show(Fragment fragment, SelectOptions options) {
        Intent intent = new Intent(fragment.getActivity(), CropActivity.class);
        mOption = options;
        fragment.startActivityForResult(intent, 0x04);
    }

    @Override
    protected void onDestroy() {
        mOption = null;
        super.onDestroy();
    }

    @Override
    protected View getToolbarLayout() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_crop;
    }

    @Override
    protected void initViews() {

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mCropLayout = (CropLayout) findViewById(R.id.cropLayout);
        findViewById(R.id.tv_crop).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }


    @Override
    public void initDatas() {
        imageUrl = mOption.getSelectedImages().get(0);
        Glide.with(mContext).load(imageUrl).apply(GlideUtils.getNormalImageOptions().fitCenter())
                .into(mCropLayout.getImageView());

        mCropLayout.setCropWidth(mOption.getCropWidth());
        mCropLayout.setCropHeight(mOption.getCropHeight());
        mCropLayout.start();
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_crop) {
            Bitmap bitmap = null;
            FileOutputStream os = null;
            Intent intent = new Intent();

            try {
                bitmap = mCropLayout.cropBitmap();
                String path = imageUrl.substring(0, imageUrl.lastIndexOf(".")) + File.separator + "crop.jpg";
                if (FileUtils.createFileByDeleteOldFile(new File(path))) {
                    os = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    intent.putExtra("crop_path", path);
                } else {
                    intent.putExtra("crop_path", imageUrl);//失败就用原图
                }
                os.flush();
                os.close();
                setResult(RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bitmap != null) bitmap.recycle();
                CloseUtils.closeIO(os);
            }

        } else if (i == R.id.tv_cancel) {
            finish();

        }
    }
}
