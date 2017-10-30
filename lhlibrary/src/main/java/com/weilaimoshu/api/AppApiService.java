package com.weilaimoshu.api;

import com.weilaimoshu.base.BaseBean;
import com.weilaimoshu.ui.activity.guide.AppImgResponse;
import com.weilaimoshu.ui.bean.PhoneCodeBean;
import com.weilaimoshu.ui.bean.PoStatusBean;
import com.weilaimoshu.ui.bean.UserInfoBean;
import com.weilaimoshu.ui.fragment.recommendation.RecommendationBean;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;


/**
 * Created by liaohui on 2017/7/20.
 */

public interface AppApiService {
    @GET("publics/getappimage")
    Observable<BaseBean<AppImgResponse>> getAppImage();


    /**
     * 获取手机验证码
     */
    @GET("auth/getcode")
    Observable<BaseBean<PhoneCodeBean>> getPhoneCode(@Query("mobile") String mobile,
                                                     @Query("code") int type);


    /**
     * 注册账号
     */
    @GET("auth/register")
    Observable<BaseBean<UserInfoBean>> register(@Query("mobile") String mobile,
                                                @Query("password") String password,
                                                @Query("ckpassword") String ckpassword,
                                                @Query("code") String code,
                                                @Query("device_token") String device_token,
                                                @Query("invite") String invite);


    /**
     * 断点续传下载接口,下载地址需要通过@url动态指定（不适固定的），@head标签是指定下载的起始位置（断点续传的位置）
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Header("RANGE") String start, @Url String url);


    /**
     * 查询P主申请状态
     */
    @FormUrlEncoded
    @POST("Auth/poStatus")
    Observable<BaseBean<PoStatusBean>> getPoStatus(@Field("authsign") String authsign, @Field("uid") String uid);

    //文件和参数一起上传
    /*  RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("mid", userInfo.mid)
                .addFormDataPart("token", userInfo.token)
                .addFormDataPart("ptype", ptype)
                .addFormDataPart("pname", pname)
                .addFormDataPart("pbirthday", pbirthday)
                .addFormDataPart("psex", psex)
                .addFormDataPart("Insect_time", insect_time)
                .addFormDataPart("vaccine_time", vaccine_time)
                .addFormDataPart("pet", headPath, RequestBody.create(MediaType.parse("image/*"), headFile))
                .build();
*/
    @POST("fuck")
    Observable<BaseBean> uploadFile(@Body RequestBody body);

    /**
     * 登陆账号
     */
    @GET("auth/login")
    Observable<BaseBean<UserInfoBean>> goLogin(@Query("account") String mobile,
                                               @Query("password") String password,
                                               @Query("from") String from,
                                               @Query("device_token") String device_token);

    /**
     * 找回密码
     */
    @GET("auth/getpass")
    Observable<BaseBean> getPassWord(@Query("mobile") String mobile, @Query("code") String code, @Query("password") String password);


    @FormUrlEncoded
    @POST("user/setavatar")
    Observable<BaseBean> uploadAvatar(@Body RequestBody body);


    /**
     * 首页数据
     */
    @GET("publics/indexRecommend")
    Observable<BaseBean<RecommendationBean>> getRecommendData();

    /**
     * 统计资源的点击次数
     */
    @GET("publics/pageview")
    Observable<BaseBean> countResourceClick(@Query("push_index") String pushIndex);
}
