package cn.lh.candost;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.anbetter.danmuku.DanMuParentView;
import com.anbetter.danmuku.DanMuView;
import com.anbetter.danmuku.model.DanMuModel;
import com.anbetter.danmuku.model.utils.DimensionUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lh.base.activity.BaseRefreshActivity;
import com.lh.ui.common.image.folder.SelectImageActivity;
import com.lh.ui.common.image.folder.SelectOptions;
import com.lh.ui.common.image.gallery.ImageGalleryActivity;
import com.lh.ui.common.video.SelectVideoActivity;
import com.lh.util.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import cn.lh.candost.ui.taoke.TaoKeActivity;
import cn.lh.candost.ui.weixinbest.WeiXinBestActivity;

public class MainActivity extends BaseRefreshActivity {

    private ImageView imageView;
    private JZVideoPlayerStandard videoPlayer;
    private DanMuParentView danmuParentView;
    private DanMuView danmuView;

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
        findViewById(R.id.select_image).setOnClickListener(v -> SelectImageActivity.show(mContext,
                new SelectOptions.Builder()
                        .setHasCam(true)
                        .setSelectCount(1)
                        .setCrop(720, 720)
                        .setCallback(videos -> Glide.with(mContext).load(videos.get(0)).into(imageView))
                        .build()));
        findViewById(R.id.image_gallery).setOnClickListener(v -> ImageGalleryActivity.startActivity(mContext, 0, new ArrayList<String>() {{
            add("http://img06.tooopen.com/images/20161029/tooopen_sy_183839859946.jpg");
            add("http://img.zcool.cn/community/010fc7554b7d28000001bf721916fc.jpg@1280w_1l_2o_100sh.jpg");
            add("http://f.hiphotos.baidu.com/image/pic/item/3ac79f3df8dcd1008742b1cc788b4710b8122f04.jpg");
            add("http://h.hiphotos.baidu.com/image/pic/item/f31fbe096b63f624bcc66aea8d44ebf81b4ca344.jpg");
            add("http://img0.imgtn.bdimg.com/it/u=2020395685,1666600559&fm=214&gp=0.jpg");
            add("http://ww2.sinaimg.cn/large/85d77acdgw1f4hs72wzxkg20dw07t7k3.jpg");
            add("http://ww1.sinaimg.cn/large/85cccab3gw1etdl4dxvt0g20ci071kjl.jpg");
        }}, true));
        findViewById(R.id.select_video).setOnClickListener(view -> SelectVideoActivity.startActivity(mContext, new SelectOptions.Builder()
                .setCallback(images -> {
                    videoPlayer.setUp(images.get(0), JZVideoPlayer.SCREEN_WINDOW_NORMAL, "嫂子请闭眼");
                    Glide.with(mContext).load(images.get(0)).into(videoPlayer.thumbImageView);
                })
                .setHasCam(true)
                .setSelectCount(5).build()));
        findViewById(R.id.go_taoke).setOnClickListener(view ->
                TaoKeActivity.startActivity(mContext)
        );
        findViewById(R.id.go_weixin_best).setOnClickListener(view -> WeiXinBestActivity.startActivity(mContext));
        findViewById(R.id.danmu_show).setOnClickListener(view -> {
            DanMuModel danMuModel = new DanMuModel();
            danMuModel.setDisplayType(DanMuModel.RIGHT_TO_LEFT);
            danMuModel.setPriority(DanMuModel.NORMAL);
            danMuModel.marginLeft = DimensionUtil.dpToPx(mContext, 30);

            // 显示的文本内容
            danMuModel.textSize = DimensionUtil.spToPx(mContext, 14);
            danMuModel.textColor = ContextCompat.getColor(mContext, R.color.colorAccent);
            danMuModel.textMarginLeft = DimensionUtil.dpToPx(mContext, 5);
            danMuModel.text = "春风油绿江南岸";

            // 弹幕文本背景
            danMuModel.textBackground = ContextCompat.getDrawable(mContext, R.drawable.round_rect_blue);
            danMuModel.textBackgroundMarginLeft = DimensionUtil.dpToPx(mContext, 15);
            danMuModel.textBackgroundPaddingTop = DimensionUtil.dpToPx(mContext, 3);
            danMuModel.textBackgroundPaddingBottom = DimensionUtil.dpToPx(mContext, 3);
            danMuModel.textBackgroundPaddingRight = DimensionUtil.dpToPx(mContext, 15);
            danMuModel.enableTouch(true);
            //弹幕点击事件
            danMuModel.setOnTouchCallBackListener(danMuView -> ToastUtils.showShort("点击啊"));

            danmuView.add(danMuModel);
        });
        imageView = findViewById(R.id.image_view);
        videoPlayer = findViewById(R.id.video_player);
        videoPlayer.setSystemTimeAndBattery();
        danmuParentView = findViewById(R.id.danmu_parent_view);
        danmuView = findViewById(R.id.danmu_view);
        danmuView.prepare();
    }

    @Override
    protected void initDatas() {
        danmuView.hideAllDanMuView(false);
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
