package com.lh.base.permission;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.blankj.utilcode.util.LogUtils;
import com.lh.base.activity.BaseActivity;
import com.lh.util.DialogHelper;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by liaohui on 2017/9/13.
 */

public abstract class PermissionBaseActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private PermissionBean[] needPermissions;
    private int requestCode = -1;
    private RequestPermissionsCallBack callBack;


    /**
     * 申请权限就直接调用此方法,getNeedPermissions()和applyForPermissions()方法中的参数不能同时为空
     * applyCode作为一个标志位，方便其他操作
     */
    protected void requestForPermissions(PermissionBean[] permissions, int requestCode, RequestPermissionsCallBack callBack) {
        if (permissions != null && permissions.length > 0) {
            this.needPermissions = permissions;
        }
        this.requestCode = requestCode;
        this.callBack = callBack;
        if (needPermissions != null && needPermissions.length > 0) {
            String[] persArray = new String[needPermissions.length];
            StringBuilder requestMessage = new StringBuilder("请求获取");
            for (int i = 0; i < needPermissions.length; i++) {
                persArray[i] = needPermissions[i].getPermissionName();
                requestMessage.append(needPermissions[i].getRequestMessage() + "、");
            }
            requestMessage.deleteCharAt(requestMessage.lastIndexOf("、"));
            requestMessage.append("权限");
            if (EasyPermissions.hasPermissions(this, persArray)) {
                callBack.onSuccess();
            } else {
                EasyPermissions.requestPermissions(this, new String(requestMessage), this.requestCode, persArray);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 授权成功
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms != null && perms.size() == needPermissions.length) {
            callBack.onSuccess();
        }
    }

    /**
     * 授权失败
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms != null && perms.size() > 0) {
            StringBuilder requestMessage = new StringBuilder();
            requestMessage.append("请求获取");
            for (int i = 0; i < perms.size(); i++) {
                for (int j = 0; j < needPermissions.length; j++) {
                    if (needPermissions[j].getPermissionName().equals(perms.get(i))) {
                        requestMessage.append(needPermissions[j].getRequestMessage() + "、");
                    }
                }
            }
            if (requestMessage.indexOf("、") != -1) {
                requestMessage.deleteCharAt(requestMessage.lastIndexOf("、"));
                requestMessage.append("权限");
            } else {
                requestMessage.append("必要的权限");
            }
            DialogHelper.getConfirmDialog(this, "", requestMessage.toString(), "去设置", "取消", false, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                    callBack.onFail(true);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callBack.onFail(false);
                }
            }).show();
        }
    }

    protected interface RequestPermissionsCallBack {
        /**
         * 成功获取权限之后的操作
         */
        void onSuccess();

        void onFail(boolean isGoSetting);
    }
}
