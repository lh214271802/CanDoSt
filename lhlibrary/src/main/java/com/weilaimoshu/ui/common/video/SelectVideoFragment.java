package com.weilaimoshu.ui.common.video;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.weilaimoshu.R;
import com.weilaimoshu.base.fragment.BaseFragment;
import com.weilaimoshu.ui.common.crop.CropActivity;
import com.weilaimoshu.ui.common.image.bean.Image;
import com.weilaimoshu.ui.common.image.folder.EmptyLayout;
import com.weilaimoshu.ui.common.image.folder.ImageLoaderListener;
import com.weilaimoshu.ui.common.image.folder.SelectOptions;
import com.weilaimoshu.util.CameraUtils;
import com.weilaimoshu.view.DividerDecoration;
import com.weilaimoshu.view.SupportGridItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 视频选择库实现界面
 */
public class SelectVideoFragment extends BaseFragment implements OnClickListener, ImageLoaderListener, BaseQuickAdapter.OnItemClickListener, SelectVideoContract.View {

    private static final int RC_CAMERA_PERM = 0x03;
    private static final int RC_EXTERNAL_STORAGE = 0x04;


    @BindView(R.id.rv_image)
    RecyclerView mContentView;
    @BindView(R.id.btn_title_select)
    Button mSelectFolderView;
    @BindView(R.id.iv_title_select)
    ImageView mSelectFolderIcon;
    @BindView(R.id.toolbar)
    View mToolbar;
    @BindView(R.id.btn_done)
    Button mDoneView;
    @BindView(R.id.btn_preview)
    Button mPreviewView;

    @BindView(R.id.error_layout)
    EmptyLayout mErrorLayout;

    private VideoFolderPopupWindow mFolderPopupWindow;
    private VideoFolderAdapter mVideoFolderAdapter;
    private VideoAdapter mVideoAdapter;
    private LoaderListener mCursorLoader = new LoaderListener();
    private String mCamVideoName;
    private List<Video> mSelectedVideo;
    private SelectVideoContract.Operator mOperator;

    private static SelectOptions mOption;

    public static SelectVideoFragment newInstance(SelectOptions options) {
        mOption = options;
        return new SelectVideoFragment();
    }

    @Override
    public void onAttach(Context context) {
        this.mOperator = (SelectVideoContract.Operator) context;
        this.mOperator.setDataView(this);
        super.onAttach(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_image;
    }

    @Override
    protected void initViews() {
        if (mOption == null) {
            getActivity().finish();
            return;
        }
        getLoaderManager().initLoader(0, null, mCursorLoader);
        mContentView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mContentView.addItemDecoration(new SupportGridItemDecoration(mContext));
        mVideoAdapter = new VideoAdapter(new ArrayList<Video>(), this);
        mVideoAdapter.setSingleSelect(mOption.getSelectCount() <= 1);
        contentView.findViewById(R.id.lay_button).setVisibility(mOption.getSelectCount() == 1 ? View.GONE : View.VISIBLE);
        mVideoFolderAdapter = new VideoFolderAdapter(R.layout.item_list_folder, this);
        mContentView.setAdapter(mVideoAdapter);
        mContentView.setItemAnimator(null);
        mVideoAdapter.setOnItemClickListener(this);
        mErrorLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                getLoaderManager().initLoader(0, null, mCursorLoader);
            }
        });
    }

    @Override
    public void initDatas() {
        if (mOption == null) {
            getActivity().finish();
            return;
        }
        mSelectedVideo = new ArrayList<>();

        if (mOption.getSelectCount() > 1 && mOption.getSelectedImages() != null) {
            List<String> images = mOption.getSelectedImages();
            for (String s : images) {
                // checkShare file exists
                if (s != null && new File(s).exists()) {
                    Video video = new Video();
                    video.setSelect(true);
                    video.setPath(s);
                    mSelectedVideo.add(video);
                }
            }
        }
        getLoaderManager().initLoader(0, null, mCursorLoader);
    }

    @OnClick({R.id.btn_preview, R.id.icon_back, R.id.btn_title_select, R.id.btn_done})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_back:
                mOperator.onBack();
                break;
            case R.id.btn_preview://TODO 预览

                break;
            case R.id.btn_title_select://TODO 下拉视频集合
                showPopupFolderList();
                break;
            case R.id.btn_done://完成
                onSelectComplete();
                break;
        }
    }

    private List<String> getArray(List<Video> images) {
        if (images == null)
            return null;
        int len = images.size();
        if (len == 0)
            return null;

        List<String> strings = new ArrayList<>();
        for (Video image : images) {
            strings.add(image.getPath());
        }
        return strings;
    }

    private void handleSelectSizeChange(int size) {
        if (size > 0) {
            mPreviewView.setEnabled(true);
            mDoneView.setEnabled(true);
            mDoneView.setText(String.format("%s(%s)", getText(R.string.action_done), size));
        } else {
            mPreviewView.setEnabled(false);
            mDoneView.setEnabled(false);
            mDoneView.setText(getText(R.string.action_done));
        }
    }

    private void handleSelectChange(int position) {
        Video image = mVideoAdapter.getItem(position);
        if (image == null)
            return;
        //如果是多选模式
        final int selectCount = mOption.getSelectCount();
        if (selectCount > 1) {
            if (image.isSelect()) {
                image.setSelect(false);
                mSelectedVideo.remove(image);
                mVideoAdapter.updateItem(position);
            } else {
                if (mSelectedVideo.size() == selectCount) {
                    Toast.makeText(getActivity(), "最多只能选择 " + selectCount + " 张照片", Toast.LENGTH_SHORT).show();
                } else {
                    image.setSelect(true);
                    mSelectedVideo.add(image);
                    mVideoAdapter.updateItem(position);
                }
            }
            handleSelectSizeChange(mSelectedVideo.size());
        } else {
            mSelectedVideo.add(image);
            handleResult();
        }
    }

    /**
     * 获得结果
     */
    private void handleResult() {
        if (mSelectedVideo.size() != 0) {
            if (mOption.isCrop()) {
                List<String> selectedImage = mOption.getSelectedImages();
                selectedImage.clear();
                selectedImage.add(mSelectedVideo.get(0).getPath());
                mSelectedVideo.clear();
                CropActivity.show(this, mOption);
            } else {
                mOption.getCallback().doSelected(getArray(mSelectedVideo));
                getActivity().finish();
            }
        }
    }

    /**
     * 完成选择
     */


    public void onSelectComplete() {
        handleResult();
    }

    /**
     * 申请相机权限成功
     */


    @Override

    public void onOpenCameraSuccess() {
        toOpenCamera();
    }


    @Override
    public void onCameraPermissionDenied() {

    }

    /**
     * 创建弹出的相册
     */
    private void showPopupFolderList() {
        if (mFolderPopupWindow == null) {
            VideoFolderPopupWindow popupWindow = new VideoFolderPopupWindow(getActivity(), new VideoFolderPopupWindow.Callback() {
                @Override
                public void onSelect(VideoFolderPopupWindow popupWindow, VideoFolder model) {
                    addVideosToAdapter(model.getImages());
                    mContentView.scrollToPosition(0);
                    popupWindow.dismiss();
                    mSelectFolderView.setText(model.getName());
                }

                @Override
                public void onDismiss() {
                    mSelectFolderIcon.setImageResource(R.drawable.ic_bottom_arrow);
                }

                @Override
                public void onShow() {
                    mSelectFolderIcon.setImageResource(R.drawable.ic_arrow_top);
                }
            });
            popupWindow.setAdapter(mVideoFolderAdapter);
            mFolderPopupWindow = popupWindow;
        }
        mFolderPopupWindow.showAsDropDown(mToolbar);
    }

    /**
     * 打开相机
     */
    private void toOpenCamera() {//TODO 录制视频
//        CameraActivity.startActivity(mContext);
    }

    /**
     * 视频录制完成通知系统添加视频
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case 0x03:
            /*        if (mCamImageName == null) return;
                    Uri localUri = Uri.fromFile(new File(CameraUtils.getCameraPath() + mCamImageName));
                    Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                    getActivity().sendBroadcast(localIntent);*/
                    break;
            }
        }
    }

    @Override
    public void displayImage(final ImageView iv, final String path) {
        if (TextUtils.isEmpty(path)) return;
        Glide.with(mContext).load(Uri.fromFile(new File(path)))
                .asBitmap()
                .error(R.mipmap.ic_split_graph)
                .centerCrop()
                .into(iv);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mOption.isHasCam()) {
            if (position != 0) {
                handleSelectChange(position);
            } else {
                if (mSelectedVideo.size() < mOption.getSelectCount()) {
                    mOperator.requestCamera();
                } else {
                    Toast.makeText(getActivity(), "最多只能选择 " + mOption.getSelectCount() + " 部视频", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            handleSelectChange(position);
        }
    }


    private class LoaderListener implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] Video_PROJECTION = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.MINI_THUMB_MAGIC,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == 0) {
                //数据库光标加载器
                return new CursorLoader(getContext(),
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, Video_PROJECTION,
                        null, null, Video_PROJECTION[2] + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
            if (data != null) {

                final ArrayList<Video> Videos = new ArrayList<>();
                final List<VideoFolder> VideoFolders = new ArrayList<>();

                final VideoFolder defaultFolder = new VideoFolder();
                defaultFolder.setName("全部视频");
                defaultFolder.setPath("");
                VideoFolders.add(defaultFolder);

                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(Video_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(Video_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(Video_PROJECTION[2]));
                        int id = data.getInt(data.getColumnIndexOrThrow(Video_PROJECTION[3]));
                        String thumbPath = data.getString(data.getColumnIndexOrThrow(Video_PROJECTION[4]));
                        String bucket = data.getString(data.getColumnIndexOrThrow(Video_PROJECTION[5]));

                        Video video = new Video();
                        video.setPath(path);
                        video.setName(name);
                        video.setDate(dateTime);
                        video.setId(id);
                        video.setThumbPath(thumbPath);
                        video.setFolderName(bucket);

                        Videos.add(video);
                        //TODO 如果是新拍的视频
                        if (mCamVideoName != null && mCamVideoName.equals(video.getName())) {
                            video.setSelect(true);
                            mSelectedVideo.add(video);
                        }

                        //如果是被选中的图片
                        if (mSelectedVideo.size() > 0) {
                            for (Video i : mSelectedVideo) {
                                if (i.getPath().equals(video.getPath())) {
                                    video.setSelect(true);
                                }
                            }
                        }

                        File VideoFile = new File(path);
                        File folderFile = VideoFile.getParentFile();
                        VideoFolder folder = new VideoFolder();
                        folder.setName(folderFile.getName());
                        folder.setPath(folderFile.getAbsolutePath());
                        if (!VideoFolders.contains(folder)) {
                            folder.getImages().add(video);
                            folder.setAlbumPath(video.getPath());//默认相册封面
                            VideoFolders.add(folder);
                        } else {
                            // 更新
                            VideoFolder f = VideoFolders.get(VideoFolders.indexOf(folder));
                            f.getImages().add(video);
                        }


                    } while (data.moveToNext());
                }
                addVideosToAdapter(Videos);
                defaultFolder.getImages().addAll(Videos);

                if (mOption.isHasCam()) {
                    defaultFolder.setAlbumPath(Videos.size() > 1 ? Videos.get(1).getPath() : null);
                } else {
                    defaultFolder.setAlbumPath(Videos.size() > 0 ? Videos.get(0).getPath() : null);
                }
                mVideoFolderAdapter.resetItem(VideoFolders);

                //删除掉不存在的，在于用户选择了相片，又去相册删除
                if (mSelectedVideo.size() > 0) {
                    List<Video> rs = new ArrayList<>();
                    for (Video i : mSelectedVideo) {
                        File f = new File(i.getPath());
                        if (!f.exists()) {
                            rs.add(i);
                        }
                    }
                    mSelectedVideo.removeAll(rs);
                }


                //用户录制的新视频默认选中， If add new mCamera picture, and we only need one picture, we result it.
                if (mCamVideoName != null) {
                    handleResult();
                }
                handleSelectSizeChange(mSelectedVideo.size());
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private void addVideosToAdapter(ArrayList<Video> Videos) {
        mVideoAdapter.setNewData(null);
        Video cam = new Video();
        mVideoAdapter.addData(cam);

        mVideoAdapter.addData(Videos);
    }

    @Override
    public void onDestroy() {
        mOption = null;
        super.onDestroy();
    }
}
