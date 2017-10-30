package com.weilaimoshu.base.permission;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.weilaimoshu.base.activity.BaseActivity;
import com.weilaimoshu.util.DialogHelper;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by liaohui on 2017/9/13.
 */

public abstract class PermissionBaseActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private PermissionBean[] needPermissions;
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
    protected PermissionBean[] getNeedPermissions() {
        return null;
    }

    /**
     * 成功获取权限之后的操作
     */
    protected void successGetPermissions(PermissionBean[] permissionBeans, int applyCode) {
    }

    /**
     * 申请权限就直接调用此方法,getNeedPermissions()和applyForPermissions()方法中的参数不能同时为空
     * applyCode作为一个标志位，方便其他操作
     */
    protected void applyForPermissions(PermissionBean[] permissions, int requestCode) {
        requestForPermission(permissions);
        this.requestCode = requestCode;
        if (needPermissions != null && needPermissions.length > 0) {
            String[] persArray = new String[needPermissions.length];
            for (int i = 0; i < needPermissions.length; i++) {
                persArray[i] = needPermissions[i].getPermissionName();
            }
            if (EasyPermissions.hasPermissions(this, persArray)) {
                successGetPermissions(needPermissions, requestCode);
            } else {
                if (needPermissions.length == 1) {//只申请一个权限的时候
                    EasyPermissions.requestPermissions(this, needPermissions[0].getRequestMessage(), needPermissions[0].getRequestCode(), needPermissions[0].getPermissionName());
                } else {//同时申请多个权限的时候
                    EasyPermissions.requestPermissions(this, "申请必要的权限", requestCode, persArray);
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
            DialogHelper.getConfirmDialog(this, "", perms.size() == 1 ? needPermissions[0].getRequestFailMessage() : "未申请获得必要的权限", "去设置", "取消", false, new DialogInterface.OnClickListener() {
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
