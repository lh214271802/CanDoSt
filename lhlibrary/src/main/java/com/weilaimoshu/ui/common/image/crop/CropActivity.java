package com.weilaimoshu.ui.common.image.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.weilaimoshu.R;
import com.weilaimoshu.base.activity.BaseActivity;
import com.weilaimoshu.ui.common.image.folder.SelectOptions;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.OnClick;


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
    }


    @Override
    public void initDatas() {
        imageUrl = mOption.getSelectedImages().get(0);
        Glide.with(mContext).load(imageUrl)
                .fitCenter()
                .into(mCropLayout.getImageView());

        mCropLayout.setCropWidth(mOption.getCropWidth());
        mCropLayout.setCropHeight(mOption.getCropHeight());
        mCropLayout.start();
    }

    @OnClick({R.id.tv_crop, R.id.tv_cancel})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_crop:
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
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }
}
