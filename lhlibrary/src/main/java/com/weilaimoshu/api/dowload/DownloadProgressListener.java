package com.weilaimoshu.api.dowload;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}