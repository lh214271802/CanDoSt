package cn.lh.candost;

import com.lh.base.BaseBean;

import cn.lh.candost.ui.taoke.TaoKeBean;
import cn.lh.candost.ui.weixinbest.WeiXinBestBean;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by liaohui on 2017/11/14.
 */

public interface MyApiService {

    /**
     * 微信精选
     */

    @POST("http://v.juhe.cn/weixin/query/")
    @FormUrlEncoded
    Observable<BaseBean<WeiXinBestBean>> getWeiXinData(@Field("pno") int page,
                                                       @Field("ps") int pageSize,
                                                       @Field("key") String key,
                                                       @Field("dtype") String dataType);

    /**
     * 微信精选
     */

    @GET
    Observable<BaseBean<WeiXinBestBean>> getWeiXinData(@Url String url,
                                                       @Query("pno") int page,
                                                       @Query("ps") int pageSize,
                                                       @Query("key") String key,
                                                       @Query("dtype") String dataType);


    /**
     * 淘客助手api
     */

    @GET
    Observable<TaoKeBean> getTaokeData(@Url String ur,
                                       @Query("page") int page,
                                       @Query("app_key") String app_key);


}
