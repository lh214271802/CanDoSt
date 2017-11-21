package com.lh.base;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;


/**
 * Created by liaohui on 2017/9/14.
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
//
//    private static final int DISK_CACHE_SIZE = 100 * 1024 * 1024;
//    // 取1/8最大内存作为最大缓存;
//    public static final int MAX_MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory()) / 8;
//
//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//        //设置磁盘缓存的路径 path
//        final File cacheDir = new File(Constants.PATH_CACHE);
//        builder.setDiskCache(new DiskCache.Factory() {
//            @Override
//            public DiskCache build() {
//                return DiskLruCacheWrapper.get(cacheDir, DISK_CACHE_SIZE);
//            }
//        });
//        // 自定义内存和图片池大小
//        builder.setMemoryCache(new LruResourceCache(MAX_MEMORY_CACHE_SIZE));
//        builder.setBitmapPool(new LruBitmapPool(MAX_MEMORY_CACHE_SIZE));
//
//    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
