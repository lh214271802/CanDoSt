package com.lh.base.permission;

import android.Manifest;

/**
 * Created by liaohui on 2017/9/13.
 */

public enum PermissionBean {
    CAMERA(Manifest.permission.CAMERA, 0x1001, "相机拍照"),
    RSTORAGE(Manifest.permission.READ_EXTERNAL_STORAGE, 0x1002, "读取内存"),
    WSTORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE, 0x1003, "读写内存"),
    LOCATION(Manifest.permission.ACCESS_FINE_LOCATION, 0x1004, "定位"),
    PHONE(Manifest.permission.CALL_PHONE, 0x1005, "拨打电话"),
    RPHONE(Manifest.permission.READ_PHONE_STATE, 0x1006, "拨打电话及管理通话"),
    SENDSMS(Manifest.permission.SEND_SMS, 0x1007, "发送短信"),
    RECORDAUDIO(Manifest.permission.RECORD_AUDIO, 0x1008, "录音"),
    RCONTACTS(Manifest.permission.READ_CONTACTS, 0x1009, "读取联系人"),
    RNETSTATE(Manifest.permission.ACCESS_NETWORK_STATE, 0x1010, "检测联网方式"),
    AWIFISTATE(Manifest.permission.ACCESS_WIFI_STATE, 0x1011, "读取设备信息");


    //权限名称
    private String permissionName;
    //权限申请请求码
    private int requestCode;
    //权限申请提示语句
    private String requestMessage;

    PermissionBean(String permissionName, int requestCode, String requestMessage) {
        this.permissionName = permissionName;
        this.requestCode = requestCode;
        this.requestMessage = requestMessage;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public int getRequestCode() {
        return requestCode;
    }


    public String getRequestMessage() {
        return requestMessage;
    }
}
