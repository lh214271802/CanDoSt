package com.lh.ui.common.video;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.lh.R;
import com.lh.base.permission.PermissionBaseActivity;
import com.lh.base.permission.PermissionBean;
import com.lh.ui.common.image.folder.SelectOptions;
import com.lh.util.StatusBarUtil;

import java.util.List;


/**
 * Created by Administrator on 2017/6/1.
 */

public class SelectVideoActivity extends PermissionBaseActivity implements SelectVideoContract.Operator {
    private static SelectOptions mOption;
    private SelectVideoContract.View mView;


    public static void startActivity(Context mContext, SelectOptions options) {
        mOption = options;
        mContext.startActivity(new Intent(mContext, SelectVideoActivity.class));
    }

    @Override
    protected View getToolbarLayout() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_empty_container;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.StatusBarLightMode(this);
        requestExternalStorage();
    }

    @Override
    public void initDatas() {
    }


    @Override
    public void requestCamera() {
        applyForPermissions(new PermissionBean[]{PermissionBean.CAMERA}, PermissionBean.CAMERA.getRequestCode());
    }

    @Override
    public void requestExternalStorage() {
        applyForPermissions(new PermissionBean[]{PermissionBean.WSTORAGE}, PermissionBean.WSTORAGE.getRequestCode());
    }

    @Override
    protected PermissionBean[] getNeedPermissions() {
        return null;
    }

    @Override
    protected void successGetPermissions(PermissionBean[] permissionBeans, int applyCode) {
        if (applyCode == PermissionBean.CAMERA.getRequestCode()) {
            if (mView != null) {
                mView.onOpenCameraSuccess();
            }
        } else if (applyCode == PermissionBean.WSTORAGE.getRequestCode()) {
            if (mView == null) {
                handleView();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
        if (requestCode == PermissionBean.CAMERA.getRequestCode()) {
            if (mView != null) {
                mView.onCameraPermissionDenied();
            }
        } else if (requestCode == PermissionBean.WSTORAGE.getRequestCode()) {
            if (mView != null) {
                removeView();
            }
        }
    }

    @Override
    public void onBack() {
        finish();
        onSupportNavigateUp();
    }

    @Override
    public void setDataView(SelectVideoContract.View view) {
        mView = view;
    }

    @Override
    protected void onDestroy() {
        mOption = null;
        super.onDestroy();
    }


    private void removeView() {
        SelectVideoContract.View view = mView;
        if (view == null)
            return;
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove((Fragment) view)
                    .commitNowAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleView() {
        try {
            //Fragment fragment = Fragment.instantiate(this, SelectFragment.class.getName());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_content, SelectVideoFragment.newInstance(mOption))
                    .commitNowAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
