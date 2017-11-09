package com.lh.api.upload;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liaohui on 2017/11/9.
 */

public class UploadBean implements Parcelable {
    private long hasWrittenLen;
    private long totalLen;
    private boolean hasFinish;

    public UploadBean(long hasWrittenLen, long totalLen, boolean hasFinish) {
        this.hasWrittenLen = hasWrittenLen;
        this.totalLen = totalLen;
        this.hasFinish = hasFinish;
    }

    protected UploadBean(Parcel in) {
        hasWrittenLen = in.readLong();
        totalLen = in.readLong();
        hasFinish = in.readByte() != 0;
    }

    public static final Creator<UploadBean> CREATOR = new Creator<UploadBean>() {
        @Override
        public UploadBean createFromParcel(Parcel in) {
            return new UploadBean(in);
        }

        @Override
        public UploadBean[] newArray(int size) {
            return new UploadBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(hasWrittenLen);
        parcel.writeLong(totalLen);
        parcel.writeByte((byte) (hasFinish ? 1 : 0));
    }
}
