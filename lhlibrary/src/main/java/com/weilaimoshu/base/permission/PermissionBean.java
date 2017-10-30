package com.weilaimoshu.base.permission;

import android.Manifest;

/**
 * Created by liaohui on 2017/9/13.
 */

public enum PermissionBean {
    CAMERA(Manifest.permission.CAMERA, 1001, "请求获取相机拍照权限", "没有权限, 你需要去设置中开启相机拍照权限"),
    RSTORAGE(Manifest.permission.READ_EXTERNAL_STORAGE, 1002, "请求获取读取内存权限", "没有权限, 你需要去设置中开启手机存储读取权限"),
    WSTORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1003, "请求获取读写内存权限", "没有权限, 你需要去设置中开启手机存储读写权限"),
    LOCATION(Manifest.permission.ACCESS_FINE_LOCATION, 1004, "请求获取定位权限", "没有权限, 你需要去设置中手机定位权限"),
    PHONE(Manifest.permission.CALL_PHONE, 1005, "请求获取拨打电话权限", "没有权限, 你需要去设置中开启拨打电话权限"),
    RPHONE(Manifest.permission.READ_PHONE_STATE, 1006, "请求获取拨打电话及管理通话权限", "没有权限, 你需要去设置中开启拨打电话及管理通话权限"),
    SENDSMS(Manifest.permission.SEND_SMS, 1007, "请求获取发送短信权限", "没有权限, 你需要去设置中开启发送短信权限"),
    RECORDAUDIO(Manifest.permission.RECORD_AUDIO, 1008, "请求获取录音权限", "没有权限, 你需要去设置中开启录音权限"),
    RCONTACTS(Manifest.permission.READ_CONTACTS, 1009, "请求获取读取联系人权限", "没有权限, 你需要去设置中开启读取联系人权限");


    //权限名称
    private String permissionName;
    //权限申请请求码
    private int requestCode;
    //权限申请提示语句
    private String requestMessage;
    //权限申请失败提示语句
    private String requestFailMessage;

    PermissionBean(String permissionName, int requestCode, String requestMessage, String requestFailMessage) {
        this.permissionName = permissionName;
        this.requestCode = requestCode;
        this.requestMessage = requestMessage;
        this.requestFailMessage = requestFailMessage;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getRequestFailMessage() {
        return requestFailMessage;
    }

    public void setRequestFailMessage(String requestFailMessage) {
        this.requestFailMessage = requestFailMessage;
    }
}
