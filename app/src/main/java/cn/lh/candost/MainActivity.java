package cn.lh.candost;

import android.view.View;
import android.widget.ImageView;

import com.lh.base.activity.BaseActivity;
import com.lh.ui.common.image.folder.SelectOptions;
import com.lh.ui.common.video.SelectVideoActivity;

import java.util.List;

import cn.lh.candost.joke.WeiXinBestActivity;

public class MainActivity extends BaseActivity {

    private ImageView imageView;

    @Override
    protected View getToolbarLayout() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        findViewById(R.id.hello_world).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SelectImageActivity.show(mContext,
//                        new SelectOptions.Builder()
//                                .setHasCam(true)
//                                .setSelectCount(1)
//                                .setCrop(720, 720)
//                                .setCallback(new SelectOptions.Callback() {
//                                    @Override
//                                    public void doSelected(List<String> videos) {
//                                        Glide.with(mContext).load(videos.get(0)).into(imageView);
//                                    }
//                                })
//                                .build());
/*                SelectVideoActivity.startActivity(mContext,new SelectOptions.Builder()
                .setCallback(new SelectOptions.Callback() {
                    @Override
                    public void doSelected(List<String> images) {

                    }
                })
                        .setHasCam(true)
                        .setSelectCount(5).build());*/
                WeiXinBestActivity.startActivity(mContext);
            }
        });
        imageView = findViewById(R.id.image_view);
    }

    @Override
    protected void initDatas() {
    }
}
