package com.lh.api;

import com.lh.base.BaseBean;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;


/**
 * Created by liaohui on 2017/7/20.
 */

public interface AppApiService {
    /**
     * 断点续传下载接口,下载地址需要通过@url动态指定（不适固定的），@head标签是指定下载的起始位置（断点续传的位置）
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Header("RANGE") String start, @Url String url);

    @POST("fuck")
    Observable<BaseBean> uploadFile(@Body RequestBody body);

    /**
     * 文件上传
     */

    @POST("upload")
    @Multipart
    Observable<ResponseBody> uploadFileInfo(@QueryMap Map<String, String> options,
                                            @PartMap Map<String, RequestBody> externalFileParameters);

}
