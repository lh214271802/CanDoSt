package com.lh.api.dowload;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface DownloadApiService {


    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    /**
     * 断点续传下载接口
     * 下载地址需要通过@url动态指定（不适固定的），@head标签是指定下载的起始位置（断点续传的位置）
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);
}