package com.lh.base.permission;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.lh.base.activity.BaseActivity;
import com.lh.util.DialogHelper;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by liaohui on 2017/9/13.
 */

public abstract class PermissionBaseActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private PermissionBean[] needPermissions;
    private int PERMISSIONREQUESTCODE = 0x1001;
    private int requestCode = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestForPermission(getNeedPermissions());
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化需要的权限
     */
    private void requestForPermission(PermissionBean[] permissions) {
        if (permissions != null && permissions.length > 0) {
            this.needPermissions = permissions;
        }
    }

    /**
     * 初始化阶段就需要获取的权限,getNeedPermissions()和applyForPermissions()方法中的参数不能同时为空
     */
    protected abstract PermissionBean[] getNeedPermissions();

    /**
     * 成功获取权限之后的操作
     */
    protected abstract void successGetPermissions(PermissionBean[] permissionBeans, int applyCode);

    /**
     * 申请权限就直接调用此方法,getNeedPermissions()和applyForPermissions()方法中的参数不能同时为空
     * applyCode作为一个标志位，方便其他操作
     */
    protected void applyForPermissions(PermissionBean[] permissions, int applyCode) {
        requestForPermission(permissions);
        this.requestCode = applyCode;
        if (needPermissions != null && needPermissions.length > 0) {
            String[] persArray = new String[needPermissions.length];
            for (int i = 0; i < needPermissions.length; i++) {
                persArray[i] = needPermissions[i].getPermissionName();
            }
            if (EasyPermissions.hasPermissions(this, persArray)) {
                successGetPermissions(needPermissions, applyCode);
            } else {
                if (needPermissions.length == 1) {//只申请一个权限的时候
                    EasyPermissions.requestPermissions(this, needPermissions[0].getRequestMessage(), needPermissions[0].getRequestCode(), needPermissions[0].getPermissionName());
                } else {//同时申请多个权限的时候
                    EasyPermissions.requestPermissions(this, "申请必要的权限", PERMISSIONREQUESTCODE, persArray);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 授权成功
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms != null && perms.size() == needPermissions.length) {
            successGetPermissions(needPermissions, this.requestCode);
        }
    }

    /**
     * 授权失败
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms != null && perms.size() > 0) {
            StringBuilder permissionApply = new StringBuilder();
            if (perms.size() > 1) {
                permissionApply.append("请求获取");
                for (int i = 0; i < needPermissions.length; i++) {
                    for (int j = 0; j < perms.size(); j++) {
                        if (needPermissions[i].getPermissionName().equals(perms.get(j)) && !EasyPermissions.hasPermissions(mContext, perms.get(j))) {
                            permissionApply.append(needPermissions[i].getRequestMessage().substring(4, needPermissions[i].getRequestMessage().length() - 2) + "、");
                        }
                    }
                }
                permissionApply.deleteCharAt(permissionApply.lastIndexOf("、"));
                permissionApply.append("权限");
            } else {
                for (int i = 0; i < needPermissions.length; i++) {
                    if (needPermissions[i].getPermissionName().equals(perms.get(0))) {
                        permissionApply.append(needPermissions[i].getRequestFailMessage());
                    }
                }
            }
            DialogHelper.getConfirmDialog(this, "", permissionApply.toString(), "去设置", "取消", false, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                    finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();
        }
    }
}
