package cn.lh.candost;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anbetter.danmuku.DanMuParentView;
import com.anbetter.danmuku.DanMuView;
import com.anbetter.danmuku.model.DanMuModel;
import com.anbetter.danmuku.model.utils.DimensionUtil;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lh.base.activity.BaseRefreshActivity;
import com.lh.ui.common.image.folder.SelectImageActivity;
import com.lh.ui.common.image.folder.SelectOptions;
import com.lh.ui.common.image.gallery.ImageGalleryActivity;
import com.lh.ui.common.video.SelectVideoActivity;
import com.lh.ui.common.web.CommonWebAcitivity;
import com.lh.util.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.util.ArrayList;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import cn.lh.candost.tinker.app.BaseBuildInfo;
import cn.lh.candost.tinker.app.BuildInfo;
import cn.lh.candost.tinker.util.Utils;
import cn.lh.candost.ui.taoke.TaoKeActivity;
import cn.lh.candost.ui.weixinbest.WeiXinBestActivity;

public class MainActivity extends BaseRefreshActivity {

    private ImageView imageView;
    private JZVideoPlayerStandard videoPlayer;
    private DanMuParentView danmuParentView;
    private DanMuView danmuView;
    private ImageView smile_avd;

    @Override
    protected View getToolbarLayout() {
        ///你好啊 我是testNewBranches啊
        return getDefaultToolbar("春风又绿江南岸");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /////////////////////////TODO FUFFFFFFFFFFFFFFFFFFFFFFF
        Utils.setBackground(false);
        LogUtils.e("嘻嘻");
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
        Utils.setBackground(true);
    }

    @Override
    protected void initViews() {
        super.initViews();
        //TODO 加载补丁包
        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip");

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

        findViewById(R.id.show_tinker_info).setOnClickListener(v -> showInfo(mContext));


        findViewById(R.id.go_to_webview).setOnClickListener(v -> {
//                WebViewActivity.startActivity(mContext);
            CommonWebAcitivity.startActivity(mContext, "http://api.viimoo.cn/api_2_0_0_3/html/app/informations.html?uuid=1125901E-B5DE-522C-C68D-A793EB253A27&authsign=", "测试", true);
        });


        smile_avd = findViewById(R.id.smile_avd);
        smile_avd.setOnClickListener(v -> {
            AnimatedVectorDrawable smileDrawable = (AnimatedVectorDrawable) smile_avd.getDrawable();
            smileDrawable.start();
        });
    }

    public boolean showInfo(Context context) {
        // add more Build Info
        final StringBuilder sb = new StringBuilder();
        Tinker tinker = Tinker.with(getApplicationContext());
        if (tinker.isTinkerLoaded()) {
            sb.append(String.format("[patch is loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", BuildInfo.TINKER_ID));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", BaseBuildInfo.BASE_TINKER_ID));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.TINKER_ID)));
            sb.append(String.format("[packageConfig patchMessage] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName("patchMessage")));
            sb.append(String.format("[TINKER_ID Rom Space] %d k \n", tinker.getTinkerRomSpace()));

        } else {
            sb.append(String.format("[patch is not loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", BuildInfo.TINKER_ID));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", BaseBuildInfo.BASE_TINKER_ID));
            sb.append("\nfuck you \n");

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", ShareTinkerInternals.getManifestTinkerID(getApplicationContext())));
        }
        sb.append(String.format("[BaseBuildInfo Message] %s \n", BaseBuildInfo.TEST_MESSAGE));

        final TextView v = new TextView(context);
        v.setText(sb);
        v.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setTextColor(0xFF000000);
        v.setTypeface(Typeface.MONOSPACE);
        final int padding = 16;
        v.setPadding(padding, padding, padding, padding);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(v);
        final AlertDialog alert = builder.create();
        alert.show();
        return true;
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
