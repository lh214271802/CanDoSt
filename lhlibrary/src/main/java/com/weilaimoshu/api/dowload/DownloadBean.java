package com.weilaimoshu.api.dowload;

import android.os.Parcel;
import android.os.Parcelable;

public class DownloadBean implements Parcelable {

    private int progress;
    private long currentFileSize;
    private long totalFileSize;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(long currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.progress);
        dest.writeLong(this.currentFileSize);
        dest.writeLong(this.totalFileSize);
    }

    public DownloadBean() {
    }

    protected DownloadBean(Parcel in) {
        this.progress = in.readInt();
        this.currentFileSize = in.readLong();
        this.totalFileSize = in.readLong();
    }

    public static final Parcelable.Creator<DownloadBean> CREATOR = new Parcelable.Creator<DownloadBean>() {
        @Override
        public DownloadBean createFromParcel(Parcel source) {
            return new DownloadBean(source);
        }

        @Override
        public DownloadBean[] newArray(int size) {
            return new DownloadBean[size];
        }
    };

    @Override
    public String toString() {
        return "DownloadBean{" +
                "progress=" + progress +
                ", currentFileSize=" + SizeFormatUtils.getDataSize(currentFileSize) +
                ", totalFileSize=" + SizeFormatUtils.getDataSize(totalFileSize) +
                '}';
    }
}