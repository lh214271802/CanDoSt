package cn.lh.candost;

import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.lh.base.activity.BaseActivity;
import com.lh.base.activity.BaseRefreshActivity;
import com.lh.ui.common.image.folder.SelectImageActivity;
import com.lh.ui.common.image.folder.SelectOptions;
import com.lh.ui.common.video.SelectVideoActivity;
import com.lh.util.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import cn.lh.candost.ui.taoke.TaoKeActivity;
import cn.lh.candost.ui.weixinbest.WeiXinBestActivity;

public class MainActivity extends BaseRefreshActivity {

    private ImageView imageView;
    private JZVideoPlayerStandard videoPlayer;

    @Override
    protected View getToolbarLayout() {
        return getDefaultToolbar("春风又绿江南岸");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
//        BarUtils.setStatusBarAlpha(this);
        StatusBarUtil.StatusBarLightMode(this);
        initRefreshLayout(true, false);
        findViewById(R.id.select_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageActivity.show(mContext,
                        new SelectOptions.Builder()
                                .setHasCam(true)
                                .setSelectCount(1)
                                .setCrop(720, 720)
                                .setCallback(new SelectOptions.Callback() {
                                    @Override
                                    public void doSelected(List<String> videos) {
                                        Glide.with(mContext).load(videos.get(0)).into(imageView);
                                    }
                                })
                                .build());

            }
        });
        findViewById(R.id.select_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectVideoActivity.startActivity(mContext, new SelectOptions.Builder()
                        .setCallback(new SelectOptions.Callback() {
                            @Override
                            public void doSelected(List<String> images) {
                                videoPlayer.setUp(images.get(0), JZVideoPlayer.SCREEN_WINDOW_NORMAL, "嫂子请闭眼");
                                Glide.with(mContext).load(images.get(0)).into(videoPlayer.thumbImageView);
                            }
                        })
                        .setHasCam(true)
                        .setSelectCount(5).build());
            }
        });
        findViewById(R.id.go_taoke).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaoKeActivity.startActivity(mContext);
            }
        });
        findViewById(R.id.go_weixin_best).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiXinBestActivity.startActivity(mContext);
            }
        });
        imageView = findViewById(R.id.image_view);
        videoPlayer = findViewById(R.id.video_player);
    }

    @Override
    protected void initDatas() {
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                smartRefreshLayout.finishRefresh();
            }
        }, 2000);
    }
}
