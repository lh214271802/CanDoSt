package cn.lh.candost;

import com.lh.base.BaseBean;

import cn.lh.candost.joke.WeiXinBestBean;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by liaohui on 2017/11/14.
 */

public interface MyApiService{

    /**
     * 文件上传
     */

    @POST("query")
    @FormUrlEncoded
    Observable<BaseBean<WeiXinBestBean>> getWeiXinData(@Field("pno") int page,
                                       @Field("ps") int pageSize,
                                       @Field("key") String key,
                                       @Field("dtype") String dataType);
}
